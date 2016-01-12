package com.rostrade.foodwagon.foodwagon.network;

import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;

/**
 * Created by frankie on 06.01.2016.
 */
public final class NetworkServiceManager {

    private static final String API_URL = "http://nn.foodwagon.ru/";

    private NetworkServiceManager() {}

    public static <T> T getServiceAdapter(Class<T> service) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(service);
    }
}
