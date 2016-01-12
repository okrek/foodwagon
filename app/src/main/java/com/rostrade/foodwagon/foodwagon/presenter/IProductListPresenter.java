package com.rostrade.foodwagon.foodwagon.presenter;

import com.rostrade.foodwagon.foodwagon.model.Category;
import com.rostrade.foodwagon.foodwagon.model.Product;
import com.rostrade.foodwagon.foodwagon.view.IProductListView;

import java.util.List;

/**
 * Created by frankie on 09.01.2016.
 */
public interface IProductListPresenter  {

    void onSearchCollapsed();

    void onSearchTextChanged(String newText);

    void showProductsForCategory(Category category);

    void onBuyButtonClicked(Product product);

    void onListItemClicked(Product product);
}
