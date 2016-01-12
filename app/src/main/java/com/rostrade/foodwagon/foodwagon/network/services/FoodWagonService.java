package com.rostrade.foodwagon.foodwagon.network.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rostrade.foodwagon.foodwagon.model.Product;
import com.rostrade.foodwagon.foodwagon.model.ProductCategory;
import com.rostrade.foodwagon.foodwagon.network.NetworkServiceManager;
import com.rostrade.foodwagon.foodwagon.network.deserializers.ProductDeserializer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by frankie on 06.01.2016.
 */
public class FoodWagonService {

    public interface FoodWagonRest {
        @POST("catalogs.json")
        Observable<ResponseBody> fetchCategories();

        @POST("products.json")
        @FormUrlEncoded
        Observable<ResponseBody> fetchProductsForCategory(@Field("ctg_id") String categoryId);
    }

    private FoodWagonRest mFoodWagonRest;

    public Observable<List<Product>> fetchProductsForCategory(String categoryId) {
        return getService().fetchProductsForCategory(categoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<ResponseBody, Observable<List<Product>>>() {
                    @Override
                    public Observable<List<Product>> call(ResponseBody responseBody) {
                        String bodyString = null;
                        List<Product> products = new ArrayList<Product>();
                        try {
                            bodyString = responseBody.string();
                            JSONObject response = new JSONObject(bodyString).getJSONObject("products");
                            Iterator iterator = response.keys();
                            while (iterator.hasNext()) {
                                String key = (String) iterator.next();
                                JSONObject currentProduct = response.getJSONObject(key);
                                GsonBuilder gsonBuilder = new GsonBuilder();
                                gsonBuilder.registerTypeAdapter(Product.class, new ProductDeserializer());
                                Gson gson = gsonBuilder.create(); // TODO: move to field
                                products.add(gson.fromJson(currentProduct.toString(), Product.class));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return Observable.just(products);
                    }
                });
    }

    public Observable<List<ProductCategory>> fetchCategories() {
        return getService().fetchCategories()
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<ResponseBody, Observable<List<ProductCategory>>>() {
                    @Override
                    public Observable<List<ProductCategory>> call(ResponseBody responseBody) {
                        String bodyString = null;
                        List<ProductCategory> categories = new ArrayList<ProductCategory>();
                        try {
                            bodyString = responseBody.string();
                            JSONObject response = new JSONObject(bodyString);
                            Iterator iterator = response.keys();
                            while (iterator.hasNext()) {
                                String key = (String) iterator.next();
                                categories.add(new Gson().fromJson(response.get(key).toString(), ProductCategory.class));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return Observable.just(categories);
                    }
                });
    }

    private FoodWagonRest getService() {
        if (mFoodWagonRest == null) {
            mFoodWagonRest = NetworkServiceManager.getServiceAdapter(FoodWagonRest.class);
        }

        return mFoodWagonRest;
    }
}
