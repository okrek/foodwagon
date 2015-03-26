package com.rostrade.foodwagon.foodwagon.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.rostrade.foodwagon.foodwagon.R;
import com.rostrade.foodwagon.foodwagon.model.Product;
import com.rostrade.foodwagon.foodwagon.model.ProductManager;
import com.rostrade.foodwagon.foodwagon.utility.BadgeChangeListener;
import com.rostrade.foodwagon.foodwagon.utility.NetworkHelper;
import com.rostrade.foodwagon.foodwagon.utility.Utility;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private BadgeChangeListener badgeChangeListener;
    private LinearLayoutManager mLayoutManager;
    private ProductManager mProductManager;
    private List<Product> mProducts;
    private Context mContext;
    private Utility mUtility;
    private Picasso mPicasso;

    public MainAdapter(List<Product> products, Context context) {
        this.mProducts = products;
        this.mContext = context;

        mProductManager = ProductManager.getInstance(mContext);
        mUtility = Utility.getInstance(mContext);
        mPicasso = new Picasso.Builder(mContext)
                .executor(Executors.newSingleThreadExecutor())
                .build();
    }

    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Product currentProduct = mProducts.get(position);
        final File productImage = mUtility.getProductImage(currentProduct);

        if (position % 2 == 0) {
            holder.rowView.setBackgroundColor(mContext.getResources()
                    .getColor(R.color.color_background));
        } else
            holder.rowView.setBackgroundColor(mContext.getResources()
                    .getColor(R.color.color_background_dark));

        holder.productName.setText(currentProduct.getName());
        holder.productDescription.setText(currentProduct.getDescription());
        holder.productPrice.setText(mUtility.getRoubleSign(String.valueOf(currentProduct.getPrice())));

        if (productImage.exists()) {
            mPicasso.load(productImage)
                    .fit()
                    .into(holder.productImage);
        } else {
            NetworkHelper.getInstance(mContext)
                    .getImageLoader()
                    .get(currentProduct.getImageUrl(), new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    if (response.getBitmap() != null) {
                        try {
                            FileOutputStream out = new FileOutputStream(productImage);
                            response.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, out);
                            out.flush();
                            out.close();
                            mPicasso.load(productImage).fit().into(holder.productImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    // On connection error
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public void setBadgeChangeListener(BadgeChangeListener badgeChangeListener) {
        this.badgeChangeListener = badgeChangeListener;
    }

    public void setLayoutManager(LinearLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
    }

    public void setProducts(List<Product> products) {
        this.mProducts = products;
        notifyDataSetChanged();
        mLayoutManager.scrollToPosition(0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView productName;
        public TextView productDescription;
        public TextView productPrice;
        public ImageView productImage;
        public Button buttonBuy;
        public View rowView;

        public ViewHolder(View itemView) {
            super(itemView);
            rowView = itemView.findViewById(R.id.rowHolder);
            productName = (TextView) itemView.findViewById(R.id.product_name);
            productDescription = (TextView) itemView.findViewById(R.id.product_description);
            productPrice = (TextView) itemView.findViewById(R.id.product_price);
            productImage = (ImageView) itemView.findViewById(R.id.product_image);
            buttonBuy = (Button) itemView.findViewById(R.id.add_to_cart);
            itemView.setOnClickListener(this);
            buttonBuy.setOnClickListener(this);
            productImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Product currentProduct = mProducts.get(getAdapterPosition());
            switch (v.getId()) {
                case R.id.add_to_cart:
                    if (currentProduct.hasModifications()) {
                        DialogManager.getInstance(mContext)
                                .showModificationsDialog(mContext, currentProduct);
                    } else {
                        mProductManager.addToCart(currentProduct);
                        badgeChangeListener.notifyBadge();
                    }
                    break;
                case R.id.product_image:
                    DialogManager.getInstance(mContext).showDetailsDialog(mContext, currentProduct);
                    break;
            }
        }
    }
}