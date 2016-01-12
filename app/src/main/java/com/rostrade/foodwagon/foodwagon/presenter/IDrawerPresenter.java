package com.rostrade.foodwagon.foodwagon.presenter;

import com.rostrade.foodwagon.foodwagon.model.Category;
import com.rostrade.foodwagon.foodwagon.model.Product;
import com.rostrade.foodwagon.foodwagon.view.IDrawerActivityView;

/**
 * Created by frankie on 20.12.2015.
 */
public interface IDrawerPresenter {

    void onDrawerItemClicked(Category category);

    void onCartClicked();

    void onCreate();

    void onMenuInflated();
}
