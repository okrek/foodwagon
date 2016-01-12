package com.rostrade.foodwagon.foodwagon.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class RobotoTextView extends TextView {

    private static Typeface sTypeface;

    public RobotoTextView(final Context context) {
        this(context, null);
    }

    public RobotoTextView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RobotoTextView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);

        if (!isInEditMode()) {
            if (sTypeface == null) {
                sTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf");
            }
            setTypeface(sTypeface);
        }

    }
}