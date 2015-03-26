package com.rostrade.foodwagon.foodwagon.ui;


import android.animation.LayoutTransition;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.rostrade.foodwagon.foodwagon.R;
import com.rostrade.foodwagon.foodwagon.model.Order;
import com.rostrade.foodwagon.foodwagon.model.Product;
import com.rostrade.foodwagon.foodwagon.model.ProductManager;
import com.rostrade.foodwagon.foodwagon.utility.Utility;

import java.util.Map;


public class CartFragment extends Fragment implements View.OnClickListener,
        TextView.OnEditorActionListener {

    private ViewGroup mContainerView;
    private ViewGroup mTotalView;
    private ProductManager mProductManager;
    private Utility mUtility;
    private Order mOrder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cart, container, false);
        setHasOptionsMenu(true);

        mContainerView = (ViewGroup) v.findViewById(R.id.orderContainer);
        mProductManager = ProductManager.getInstance(getActivity());
        mUtility = Utility.getInstance(getActivity());
        mContainerView.setLayoutTransition(new LayoutTransition());
        mOrder = mProductManager.getOrderFromDb();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((Button) getActivity().findViewById(R.id.button_process)).setText(R.string.cart_order_process);
        Button buttonBack = ((Button) getActivity().findViewById(R.id.button_back));
        buttonBack.setText("");
        buttonBack.setClickable(false);
        redrawView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_cart, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.clear_cart) {
            mProductManager.clearCart();
            mContainerView.removeAllViews();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addItem(final Product product, Integer quantity) {
        ViewGroup itemView = (ViewGroup) LayoutInflater.from(getActivity()).inflate(
                R.layout.cart_item, mContainerView, false);
        itemView.setTag(product);

        View detailsClickHandler = itemView.findViewById(R.id.details_handler);
        TextView itemName = (TextView) itemView.findViewById(R.id.cart_item_name);
        TextView itemModification = (TextView) itemView.findViewById(R.id.cart_item_modification);
        EditText itemQuantity = (EditText) itemView.findViewById(R.id.cart_item_quantity);
        TextView itemPrice = (TextView) itemView.findViewById(R.id.cart_item_price);
        ImageButton itemRemove = (ImageButton) itemView.findViewById(R.id.cart_button_remove);

        if (product.getSelectedModification() != null) {
            itemModification.setText(product.getSelectedModification().getModValue() + " "
                    + product.getSelectedModification().getModName().toLowerCase());
            itemModification.setVisibility(View.VISIBLE);
        }
        itemName.setText(product.getName());
        itemPrice.setText(mUtility.getRoubleSign(mOrder.getTotalCostForProduct(product)));
        itemQuantity.setText(String.valueOf(quantity));

        itemQuantity.setOnEditorActionListener(this);
        itemRemove.setOnClickListener(this);
        detailsClickHandler.setOnClickListener(this);

        mContainerView.addView(itemView);
    }

    private void redrawView() {
        if (mOrder.getOrderItems().size() > 0) {
            mContainerView.removeAllViews();

            for (Map.Entry<Product, Integer> orderEntry : mOrder.getOrderItems().entrySet()) {
                addItem(orderEntry.getKey(), orderEntry.getValue());
            }

            mTotalView = (ViewGroup) LayoutInflater.from(getActivity()).inflate(
                    R.layout.cart_total, mContainerView, false);
            TextView totalCost = (TextView) mTotalView.findViewById(R.id.cart_total);
            totalCost.setText(mUtility.getRoubleSign(String.valueOf(mOrder.getTotalCost())));
            mContainerView.addView(mTotalView);
        }
    }

    @Override
    public void onClick(View v) {
        View itemView = (View) v.getParent();
        Product currentProduct = (Product) itemView.getTag();

        if (v.getId() == R.id.cart_button_remove) {
            if (currentProduct != null) {
                mProductManager.removeFromCart((Product) itemView.getTag());
                mOrder = mProductManager.getOrderFromDb();
                TextView total_cost = (TextView) mTotalView.findViewById(R.id.cart_total);
                total_cost.setText(mUtility.getRoubleSign(String.valueOf(mOrder.getTotalCost())));
                mContainerView.removeView(itemView);

                if (mOrder.getOrderItems().size() < 1) {
                    mContainerView.removeAllViews();
                }
            }

        } else if (v.getId() == R.id.details_handler) {
            if (currentProduct != null) {
                DialogManager.getInstance(getActivity()).showDetailsDialog(getActivity(),
                        currentProduct);
            }
        }

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
                mProductManager.setOrderItemQuantity(mOrder, currentProduct, String.valueOf(quantity));

                TextView itemPrice = (TextView) rowView.findViewById(R.id.cart_item_price);
                String totalItemCost = String.valueOf(quantity * currentProduct.getPrice());
                itemPrice.setText(mUtility.getRoubleSign(totalItemCost));

                TextView totalCost = (TextView) getActivity().findViewById(R.id.cart_total);
                totalCost.setText(mUtility.getRoubleSign(String.valueOf(mOrder.getTotalCost())));
                v.clearFocus();
            }
        }

        return true;
    }
}
