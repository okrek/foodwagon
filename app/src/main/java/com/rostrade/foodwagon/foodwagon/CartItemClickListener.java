package com.rostrade.foodwagon.foodwagon;

import android.view.View;

import com.rostrade.foodwagon.foodwagon.model.OrderItem;

/**
 * Created by frankie on 04.01.2016.
 */
public interface CartItemClickListener {

    void onItemClicked(View v, OrderItem item);

    void onRemoveItemClicked(View v, OrderItem item, int position);
}
