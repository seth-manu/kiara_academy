package com.kiaraacademy.ui.chapters;


import com.kiaraacademy.core.NetworkManager;
import com.kiaraacademy.data.handlers.ChaptetwiseHandler;
import com.kiaraacademy.ui.base.MvpPresenter;
import com.kiaraacademy.utils.AppConstants;
import com.kiaraacademy.utils.HandlerCallback;

public class ChapterwisePresenter extends MvpPresenter<ChapterwiseView> implements HandlerCallback {
    private final ChaptetwiseHandler mDashboardHandler;

    public ChapterwisePresenter() {
        mDashboardHandler = new ChaptetwiseHandler();
    }

    @Override
    public void attachView(ChapterwiseView mvpView) {
        super.attachView(mvpView);
        mDashboardHandler.setHandlerCallBack(this);
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

    public void purchaseCourse(String courseId, String userId, String coursePrice, String orderId, String paymentSuccessId) {
        if (NetworkManager.isConnectedToInternet())
            mDashboardHandler.purchaseCourse(courseId, userId, coursePrice, orderId, paymentSuccessId);
        else
            mMvpView.showNetworkDialog(AppConstants.RequestConstants.REQUEST_PURCHASE_COURSE);
    }
}