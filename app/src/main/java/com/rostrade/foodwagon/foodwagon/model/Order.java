package com.rostrade.foodwagon.foodwagon.model;

import com.rostrade.foodwagon.foodwagon.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.TreeMap;

public class Order {

    private TreeMap<Product, Integer> orderItems;

    public Order() {
        orderItems = new TreeMap<>();
    }

    public void addItem(Product product, int quantityChange) {
        orderItems.put(product, quantityChange);
    }

    public int getTotalCost() {
        int totalCost = 0;
        for (Map.Entry<Product, Integer> entry : orderItems.entrySet()) {
            totalCost = totalCost + (entry.getKey().getPrice() * entry.getValue());
        }

        return totalCost;
    }

    public TreeMap<Product, Integer> getOrderItems() {
        return orderItems;
    }

    public int getOrderItemsCount() {
        int count = 0;
        for (Integer quantity : orderItems.values()) {
            count = count + quantity;
        }

        return count;
    }

    public String getTotalCostForProduct(Product product) {
        return String.valueOf(product.getPrice() * orderItems.get(product));
    }

    public void setItemQuantity(Product product, String quantity) {
        orderItems.put(product, Integer.valueOf(quantity));
    }

    public String buildJsonOrder() {
        JSONObject jsonOrder = new JSONObject();
        try {
            JSONObject cartItems = new JSONObject();
            for (Product product : getOrderItems().keySet()) {
                JSONObject jsonProduct = new JSONObject();
                jsonProduct.put("count", getOrderItems().get(product));
                jsonProduct.put("name", product.getName());

                if (product.getSelectedModification() == null) {
                    jsonProduct.put("description", "");
                    jsonProduct.put("mod_id", 0);
                } else {
                    jsonProduct.put("description", product.getSelectedModification().getModName()
                            + " : " + product.getSelectedModification().getModValue());
                    jsonProduct.put("mod_id", Integer.parseInt(product.getSelectedModificationId()));
                }

                jsonProduct.put("img", product.getImageUrl());
                jsonProduct.put("cost", product.getPrice());
                jsonProduct.put("price", getTotalCostForProduct(product));

                if (product.getSelectedModification() == null) {
                    cartItems.put(product.getId(), jsonProduct);
                } else {
                    cartItems.put(product.getId() + "_"
                            + product.getSelectedModificationId(), jsonProduct);
                }

            }

            jsonOrder.put("items", cartItems);
            jsonOrder.put("total_count", getOrderItemsCount());
            jsonOrder.put("total_discount", 0);
            jsonOrder.put("discount_percent", 0);
            jsonOrder.put("discount_reason", R.string.discount_reason);
            jsonOrder.put("total_sum", getTotalCost());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonOrder.toString();
    }
}
