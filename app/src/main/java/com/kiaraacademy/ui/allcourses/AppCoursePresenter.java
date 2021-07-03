package com.kiaraacademy.ui.allcourses;

import com.kiaraacademy.core.NetworkManager;
import com.kiaraacademy.data.handlers.AllCourseHandler;
import com.kiaraacademy.ui.base.MvpPresenter;
import com.kiaraacademy.utils.AppConstants;
import com.kiaraacademy.utils.HandlerCallback;

public class AppCoursePresenter extends MvpPresenter<AllCourseVIew> implements HandlerCallback {
    private final AllCourseHandler mAllCourseHandler;

    AppCoursePresenter() {
        mAllCourseHandler = new AllCourseHandler();
    }

    @Override
    public void attachView(AllCourseVIew mvpView) {
        super.attachView(mvpView);
        mAllCourseHandler.setHandlerCallBack(this);
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
            mAllCourseHandler.getAllCourses();
        else
            mMvpView.showNetworkDialog(AppConstants.RequestConstants.REQUEST_GET_ALL_COURSE);
    }

    public void getChapterwiseCourse(String courseId) {
        if (NetworkManager.isConnectedToInternet())
            mAllCourseHandler.getChapterwiseCourse(courseId);
        else
            mMvpView.showNetworkDialog(AppConstants.RequestConstants.REQUEST_CHAPTERWISE_COURSE);
    }


}
