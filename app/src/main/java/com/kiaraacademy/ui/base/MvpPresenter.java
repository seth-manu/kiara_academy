package com.kiaraacademy.ui.base;

import com.kiaraacademy.core.BaseApplication;

public class MvpPresenter<T extends com.kiaraacademy.ui.base.MvpView> {
    protected T mMvpView;

    public void detachView() {
        if (mMvpView != null)
            this.mMvpView = null;
    }

    protected boolean isViewAttached() {
        return mMvpView == null;
    }

    public void attachView(T mvpView) {
        this.mMvpView = mvpView;
        BaseApplication.getInstance().cancelPendingRequests();
    }

    public T getType() {
        return mMvpView;
    }
}