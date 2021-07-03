package com.kiaraacademy.data.handlers;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kiaraacademy.BuildConfig;
import com.kiaraacademy.data.network.APIConstants;
import com.kiaraacademy.data.network.VolleyInvokeWebService;
import com.kiaraacademy.data.network.VolleyResponseInterface;
import com.kiaraacademy.data.response.ResponseChapterwiseCourse;
import com.kiaraacademy.data.response.ResponseCourse;
import com.kiaraacademy.utils.AppConstants;
import com.kiaraacademy.utils.HandlerCallback;

import org.json.JSONObject;

public class AllCourseHandler implements VolleyResponseInterface {

    private HandlerCallback mHandlerCallback;

    public void setHandlerCallBack(HandlerCallback handlerCallback) {
        mHandlerCallback = handlerCallback;
    }

    @Override
    public void onJsonResponse(JSONObject object, int tag) {
        if (tag == AppConstants.RequestConstants.REQUEST_GET_ALL_COURSE) {
            if (object != null) {
                ResponseCourse response = new Gson().fromJson(object.toString(), ResponseCourse.class);
                mHandlerCallback.onSuccess(response, tag);
            }
        }
        if (tag == AppConstants.RequestConstants.REQUEST_CHAPTERWISE_COURSE) {
            if (object != null) {
                ResponseChapterwiseCourse response = new Gson().fromJson(object.toString(), ResponseChapterwiseCourse.class);
                mHandlerCallback.onSuccess(response, tag);
            }
        }
    }

    @Override
    public void onError(VolleyError error, int tag) {
        mHandlerCallback.onError(error, tag);
    }

    public void getAllCourses() {
        mHandlerCallback.onStart();
        VolleyInvokeWebService service = new VolleyInvokeWebService(VolleyInvokeWebService.JSON_TYPE_REQUEST,
                this, Request.Method.GET);
        service.hitWebService(BuildConfig.BASE_URL + APIConstants.API_GET_ALL_USER, null,
                AppConstants.RequestConstants.REQUEST_GET_ALL_COURSE);
    }

    public void getChapterwiseCourse(String courseId) {
        mHandlerCallback.onStart();
        VolleyInvokeWebService service = new VolleyInvokeWebService(VolleyInvokeWebService.JSON_TYPE_REQUEST,
                this, Request.Method.GET);
        service.hitWebService(BuildConfig.BASE_URL + APIConstants.API_CHAPTERWISE_COURSE + courseId, null,
                AppConstants.RequestConstants.REQUEST_CHAPTERWISE_COURSE);
    }
}


