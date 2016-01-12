package com.rostrade.foodwagon.foodwagon.utils;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rostrade.foodwagon.foodwagon.R;

public class CustomListPreference extends ListPreference {
    private MaterialDialog.Builder mBuilder;
    private Context context;

    public CustomListPreference(Context context) {
        super(context);
        this.context = context;
    }

    public CustomListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void showDialog(Bundle state) {
        int preselect = this.findIndexOfValue(this.getValue());
        mBuilder = new MaterialDialog.Builder(context)
                .title(getTitle())
                .icon(getDialogIcon())
                .backgroundColor(context.getResources().getColor(R.color.color_background))
                .titleColor(context.getResources().getColor(R.color.white_text))
                .itemsColorRes(R.color.white_text)
                .negativeText(getNegativeButtonText())
                .items(getEntries())
                .itemsCallbackSingleChoice(preselect, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        onClick(null, DialogInterface.BUTTON_POSITIVE);
                        dialog.dismiss();

                        if (which >= 0 && getEntryValues() != null) {
                            String value = getEntryValues()[which].toString();
                            if (callChangeListener(value))
                                setValue(value);
                        }
                        return true;
                    }
                });

        View contentView = this.onCreateDialogView();
        if (contentView != null) {
            this.onBindDialogView(contentView);
            mBuilder.customView(contentView, false);
        } else {
            mBuilder.content(this.getDialogMessage());
        }

        mBuilder.show();
    }
}