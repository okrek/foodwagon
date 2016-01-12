package com.rostrade.foodwagon.foodwagon.presenter.impl;

import com.rostrade.foodwagon.foodwagon.presenter.IPresenter;
import com.rostrade.foodwagon.foodwagon.view.IView;

/**
 * Created by frankie on 11.01.2016.
 */
public abstract class BasePresenter<T extends IView> implements IPresenter<T> {

    private T mIView;

    public BasePresenter(T iView) {
        attachView(iView);
    }

    @Override
    public void attachView(T mvpView) {
        mIView = mvpView;
        init();
        onViewAttached(mIView);
    }

    @Override
    public void detachView() {
        mIView = null;
        onViewDetached();
    }

    public boolean isViewAttached() {
        return mIView != null;
    }

    public T getView() {
        return mIView;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    @Override
    public void onViewAttached(T view) {

    }

    @Override
    public void onViewDetached() {

    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.attachView(IView) before" +
                    " requesting data to the Presenter");
        }
    }
}
