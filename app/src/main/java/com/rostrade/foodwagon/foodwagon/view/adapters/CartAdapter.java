package com.rostrade.foodwagon.foodwagon.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.rostrade.foodwagon.foodwagon.CartItemClickListener;
import com.rostrade.foodwagon.foodwagon.R;
import com.rostrade.foodwagon.foodwagon.model.Cart;
import com.rostrade.foodwagon.foodwagon.model.OrderItem;
import com.rostrade.foodwagon.foodwagon.utility.Utility;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by frankie on 04.01.2016.
 */
public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_TOTAL_COST = 1;

    private List<OrderItem> mDataset;
    private CartItemClickListener mCartItemClickListener;

    public CartAdapter(Cart cart) {
        mDataset = cart.getItems();
    }

    public void setCartItemClickListener(CartItemClickListener cartItemClickListener) {
        mCartItemClickListener = cartItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;
        if (viewType == VIEW_TYPE_ITEM) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
            return new CartItemViewHolder(v);
        }

        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_total, parent, false);
        return new CartTotalCostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == VIEW_TYPE_ITEM) {
            CartItemViewHolder viewHolder = (CartItemViewHolder) holder;
            final OrderItem orderItem = mDataset.get(position);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCartItemClickListener != null) {
                        mCartItemClickListener.onItemClicked(v, orderItem);
                    }
                }
            });

            viewHolder.removeItemImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCartItemClickListener != null) {
                        mCartItemClickListener.onRemoveItemClicked(v, orderItem, position);
                    }
                }
            });

            viewHolder.itemNameTextView.setText(orderItem.getProduct().getName());
            viewHolder.itemPriceTextView.setText(Utility.getInstance(viewHolder.itemView.getContext())
                    .getRoubleSign(String.valueOf(orderItem.getCost())));
            viewHolder.itemQuantityTextView.setText(String.valueOf(orderItem.getQuantity()));
        } else {
            CartTotalCostViewHolder viewHolder = (CartTotalCostViewHolder) holder;
            viewHolder.totalCostTextView.setText(String.valueOf(Cart.getInstance().getTotalCost()));
        }
    }

    @Override
    public int getItemCount() {
        if (!mDataset.isEmpty()) {
            return mDataset.size() + 1; // Footer
        }

        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mDataset.size()) {
            return VIEW_TYPE_ITEM;
        }

        return VIEW_TYPE_TOTAL_COST;
    }

    static class CartItemViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.cart_item_name) TextView itemNameTextView;
        @Bind(R.id.cart_item_modification) TextView itemModificationTextView;
        @Bind(R.id.cart_item_price) TextView itemPriceTextView;
        @Bind(R.id.cart_item_quantity) EditText itemQuantityTextView;
        @Bind(R.id.cart_button_remove) ImageButton removeItemImageView;

        public CartItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class CartTotalCostViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.cart_total) TextView totalCostTextView;

        public CartTotalCostViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
