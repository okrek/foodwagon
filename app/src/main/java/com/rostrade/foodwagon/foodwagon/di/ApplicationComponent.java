package com.rostrade.foodwagon.foodwagon.di;

import android.content.SharedPreferences;

import com.rostrade.foodwagon.foodwagon.network.services.FoodWagonService;
import com.rostrade.foodwagon.foodwagon.presenter.impl.SplashScreenPresenter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by frankie on 03.01.2016.
 */
@Singleton
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface ApplicationComponent {

    FoodWagonService provideFoodWagonService();

    SharedPreferences provideSharedPreferences();

    void inject(SplashScreenPresenter presenter);
}


