package com.rostrade.foodwagon.foodwagon.listeners;

import android.view.View;

/**
 * Created by frankie on 03.01.2016.
 */
public interface ProductListItemClickListener<T> {

    void onItemClicked(View v, T item);

    void buyButtonClicked(View v, T item);
}
