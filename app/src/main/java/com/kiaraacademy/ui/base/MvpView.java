package com.kiaraacademy.ui.base;

public interface MvpView {

    void onSuccess(Object object, Integer tag);

    void onError(Object object, Integer tag);

    void showProgress(String message);

    void hideProgress();

    void showNetworkDialog(Integer tag);

}
