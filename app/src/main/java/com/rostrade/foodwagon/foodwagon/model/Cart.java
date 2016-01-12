package com.rostrade.foodwagon.foodwagon.model;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.rostrade.foodwagon.foodwagon.database.DBFlowManager;

import java.util.List;

import rx.subjects.PublishSubject;

/**
 * Created by frankie on 03.01.2016.
 */
public class Cart {

    private static Cart sInstance;

    private List<OrderItem> mItems;
    private PublishSubject<Cart> mCartPublishSubject;

    private Cart() {
        mItems = DBFlowManager.getOrderItems();
        mCartPublishSubject = PublishSubject.create();
    }

    public static synchronized Cart getInstance() {
        if (sInstance == null) {
            sInstance = new Cart();
        }

        return sInstance;
    }

    public List<OrderItem> getItems() {
        return mItems;
    }

    public int getItemCount() {
        return mItems.size();
    }

    public double getTotalCost() {
        double totalCost = 0;
        for (OrderItem item : mItems) {
            totalCost += (item.getProduct().getPrice() * item.getQuantity());
        }

        return totalCost;
    }

    public void addItem(Product product) {
        for (OrderItem item : mItems) {
            if (item.getProductId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + 1);
                item.save();

                mCartPublishSubject.onNext(this);
                return;
            }
        }

        OrderItem orderItem = new OrderItem(product, 1);
        mItems.add(orderItem);
        orderItem.save();

        mCartPublishSubject.onNext(this);
    }

    public void removeProduct(Product product) {
        for (OrderItem item : mItems) {
            if (item.getProductId().equals(product.getId())) {
                mItems.remove(item);
                item.delete();

                mCartPublishSubject.onNext(this);
                break;
            }
        }
    }

    public void removeItem(OrderItem orderItem) {
        getItems().remove(orderItem);
        orderItem.delete();
        mCartPublishSubject.onNext(this);
    }

    public void clear() {
        SQLite.delete(OrderItem.class).queryClose();
        mItems.clear();

        mCartPublishSubject.onNext(this);
    }

    public PublishSubject<Cart> getSubject() {
        return mCartPublishSubject;
    }
}
