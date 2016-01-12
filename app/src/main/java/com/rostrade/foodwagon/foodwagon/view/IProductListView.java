package com.rostrade.foodwagon.foodwagon.view;

import com.rostrade.foodwagon.foodwagon.model.Product;

import java.util.List;

/**
 * Created by frankie on 09.01.2016.
 */
public interface IProductListView extends IView {

    void showProducts(List<Product> products);

    void showProductDetails(Product product);
}
