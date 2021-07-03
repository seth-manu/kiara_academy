package com.kiaraacademy.ui.forgetpassword;


import com.kiaraacademy.core.NetworkManager;
import com.kiaraacademy.data.handlers.ForgetPwdHandler;
import com.kiaraacademy.ui.base.MvpPresenter;
import com.kiaraacademy.utils.AppConstants;
import com.kiaraacademy.utils.HandlerCallback;

public class ForgetPwdPresenter extends MvpPresenter<ForgetPwdView> implements HandlerCallback {
    private final ForgetPwdHandler mHandler;

    public ForgetPwdPresenter() {
        mHandler = new ForgetPwdHandler();
    }

    @Override
    public void attachView(ForgetPwdView mvpView) {
        super.attachView(mvpView);
        mHandler.setHandlerCallBack(this);
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

    public void forgetPassword(String email) {
        if (NetworkManager.isConnectedToInternet())
            mHandler.forgetPassword(email);
        else
            mMvpView.showNetworkDialog(AppConstants.RequestConstants.REQUEST_FORGET_PWD);
    }

    public void matchOTP(String email, String otp) {
        if (NetworkManager.isConnectedToInternet())
            mHandler.matchOTP(email, otp);
        else
            mMvpView.showNetworkDialog(AppConstants.RequestConstants.REQUEST_MATCH_OTP);
    }

    public void resetPassword(String email, String newPassword) {
        if (NetworkManager.isConnectedToInternet())
            mHandler.resetPassword(email, newPassword);
        else
            mMvpView.showNetworkDialog(AppConstants.RequestConstants.REQUEST_RESET_PWD);
    }
}