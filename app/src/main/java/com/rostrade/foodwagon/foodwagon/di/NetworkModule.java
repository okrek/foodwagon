package com.rostrade.foodwagon.foodwagon.di;

import com.rostrade.foodwagon.foodwagon.network.services.FoodWagonService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by frankie on 07.01.2016.
 */
@Module
public class NetworkModule {

    public NetworkModule() {
    }

    @Provides
    @Singleton
    FoodWagonService provideFoodWagonService() {
        return new FoodWagonService();
    }
}
