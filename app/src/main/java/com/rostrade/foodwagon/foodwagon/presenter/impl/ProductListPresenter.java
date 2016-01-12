package com.rostrade.foodwagon.foodwagon.presenter.impl;

import android.content.SharedPreferences;

import com.rostrade.foodwagon.foodwagon.Constants;
import com.rostrade.foodwagon.foodwagon.FoodWagonApp;
import com.rostrade.foodwagon.foodwagon.bus.CategorySelectedEvent;
import com.rostrade.foodwagon.foodwagon.bus.FavoritesClearedEvent;
import com.rostrade.foodwagon.foodwagon.database.DBFlowManager;
import com.rostrade.foodwagon.foodwagon.model.Cart;
import com.rostrade.foodwagon.foodwagon.model.Category;
import com.rostrade.foodwagon.foodwagon.model.NavigationCategory;
import com.rostrade.foodwagon.foodwagon.model.Product;
import com.rostrade.foodwagon.foodwagon.model.ProductCategory;
import com.rostrade.foodwagon.foodwagon.presenter.IProductListPresenter;
import com.rostrade.foodwagon.foodwagon.view.IProductListView;

import java.util.Collections;
import java.util.List;

import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by frankie on 09.01.2016.
 */
public class ProductListPresenter extends BasePresenter<IProductListView> implements IProductListPresenter {

    private List<Product> searchableProducts;
    private Category mCurrentSelectedCategory;
    private Cart mCart;

    private EventBus mEventBus;
    private SharedPreferences mSharedPreferences;

    public ProductListPresenter(IProductListView iView) {
        super(iView);
    }

    @Override
    public void init() {
        mCart = Cart.getInstance();
        searchableProducts = DBFlowManager.getAllProducts();

        mEventBus = EventBus.getDefault();
        mEventBus.register(this);

        mSharedPreferences = ((FoodWagonApp) getView().getContext())
                .getApplicationComponent()
                .provideSharedPreferences();

        String defaultCategoryId =
                mSharedPreferences.getString(Constants.PREFS_KEY_DEFAULT_CATEGORY,
                        Constants.PREFS_DEFAULT_CATEGORY_VALUE);
        mCurrentSelectedCategory = new ProductCategory(null, Integer.parseInt(defaultCategoryId));
    }

    @Override
    public void onViewAttached(IProductListView view) {
        showProductsForCategory(mCurrentSelectedCategory);
    }

    @Override
    public void onSearchCollapsed() {
        showProductsForCategory(mCurrentSelectedCategory);
    }

    @Override
    public void onSearchTextChanged(final String newText) {
        if (newText.length() > 0) {
            Observable.from(searchableProducts)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .filter(new Func1<Product, Boolean>() {
                        @Override
                        public Boolean call(Product product) {
                            return product.getName().toLowerCase().contains(newText.toLowerCase()) ||
                                    product.getDescription().toLowerCase().contains(newText.toLowerCase());
                        }
                    })
                    .toSortedList(new Func2<Product, Product, Integer>() {
                        @Override
                        public Integer call(Product product, Product product2) {
                            return product.getName().compareTo(product2.getName());
                        }
                    })
                    .subscribe(new Action1<List<Product>>() {
                        @Override
                        public void call(List<Product> products) {
                            getView().showProducts(products);
                        }
                    });
        } else {
            getView().showProducts(Collections.<Product>emptyList());
        }
    }

    @Override
    public void showProductsForCategory(final Category category) {
        final Func1<Product, Boolean> showFavoriteFunc = new Func1<Product, Boolean>() {
            @Override
            public Boolean call(Product product) {
                return product.isFavorite();
            }
        };

        final Func1<Product, Boolean> showCategoryFunc = new Func1<Product, Boolean>() {
            @Override
            public Boolean call(Product product) {
                return Integer.parseInt(product.getCategory()) == category.getId();
            }
        };

        Observable<Product> productObservable = Observable.defer(new Func0<Observable<Product>>() {
            @Override
            public Observable<Product> call() {
                return Observable.from(searchableProducts)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .filter(category == NavigationCategory.FAVORITES ? showFavoriteFunc : showCategoryFunc);
            }
        });

        productObservable
                .toList()
                .subscribe(new Action1<List<Product>>() {
                    @Override
                    public void call(List<Product> products) {
                        getView().showProducts(products);
                    }
                });
    }

    @Override
    public void onBuyButtonClicked(Product product) {
        mCart.addItem(product);
    }

    @Override
    public void onListItemClicked(Product product) {
        getView().showProductDetails(product);
    }

    public void onEvent(CategorySelectedEvent event) {
        Category category = event.getCategory();
        if (category instanceof ProductCategory || category == NavigationCategory.FAVORITES) {
            mCurrentSelectedCategory = event.getCategory();
            showProductsForCategory(event.getCategory());
        }
    }

    public void onEvent(FavoritesClearedEvent event) {
        Observable.just(DBFlowManager.getAllProducts())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Product>>() {
                    @Override
                    public void call(List<Product> products) {
                        searchableProducts = products;
                        showProductsForCategory(NavigationCategory.FAVORITES);
                    }
                });
    }

}
