package com.rostrade.foodwagon.foodwagon;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.rostrade.foodwagon.foodwagon.di.ApplicationComponent;
import com.rostrade.foodwagon.foodwagon.di.ApplicationModule;
import com.rostrade.foodwagon.foodwagon.di.DaggerApplicationComponent;
import com.rostrade.foodwagon.foodwagon.di.NetworkModule;

import timber.log.Timber;


/**
 * Created by frankie on 19.12.2015.
 */
public class FoodWagonApp extends Application {

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
        Timber.plant(new Timber.DebugTree());
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .networkModule(new NetworkModule())
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }
}
