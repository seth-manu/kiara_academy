package com.kiaraacademy.ui.dashboard;

import com.kiaraacademy.core.NetworkManager;
import com.kiaraacademy.data.handlers.DashboardHandler;
import com.kiaraacademy.ui.base.MvpPresenter;
import com.kiaraacademy.utils.AppConstants;
import com.kiaraacademy.utils.HandlerCallback;

import org.json.JSONObject;

public class DashboardPresenter extends MvpPresenter<com.kiaraacademy.ui.dashboard.DashboardView> implements HandlerCallback {
    private final DashboardHandler mDashboardHandler;

    public DashboardPresenter() {
        mDashboardHandler = new DashboardHandler();
    }

    @Override
    public void attachView(com.kiaraacademy.ui.dashboard.DashboardView mvpView) {
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

    public void getAllCourses() {
        if (NetworkManager.isConnectedToInternet())
            mDashboardHandler.getAllCourses();
        else
            mMvpView.showNetworkDialog(AppConstants.RequestConstants.REQUEST_GET_ALL_COURSE);
    }

    public void purchaseCourse(String courseId, String userId, String coursePrice, String orderId, String paymentSuccessId) {
        if (NetworkManager.isConnectedToInternet())
            mDashboardHandler.purchaseCourse(courseId, userId, coursePrice, orderId, paymentSuccessId);
        else
            mMvpView.showNetworkDialog(AppConstants.RequestConstants.REQUEST_PURCHASE_COURSE);
    }

    public void getMyCourses() {
        if (NetworkManager.isConnectedToInternet())
            mDashboardHandler.getMyCourses();
        else
            mMvpView.showNetworkDialog(AppConstants.RequestConstants.REQUEST_MY_COURSE);
    }

    public void getSavedCourses() {
        if (NetworkManager.isConnectedToInternet())
            mDashboardHandler.getSavedCourses();
        else
            mMvpView.showNetworkDialog(AppConstants.RequestConstants.REQUEST_SAVED_COURSE);
    }

    public void saveUnsaveCourses(int isSave, String courseId, String userId) {
        if (NetworkManager.isConnectedToInternet())
            mDashboardHandler.saveUnsaveCourses(isSave, courseId, userId);
        else
            mMvpView.showNetworkDialog(AppConstants.RequestConstants.REQUEST_SAVE_UNSAVE_COURSE);
    }

    public void getChapterwiseCourse(String courseId) {
        if (NetworkManager.isConnectedToInternet())
            mDashboardHandler.getChapterwiseCourse(courseId);
        else
            mMvpView.showNetworkDialog(AppConstants.RequestConstants.REQUEST_CHAPTERWISE_COURSE);
    }

    public void getRazorpayOrderid(JSONObject orderRequest) {
        if (NetworkManager.isConnectedToInternet())
            mDashboardHandler.getRazorPayOrderId(orderRequest);
        else
            mMvpView.showNetworkDialog(AppConstants.RequestConstants.REQUEST_GET_ORDER_ID);
    }
}