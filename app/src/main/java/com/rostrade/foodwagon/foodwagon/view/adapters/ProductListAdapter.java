package com.rostrade.foodwagon.foodwagon.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rostrade.foodwagon.foodwagon.listeners.ProductListItemClickListener;
import com.rostrade.foodwagon.foodwagon.R;
import com.rostrade.foodwagon.foodwagon.model.Product;
import com.rostrade.foodwagon.foodwagon.utils.Utility;
import com.rostrade.foodwagon.foodwagon.utils.DownloadImageTarget;
import com.rostrade.foodwagon.foodwagon.utils.DialogManager;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executors;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    private RecyclerView.LayoutManager mLayoutManager;
    private List<Product> mProducts;
    private Context mContext;
    private Utility mUtility;
    private Picasso mPicasso;

    private ProductListItemClickListener<Product> mProductListItemClickListener;

    public ProductListAdapter(List<Product> products, Context context) {
        this.mProducts = products;
        this.mContext = context;

        mUtility = Utility.getInstance(mContext);
        mPicasso = Picasso.with(mContext);
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mLayoutManager = recyclerView.getLayoutManager();
    }

    @Override
    public ProductListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Product currentProduct = mProducts.get(position);
        final File productImageFile = mUtility.getProductImage(currentProduct);

        if (position % 2 == 0) {
            holder.rowView.setBackgroundColor(mContext.getResources()
                    .getColor(R.color.color_background));
        } else
            holder.rowView.setBackgroundColor(mContext.getResources()
                    .getColor(R.color.color_background_dark));

        holder.productName.setText(currentProduct.getName());
        holder.productDescription.setText(currentProduct.getDescription());
        holder.productPrice.setText(mUtility.getRoubleSign(String.valueOf(currentProduct.getPrice())));

        if (productImageFile.exists()) {
            mPicasso.load(productImageFile)
                    .into(holder.productImage);
        } else {
            holder.productImage.setImageBitmap(null);
            mPicasso.load(currentProduct.getImageUrl())
                    .into(new DownloadImageTarget(productImageFile.getAbsolutePath(),
                            new DownloadImageTarget.Callback() {
                                @Override
                                public void onImageSaved(String filePath) {
                                    File f = new File(filePath);
                                    mPicasso.invalidate(filePath);
                                   mPicasso.load(f).into(holder.productImage);
                                }
                            }));
        }
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public void setProducts(List<Product> products) {
        this.mProducts = products;
        notifyDataSetChanged();
        mLayoutManager.scrollToPosition(0);
    }

    public void setProductListItemClickListener(ProductListItemClickListener<Product> productListItemClickListener) {
        mProductListItemClickListener = productListItemClickListener;
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
                    mProductListItemClickListener.buyButtonClicked(v, currentProduct);

                    break;
                case R.id.product_image:
                    DialogManager.getInstance(mContext).showDetailsDialog(mContext, currentProduct);
                    break;
            }
        }
    }
}