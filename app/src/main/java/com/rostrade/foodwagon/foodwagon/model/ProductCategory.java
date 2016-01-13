package com.rostrade.foodwagon.foodwagon.model;

import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.rostrade.foodwagon.foodwagon.database.FoodWagonDatabase;

@Table(database = FoodWagonDatabase.class)
public class ProductCategory extends BaseModel implements Category {

    @PrimaryKey
    @SerializedName("id")
    int id;

    @Column
    @SerializedName("name")
    String name;

    public ProductCategory() {
    }

    public ProductCategory(String name, int id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }
}
