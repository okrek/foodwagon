package com.rostrade.foodwagon.foodwagon.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rostrade.foodwagon.foodwagon.R;
import com.rostrade.foodwagon.foodwagon.model.Cart;
import com.rostrade.foodwagon.foodwagon.model.Product;
import com.rostrade.foodwagon.foodwagon.view.activities.CartActivity;
import com.rostrade.foodwagon.foodwagon.view.activities.DrawerActivity;
import com.squareup.picasso.Picasso;

public class DialogManager {

    private static DialogManager mInstance;
    private Utility mUtility;

    private DialogManager(Context context) {
        mUtility = Utility.getInstance(context);
    }

    public static synchronized DialogManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DialogManager(context);
        }

        return mInstance;
    }

//    public void showModificationsDialog(final Context context, final Product product) {
//
//        final List<Modification> modifications = product.getModifications();
//        CharSequence[] modList = new CharSequence[product.getModifications().size()];
//        String title = product.getModifications().get(0).getModName();
//        for (int i = 0; i < modList.length; i++) {
//            modList[i] = modifications.get(i).getModValue();
//        }
//
//        final MaterialDialog dialog = new MaterialDialog.Builder(context)
//                .title(title)
//                .backgroundColor(context.getResources().getColor(R.color.color_background))
//                .customView(R.layout.dialog_modifications, false)
//                .titleColor(context.getResources().getColor(R.color.white_text))
//                .items(modList)
//                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallback() {
//                    @Override
//                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
//                        product.setSelectedModification(modifications.get(which));
//                        View customView = dialog.getCustomView();
//                        TextView price = (TextView) customView.findViewById(R.id.mod_price);
//                        TextView weight = (TextView) customView.findViewById(R.id.mod_weight);
//                        price.setText(mUtility.getRoubleSign(String.valueOf(product
//                                .getSelectedModification()
//                                .getModPrice())));
//                        weight.setText(String.valueOf(product
//                                .getSelectedModification()
//                                .getModWeight())
//                                + context.getResources().getString(R.string.gramms));
//                    }
//                })
//                .alwaysCallSingleChoiceCallback()
//                .itemColorRes(R.color.white_text)
//                .positiveText(R.string.choose)
//                .negativeText(R.string.cancel)
//                .callback(new MaterialDialog.ButtonCallback() {
//                    @Override
//                    public void onPositive(MaterialDialog dialog) {
////                        mProductManager.addToCart(product);
//                        // TODO: show card badge
//                        //((DrawerActivity) context).showCartBadge();
//                    }
//
//                    @Override
//                    public void onNegative(MaterialDialog dialog) {
//                        dialog.dismiss();
//                    }
//                }).build();
//
//        product.setSelectedModification(product.getModifications().get(0));
//        View customView = dialog.getCustomView();
//        TextView price = (TextView) customView.findViewById(R.id.mod_price);
//        TextView weight = (TextView) customView.findViewById(R.id.mod_weight);
//        price.setText(mUtility.getRoubleSign(String.valueOf(product.getSelectedModification()
//                .getModPrice())));
//        weight.setText(String.valueOf(product.getSelectedModification()
//                .getModWeight()) + context.getResources().getString(R.string.gramms));
//
//
//        dialog.show();
//    }

    public void showDetailsDialog(final Context context, final Product product) {

        MaterialDialog dialog;
        if (context instanceof CartActivity) dialog = getCartDetailsDialog(context);
        else if (context instanceof DrawerActivity) dialog = getMainDetailsDialog(context, product);
        else dialog = getCartDetailsDialog(context);

        View view = dialog.getCustomView();
        ImageView imageView = (ImageView) view.findViewById(R.id.dialog_product_image);

        TextView descText = (TextView) view.findViewById(R.id.dialog_description);
        descText.setText(product.getDescription());

        TextView priceText = (TextView) view.findViewById(R.id.dialog_price);
        priceText.setText(mUtility.getRoubleSign(String.valueOf(product.getPrice())));

        TextView weightText = (TextView) view.findViewById(R.id.dialog_weight);
        if (Integer.parseInt(product.getWeight()) > 0) {
            weightText.setText(product.getWeight() + context.getResources().getString(R.string.gramms));
            LinearLayout divider = (LinearLayout) view.findViewById(R.id.dialogDivider);
            divider.setVisibility(View.VISIBLE);
        }

        final ImageButton imageButton = (ImageButton) view.findViewById(R.id.dialog_favorite);
        if (product.isFavorite()) imageButton.setSelected(true);
        else imageButton.setSelected(false);

        Picasso.with(context).load(mUtility.getProductImage(product))
                .config(Bitmap.Config.RGB_565)
                .into(imageView);

        final Animation animation = AnimationUtils.loadAnimation(context, R.anim.animation_pulse);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product.isFavorite()) {
                    imageButton.setSelected(false);
                    product.toggleFavorite();

//                    mProductManager.updateFavorites(product);

                    imageButton.startAnimation(animation);
                } else {
                    imageButton.setSelected(true);
                    product.toggleFavorite();

                    imageButton.startAnimation(animation);
                }
            }
        });

        dialog.show();
    }

    private MaterialDialog getMainDetailsDialog(final Context context, final Product product) {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        Cart.getInstance().addItem(product);

                        dialog.dismiss();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .customView(R.layout.dialog_full_description, false)
                .backgroundColor(context.getResources().getColor(R.color.color_background))
                .positiveText(R.string.add_to_cart)
                .negativeText(R.string.back).build();

        return dialog;
    }

    private MaterialDialog getCartDetailsDialog(Context context) {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .customView(R.layout.dialog_full_description, false)
                .backgroundColor(context.getResources().getColor(R.color.color_background))
                .positiveText(R.string.ok)
                .build();

        return dialog;
    }
}
