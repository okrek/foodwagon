package com.rostrade.foodwagon.foodwagon.presenter.impl;

import com.rostrade.foodwagon.foodwagon.model.Cart;
import com.rostrade.foodwagon.foodwagon.model.OrderItem;
import com.rostrade.foodwagon.foodwagon.presenter.ICartPresenter;
import com.rostrade.foodwagon.foodwagon.view.ICartView;

/**
 * Created by frankie on 04.01.2016.
 */
public class CartPresenter extends BasePresenter<ICartView> implements ICartPresenter {

    private Cart mCart;

    public CartPresenter(ICartView iView) {
        super(iView);
    }

    @Override
    public void init() {
        mCart = Cart.getInstance();
    }

    @Override
    public void onViewAttached(ICartView view) {
        view.showCartContent(mCart);
    }

    @Override
    public void onItemClicked(OrderItem orderItem) {
        getView().showDetailsDialog(orderItem);
    }

    @Override
    public void onRemoveItemClicked(OrderItem orderItem, int position) {
        mCart.removeItem(orderItem);
        getView().showCartContentWithItemChange(mCart, position);
    }

    @Override
    public void onClearCartClicked() {
        mCart.clear();
        getView().showCartContent(mCart);
    }

    @Override
    public void onQuantityChanged(OrderItem orderItem) {

    }
}
