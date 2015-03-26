package com.rostrade.foodwagon.foodwagon.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rostrade.foodwagon.foodwagon.R;
import com.rostrade.foodwagon.foodwagon.utility.NetworkHelper;
import com.rostrade.foodwagon.foodwagon.utility.SyncListener;
import com.rostrade.foodwagon.foodwagon.utility.Utility;

public class SplashScreenActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.activity_splash_progress_bar);
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();
        boolean hasDatabase = preferences.getBoolean("hasDatabase", false);
        boolean autoupdateEnabled = preferences.getBoolean("autoUpdate", false);

        Utility mUtility = Utility.getInstance(this);
        mUtility.setSyncListener(new SyncListener() {
            @Override
            public void onTaskStarted(int length) {
                progressBar.setMax(length);
            }

            @Override
            public void onTaskUpdated() {
                int progress = progressBar.getProgress();
                progressBar.setProgress(progress + 1);
                if (progressBar.getProgress() == progressBar.getMax()) {
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                    editor.putBoolean("hasDatabase", true);
                    editor.apply();
                    finish();
                }
            }

            @Override
            public void onError() {
                new MaterialDialog.Builder(SplashScreenActivity.this)
                        .content(R.string.connection_error)
                        .positiveText(R.string.ok)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                dialog.dismiss();
                                finish();
                            }
                        })
                        .show();
            }
        });

        if (!hasDatabase || autoupdateEnabled) {
            if (NetworkHelper.isOnline(this)) {
                mUtility.fetchDatabase();
            } else {
                new MaterialDialog.Builder(this).content(R.string.no_internet_connection)
                        .positiveText("OK")
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                dialog.dismiss();
                                SplashScreenActivity.this.finish();
                            }
                        })
                        .show();
            }
        } else {
            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
            finish();
        }
    }
}