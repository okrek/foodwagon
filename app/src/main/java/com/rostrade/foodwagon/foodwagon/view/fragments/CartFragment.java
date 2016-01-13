package com.rostrade.foodwagon.foodwagon.view.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.rostrade.foodwagon.foodwagon.listeners.CartItemClickListener;
import com.rostrade.foodwagon.foodwagon.R;
import com.rostrade.foodwagon.foodwagon.model.Cart;
import com.rostrade.foodwagon.foodwagon.model.OrderItem;
import com.rostrade.foodwagon.foodwagon.model.Product;
import com.rostrade.foodwagon.foodwagon.presenter.ICartPresenter;
import com.rostrade.foodwagon.foodwagon.presenter.impl.CartPresenter;
import com.rostrade.foodwagon.foodwagon.utils.Utility;
import com.rostrade.foodwagon.foodwagon.utils.DialogManager;
import com.rostrade.foodwagon.foodwagon.view.ICartView;
import com.rostrade.foodwagon.foodwagon.view.adapters.CartAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;


public class CartFragment extends Fragment implements ICartView, CartItemClickListener,
        TextView.OnEditorActionListener {

    private Utility mUtility;
    private CartAdapter mAdapter;

    @Bind(R.id.cartRecyclerView) RecyclerView mRecyclerView;

    ICartPresenter mCartPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        ButterKnife.bind(this, view);
        mUtility = Utility.getInstance(getActivity());
        initAdapter();
        mCartPresenter = new CartPresenter(this);
    }

    private void initAdapter() {
        mAdapter = new CartAdapter(Cart.getInstance());
        mAdapter.setCartItemClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((Button) getActivity().findViewById(R.id.button_process)).setText(R.string.cart_order_process);
        Button buttonBack = ((Button) getActivity().findViewById(R.id.button_back));
        buttonBack.setText("");
        buttonBack.setClickable(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_cart, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.clear_cart) {
            mCartPresenter.onClearCartClicked();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        View rowView = (View) v.getParent();
        Product currentProduct = (Product) rowView.getTag();

        if (actionId == EditorInfo.IME_ACTION_DONE) {
            if (!v.getText().toString().equals("0") && !v.getText().toString().isEmpty()) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                int quantity = Integer.parseInt(v.getText().toString());
//                mProductManager.setOrderItemQuantity(mOrder, currentProduct, String.valueOf(quantity));

                TextView itemPrice = (TextView) rowView.findViewById(R.id.cart_item_price);
                String totalItemCost = String.valueOf(quantity * currentProduct.getPrice());
                itemPrice.setText(mUtility.getRoubleSign(totalItemCost));

                TextView totalCost = (TextView) getActivity().findViewById(R.id.cart_total);
//                totalCost.setText(mUtility.getRoubleSign(String.valueOf(mOrder.getTotalCost())));
                v.clearFocus();
            }
        }

        return true;
    }

    @Override
    public void showCartContent(Cart cart) {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showDetailsDialog(OrderItem orderItem) {
        DialogManager.getInstance(getActivity()).showDetailsDialog(getActivity(), orderItem.getProduct());
    }

    @Override
    public void showCartContentWithItemChange(Cart cart, int position) {
        if (cart.getItems().isEmpty()) {
            mAdapter.notifyItemRangeRemoved(0, 2);
            return;
        }

        mAdapter.notifyItemRemoved(position);
    }

    @Override
    public void onItemClicked(View v, OrderItem item) {
        mCartPresenter.onItemClicked(item);
    }

    @Override
    public void onRemoveItemClicked(View v, OrderItem item, int position) {
        mCartPresenter.onRemoveItemClicked(item, position);
    }
}
