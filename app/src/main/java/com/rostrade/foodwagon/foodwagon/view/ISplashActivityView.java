package com.rostrade.foodwagon.foodwagon.view;

/**
 * Created by frankie on 08.01.2016.
 */
public interface ISplashActivityView extends IView {
    void prepareProgressBar(int maxProgress);

    void displayDownloadProgress(int progress);

    void showErrorDialog();

    void showDrawerActivity();
}
