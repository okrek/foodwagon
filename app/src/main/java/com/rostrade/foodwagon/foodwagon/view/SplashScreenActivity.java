package com.rostrade.foodwagon.foodwagon.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rostrade.foodwagon.foodwagon.R;
import com.rostrade.foodwagon.foodwagon.presenter.ISplashScreenPresenter;
import com.rostrade.foodwagon.foodwagon.presenter.impl.SplashScreenPresenter;

public class SplashScreenActivity extends AppCompatActivity implements ISplashActivityView {

    ISplashScreenPresenter mSplashScreenPresenter;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        progressBar = (ProgressBar) findViewById(R.id.activity_splash_progress_bar);
        mSplashScreenPresenter = new SplashScreenPresenter(this);
    }

    @Override
    public void showDrawerActivity() {
        startActivity(new Intent(SplashScreenActivity.this, DrawerActivity.class));
        finish();
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void prepareProgressBar(int maxProgress) {
        progressBar.setMax(maxProgress);
    }

    @Override
    public void displayDownloadProgress(int progress) {
        progressBar.setProgress(progress);
    }

    @Override
    public void showErrorDialog() {
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
}