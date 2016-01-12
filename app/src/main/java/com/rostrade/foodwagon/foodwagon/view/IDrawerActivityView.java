package com.rostrade.foodwagon.foodwagon.view;

import com.rostrade.foodwagon.foodwagon.model.Category;

import java.util.List;

/**
 * Created by frankie on 19.12.2015.
 */
public interface IDrawerActivityView extends IView {

    void buildDrawer(List<Category> categories);

    void selectDrawerItemForCategory(Category category);

    void showCartBadge(int itemCount);

    void showCart();

    void showContacts();

    void showSettings();
}
