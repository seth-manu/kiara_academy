package com.kiaraacademy.ui.signup;

import com.kiaraacademy.core.NetworkManager;
import com.kiaraacademy.data.handlers.SignUpHandler;
import com.kiaraacademy.ui.base.MvpPresenter;
import com.kiaraacademy.utils.AppConstants;
import com.kiaraacademy.utils.HandlerCallback;

import java.io.File;

public class SignupPresenter extends MvpPresenter<SignupView> implements HandlerCallback {
    private final SignUpHandler mSignUpHandler;

    SignupPresenter() {
        mSignUpHandler = new SignUpHandler();
    }

    @Override
    public void attachView(SignupView mvpView) {
        super.attachView(mvpView);
        mSignUpHandler.setHandlerCallBack(this);
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

    public void signUpUser(String firstName, String lastName, String emailId, String countryCode, String mobile, String pwd, String classes, File file) {
        if (NetworkManager.isConnectedToInternet())
            mSignUpHandler.signUpUser(firstName, lastName, emailId, countryCode, mobile, pwd, classes, file);
        else
            mMvpView.showNetworkDialog(AppConstants.RequestConstants.REQUEST_SIGN_UP);
    }
}