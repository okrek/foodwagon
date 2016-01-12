package com.rostrade.foodwagon.foodwagon.di;

import com.rostrade.foodwagon.foodwagon.network.services.FoodWagonService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by frankie on 07.01.2016.
 */
@Singleton
@Component(modules = {NetworkModule.class})
public interface NetworkComponent {
    FoodWagonService provideFoodWagonService();
}
