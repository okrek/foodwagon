package com.rostrade.foodwagon.foodwagon.presenter;

import com.rostrade.foodwagon.foodwagon.model.OrderItem;

/**
 * Created by frankie on 04.01.2016.
 */
public interface ICartPresenter {

    void onRemoveItemClicked(OrderItem orderItem, int position);

    void onItemClicked(OrderItem orderItem);

    void onQuantityChanged(OrderItem orderItem);

    void onClearCartClicked();
}
