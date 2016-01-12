package com.rostrade.foodwagon.foodwagon.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.rostrade.foodwagon.foodwagon.database.FoodWagonDatabase;

/**
 * Created by frankie on 30.12.2015.
 */
@Table(database = FoodWagonDatabase.class, name = "Cart")
public class OrderItem extends BaseModel {

    @PrimaryKey String productId;
    @Column int quantity;

    @Column
    @ForeignKey(saveForeignKeyModel = false)
    Product product;

    public OrderItem() {
    }

    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.productId = product.getId();
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int count) {
        this.quantity = count;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getCost() {
        return getProduct().getPrice() * getQuantity();
    }
}
