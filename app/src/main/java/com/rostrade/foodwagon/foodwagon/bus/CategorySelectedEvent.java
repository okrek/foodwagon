package com.rostrade.foodwagon.foodwagon.bus;

import com.rostrade.foodwagon.foodwagon.model.Category;

/**
 * Created by frankie on 09.01.2016.
 */
public class CategorySelectedEvent {

    private Category mCategory;

    public CategorySelectedEvent(Category category) {
        mCategory = category;
    }

    public Category getCategory() {
        return mCategory;
    }
}
