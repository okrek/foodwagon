package com.rostrade.foodwagon.foodwagon.presenter;

import com.rostrade.foodwagon.foodwagon.view.IView;

/**
 * Created by frankie on 19.12.2015.
 */
public interface IPresenter<V extends IView> {

    void attachView(V view);

    void detachView();

    void init();

    void onViewAttached(V view);

    void onViewDetached();

}
