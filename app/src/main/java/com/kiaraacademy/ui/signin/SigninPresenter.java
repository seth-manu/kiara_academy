package com.kiaraacademy.ui.signin;


import com.kiaraacademy.core.NetworkManager;
import com.kiaraacademy.data.handlers.SigninHandler;
import com.kiaraacademy.ui.base.MvpPresenter;
import com.kiaraacademy.utils.AppConstants;
import com.kiaraacademy.utils.HandlerCallback;

public class SigninPresenter extends MvpPresenter<SigninView> implements HandlerCallback {
    private final SigninHandler mSigninHandler;

    SigninPresenter() {
        mSigninHandler = new SigninHandler();
    }

    @Override
    public void attachView(SigninView mvpView) {
        super.attachView(mvpView);
        mSigninHandler.setHandlerCallBack(this);
    }

    @Override
    public void onStart() {
        if (isViewAttached()) {
            return;
        }
        String mProgressMessage = AppConstants.STR_LOADING;
        mMvpView.showProgress(mProgressMessage);
    }

    @Override
    public void onSuccess(Object object, Integer tag) {
        if (isViewAttached()) {
            return;
        }
        mMvpView.hideProgress();
        mMvpView.onSuccess(object, tag);
    }

    @Override
    public void onError(Object object, Integer tag) {
        if (isViewAttached()) {
            return;
        }
        mMvpView.hideProgress();
        mMvpView.onError(object, tag);
    }

    public void signInUser(String emailId, String pwd, String deviceToken) {
        if (NetworkManager.isConnectedToInternet())
            mSigninHandler.signinUser(emailId, pwd, deviceToken);
        else
            mMvpView.showNetworkDialog(AppConstants.RequestConstants.REQUEST_SIGN_IN);
    }
}