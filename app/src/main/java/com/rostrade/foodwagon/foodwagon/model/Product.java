package com.rostrade.foodwagon.foodwagon.model;

import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.rostrade.foodwagon.foodwagon.database.FoodWagonDatabase;

@ModelContainer
@Table(database = FoodWagonDatabase.class)
public class Product extends BaseModel {

    @Column
    @SerializedName("name") String mName;
    @Column String mDescription;
    @Column String mImageUrl;
    @Column String mPrice;
    @Column String mWeight;

    @PrimaryKey
    @SerializedName("id") String mId;

    @Column String mCategory;
    @Column boolean mIsFavorite = false;

    boolean isFromJson;

    @Override
    public void save() {
        if (!isFromJson()) {
            super.save();
        } else {
            Product product = SQLite.select()
                    .from(Product.class)
                    .where(Product_Table.mId.eq(mId))
                    .querySingle();

            if (product != null) {
                setFavorite(product.isFavorite());
            }

            super.save();
        }
    }

    public void toggleFavorite() {
        setFavorite(!isFavorite());
        save();
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public int getPrice() {
        return Integer.parseInt(mPrice);
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public void setImageUrl(String imageUrl) {
        this.mImageUrl = imageUrl;
    }

    public void setPrice(String price) {
        this.mPrice = price;
    }

    public void setWeight(String weight) {
        this.mWeight = weight;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public void setCategory(String category) {
        this.mCategory = category;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getWeight() {
        return mWeight;
    }

    public String getCategory() {
        return mCategory;
    }

    public boolean isFavorite() {
        return mIsFavorite;
    }

    public void setFavorite(boolean isFavorite) {
        this.mIsFavorite = isFavorite;
    }

    public boolean isFromJson() {
        return isFromJson;
    }

    public void setFromJson(boolean fromJson) {
        isFromJson = fromJson;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (mName != null ? !mName.equals(product.mName) : product.mName != null) return false;
        return !(mId != null ? !mId.equals(product.mId) : product.mId != null);

    }

    @Override
    public int hashCode() {
        int result = mName != null ? mName.hashCode() : 0;
        result = 31 * result + (mId != null ? mId.hashCode() : 0);
        return result;
    }
}