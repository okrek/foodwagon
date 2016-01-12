package com.rostrade.foodwagon.foodwagon.di;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by frankie on 03.01.2016.
 */
@Module
public class ApplicationModule {

    Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesAppliction() {
        return mApplication;
    }

    @Provides
    @Singleton
    SharedPreferences providesShadredPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application.getApplicationContext());
    }
}
