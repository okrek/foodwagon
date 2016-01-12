package com.rostrade.foodwagon.foodwagon.presenter.impl;

import android.content.SharedPreferences;

import com.rostrade.foodwagon.foodwagon.Constants;
import com.rostrade.foodwagon.foodwagon.FoodWagonApp;
import com.rostrade.foodwagon.foodwagon.database.DBFlowManager;
import com.rostrade.foodwagon.foodwagon.model.Product;
import com.rostrade.foodwagon.foodwagon.model.ProductCategory;
import com.rostrade.foodwagon.foodwagon.network.services.FoodWagonService;
import com.rostrade.foodwagon.foodwagon.presenter.ISplashScreenPresenter;
import com.rostrade.foodwagon.foodwagon.utils.NetworkHelper;
import com.rostrade.foodwagon.foodwagon.view.ISplashActivityView;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by frankie on 08.01.2016.
 */
public class SplashScreenPresenter extends BasePresenter<ISplashActivityView> implements ISplashScreenPresenter {

    @Inject SharedPreferences mSharedPreferences;
    @Inject FoodWagonService mFoodWagonService;

    private List<ProductCategory> mCategories;

    public SplashScreenPresenter(ISplashActivityView iView) {
        super(iView);
    }

    @Override
    public void init() {
        ((FoodWagonApp) getView().getContext())
                .getApplicationComponent().inject(this);
    }

    @Override
    public void onViewAttached(ISplashActivityView view) {
        boolean hasDatabase = false; // preferences.getBoolean("hasDatabase", false);
        boolean autoupdateEnabled = mSharedPreferences.getBoolean(Constants.PREFS_KEY_AUTOUPDATE_ENABLED, false);

        if (!hasDatabase || autoupdateEnabled) {
            if (NetworkHelper.isOnline(getView().getContext())) {
                fetchData();
            } else {
                getView().showErrorDialog();
            }
        } else {
            getView().showDrawerActivity();
        }
    }

    public void fetchData() {
        mFoodWagonService.fetchCategories()
                .subscribe(new Subscriber<List<ProductCategory>>() {
                    @Override
                    public void onCompleted() {
                        getView().prepareProgressBar(mCategories.size());
                        fetchProducts(mCategories);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showErrorDialog();
                    }

                    @Override
                    public void onNext(List<ProductCategory> categories) {
                        mCategories = categories;
                        DBFlowManager.saveCategories(categories);
                    }
                });
    }

    public void fetchProducts(List<ProductCategory> categories) {
        final AtomicInteger progress = new AtomicInteger();
        Observable.from(categories)
                .concatMap(new Func1<ProductCategory, Observable<List<Product>>>() {
                    @Override
                    public Observable<List<Product>> call(ProductCategory productCategory) {
                        return mFoodWagonService.fetchProductsForCategory(String.valueOf(productCategory.getId()));
                    }
                })
               .doOnNext(new Action1<List<Product>>() {
                   @Override
                   public void call(List<Product> products) {
                        progress.incrementAndGet();
                   }
               })
                .subscribe(new Subscriber<List<Product>>() {
            @Override
            public void onCompleted() {
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putBoolean(Constants.PREFS_KEY_HAS_DATABASE, true);
                editor.putString(Constants.PREFS_KEY_DEFAULT_CATEGORY,
                        String.valueOf(mCategories.get(0).getId()));
                editor.apply();

                getView().showDrawerActivity();
            }

            @Override
            public void onError(Throwable e) {
                getView().showErrorDialog();
            }

            @Override
            public void onNext(List<Product> products) {
                DBFlowManager.saveProducts(products);
                getView().displayDownloadProgress(progress.incrementAndGet());
            }
        });
    }
}
