package com.rostrade.foodwagon.foodwagon.utility;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;

import com.rostrade.foodwagon.foodwagon.model.Product;
import com.rostrade.foodwagon.foodwagon.view.RoubleSpan;

import java.io.File;

public final class Utility {

    private static Utility mInstance;
    private Typeface mRoubleSupportedTypeface;
    private Context mContext;

    private Utility(Context context) {
        mContext = context;
        mRoubleSupportedTypeface =
                Typeface.createFromAsset(mContext.getAssets(), "fonts/rouble2.ttf");
    }

    public static synchronized Utility getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Utility(context.getApplicationContext());
        }
        return mInstance;
    }

    public SpannableStringBuilder getRoubleSign(String line) {
        SpannableStringBuilder resultSpan = new SpannableStringBuilder(line + '\u20BD');
        RoubleSpan roubleTypefaceSpan = new RoubleSpan(mRoubleSupportedTypeface);
        resultSpan.setSpan(roubleTypefaceSpan, resultSpan.length() - 1, resultSpan.length(), 0);

        return resultSpan;
    }

    public File getProductImage(Product product) {
        String fileName = product.getImageUrl().substring(product.getImageUrl()
                .lastIndexOf('/') + 1, product.getImageUrl().length());
        String path = mContext.getFilesDir().getPath();

        return new File(path + "/" + fileName);
    }
}
