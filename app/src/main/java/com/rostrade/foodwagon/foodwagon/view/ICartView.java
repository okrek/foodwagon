package com.rostrade.foodwagon.foodwagon.view;

import com.rostrade.foodwagon.foodwagon.model.Cart;
import com.rostrade.foodwagon.foodwagon.model.OrderItem;

/**
 * Created by frankie on 04.01.2016.
 */
public interface ICartView extends IView {

    void showCartContent(Cart cart);

    void showDetailsDialog(OrderItem orderItem);

    void showCartContentWithItemChange(Cart cart, int position);
}
