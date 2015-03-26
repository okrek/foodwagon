package com.rostrade.foodwagon.foodwagon.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Product implements Comparable<Product> {

    public static final int FAVORITE = 1;
    public static final int NOT_FAVORITE = 0;

    private String mName;
    private String mDescription;
    private String mImageUrl;
    private String mPrice;
    private String mWeight;
    private String mId;
    private String mCategory;
    private int mFavoriteState = NOT_FAVORITE;
    private List<Modification> modifications;
    private Modification selectedModification;

    /**
     * Toggles favorite state on call
     */
    public void favoriteToggle() {
        setFavorite((isFavorite() == NOT_FAVORITE) ? FAVORITE : NOT_FAVORITE);
    }

    @Override
    public int compareTo(@NonNull Product another) {
        int result = 0;
        if (this.getSelectedModificationId()!= null && another.getSelectedModificationId() != null) {
            result = this.getSelectedModificationId().compareTo(another.getSelectedModificationId());
        }
        if (result != 0) return result;

        return this.getId().compareTo(another.getId());
    }

    public boolean hasModifications() {
        return modifications != null && modifications.size() > 1;
    }

    public List<Modification> getModifications() {
        if (modifications == null) modifications = new ArrayList<>();
        return modifications;
    }

    public String getSelectedModificationId() {
        if (selectedModification != null) return selectedModification.getModId();
        return null;
    }

    public Modification getSelectedModification() {
        return selectedModification;
    }

    public Modification getModificationById(String id) {
        if (modifications != null) {
            for (Modification modification : modifications) {
                if (modification.getModId().equals(id)) return modification;
            }
        }

        return null;
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

    public void setSelectedModification(Modification selectedModification) {
        this.selectedModification = selectedModification;
    }

    public void setModifications(List<Modification> modifications) {
        this.modifications = modifications;
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

    public int isFavorite() {
        return mFavoriteState;
    }

    public void setFavorite(int mIsFavorite) {
        this.mFavoriteState = mIsFavorite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;
        return getSelectedModificationId().equals(product.getSelectedModificationId())
                && getId().equals(product.getId());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getSelectedModificationId().hashCode();
        return result;
    }
}