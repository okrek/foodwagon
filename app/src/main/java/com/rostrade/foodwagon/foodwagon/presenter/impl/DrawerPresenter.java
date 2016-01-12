package com.rostrade.foodwagon.foodwagon.presenter.impl;

import android.content.SharedPreferences;

import com.rostrade.foodwagon.foodwagon.Constants;
import com.rostrade.foodwagon.foodwagon.FoodWagonApp;
import com.rostrade.foodwagon.foodwagon.bus.CategorySelectedEvent;
import com.rostrade.foodwagon.foodwagon.database.DBFlowManager;
import com.rostrade.foodwagon.foodwagon.model.NavigationCategory;
import com.rostrade.foodwagon.foodwagon.model.Cart;
import com.rostrade.foodwagon.foodwagon.model.Category;
import com.rostrade.foodwagon.foodwagon.model.ProductCategory;
import com.rostrade.foodwagon.foodwagon.presenter.IDrawerPresenter;
import com.rostrade.foodwagon.foodwagon.view.IDrawerActivityView;

import java.util.List;

import de.greenrobot.event.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by frankie on 20.12.2015.
 */
public class DrawerPresenter extends BasePresenter<IDrawerActivityView> implements IDrawerPresenter {


    private Cart mCart;

    private EventBus mEventBus;
    private SharedPreferences mSharedPreferences;

    public DrawerPresenter(IDrawerActivityView iView) {
        super(iView);
    }

    @Override
    public void init() {
        mEventBus = EventBus.getDefault();

        mSharedPreferences = ((FoodWagonApp) getView().getContext())
                .getApplicationComponent().provideSharedPreferences();

        mCart = Cart.getInstance();
        mCart.getSubject()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Cart>() {
                    @Override
                    public void call(Cart cart) {
                        getView().showCartBadge(cart.getItemCount());
                    }
                });
    }

    @Override
    public void onMenuInflated() {
        getView().showCartBadge(mCart.getItemCount());
    }

    @Override
    public void onDrawerRequested() {
        int defaultCategoryId = Integer.parseInt(mSharedPreferences.getString(Constants.PREFS_KEY_DEFAULT_CATEGORY,
                Constants.PREFS_DEFAULT_CATEGORY_VALUE));
        List<Category> categories = DBFlowManager.getCategories();
        getView().buildDrawer(categories);

        if (defaultCategoryId == Constants.FAVORITES_CATEGORY_ID) {
            getView().selectDrawerItemForCategory(NavigationCategory.FAVORITES);
        } else {
            for (Category category : categories) {
                if (category.getId() == defaultCategoryId) {
                    getView().selectDrawerItemForCategory(category);
                }
            }
        }
    }

    @Override
    public void onDrawerItemClicked(final Category category) {
        if (category instanceof ProductCategory) {
            mEventBus.post(new CategorySelectedEvent(category));
        } else {
            switch ((NavigationCategory) category) {
                case CONTACTS:
                    getView().showContacts();
                    break;
                case FAVORITES:
                    mEventBus.post(new CategorySelectedEvent(category));
                    break;
                case SETTINGS:
                    getView().showSettings();
                    break;
            }
        }
    }


    @Override
    public void onCartClicked() {
        getView().showCart();
    }

}
