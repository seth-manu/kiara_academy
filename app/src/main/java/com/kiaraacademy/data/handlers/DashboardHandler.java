package com.kiaraacademy.data.handlers;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kiaraacademy.BuildConfig;
import com.kiaraacademy.data.SharedPreferenceManager;
import com.kiaraacademy.data.models.Order;
import com.kiaraacademy.data.network.APIConstants;
import com.kiaraacademy.data.network.VolleyInvokeWebService;
import com.kiaraacademy.data.network.VolleyResponseInterface;
import com.kiaraacademy.data.request.RequestPurchaseCourse;
import com.kiaraacademy.data.request.RequestSaveUnsaveCourse;
import com.kiaraacademy.data.response.BaseResponse;
import com.kiaraacademy.data.response.ResponseChapterwiseCourse;
import com.kiaraacademy.data.response.ResponseCourse;
import com.kiaraacademy.data.response.ResponseMyCourse;
import com.kiaraacademy.data.response.ResponseSavedCourse;
import com.kiaraacademy.utils.AppConstants;
import com.kiaraacademy.utils.HandlerCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class DashboardHandler implements VolleyResponseInterface {

    private HandlerCallback mHandlerCallback;

    public void setHandlerCallBack(HandlerCallback handlerCallback) {
        mHandlerCallback = handlerCallback;
    }

    @Override
    public void onJsonResponse(JSONObject object, int tag) {
        switch (tag) {
            case AppConstants.RequestConstants.REQUEST_GET_ALL_COURSE:
                if (object != null) {
                    ResponseCourse response = new Gson().fromJson(object.toString(), ResponseCourse.class);
                    mHandlerCallback.onSuccess(response, tag);
                }
                break;
            case AppConstants.RequestConstants.REQUEST_MY_COURSE:
                if (object != null) {
                    ResponseMyCourse response = new Gson().fromJson(object.toString(), ResponseMyCourse.class);
                    mHandlerCallback.onSuccess(response, tag);
                }
                break;
            case AppConstants.RequestConstants.REQUEST_SAVED_COURSE:
                if (object != null) {
                    ResponseSavedCourse response = new Gson().fromJson(object.toString(), ResponseSavedCourse.class);
                    mHandlerCallback.onSuccess(response, tag);
                }
                break;
            case AppConstants.RequestConstants.REQUEST_PURCHASE_COURSE:
            case AppConstants.RequestConstants.REQUEST_SAVE_UNSAVE_COURSE:
                if (object != null) {
                    BaseResponse response = new Gson().fromJson(object.toString(), BaseResponse.class);
                    mHandlerCallback.onSuccess(response, tag);
                }
                break;
            case AppConstants.RequestConstants.REQUEST_CHAPTERWISE_COURSE:
                if (object != null) {
                    ResponseChapterwiseCourse response = new Gson().fromJson(object.toString(), ResponseChapterwiseCourse.class);
                    mHandlerCallback.onSuccess(response, tag);
                }
                break;
            case AppConstants.RequestConstants.REQUEST_GET_ORDER_ID:
                if (object != null) {
                    Order response = new Gson().fromJson(object.toString(), Order.class);
                    mHandlerCallback.onSuccess(response, tag);
                }
                break;
            default:
                break;
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

    public void purchaseCourse(String courseId, String userId, String coursePrice, String orderId, String paymentSuccessId) {
        mHandlerCallback.onStart();
        RequestPurchaseCourse request = new RequestPurchaseCourse(courseId, userId, coursePrice, orderId, paymentSuccessId);
        JSONObject payload = null;
        try {
            payload = new JSONObject(new Gson().toJson(request, RequestPurchaseCourse.class));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        VolleyInvokeWebService service = new VolleyInvokeWebService(VolleyInvokeWebService.JSON_TYPE_REQUEST,
                this, Request.Method.POST);
        service.hitWebService(BuildConfig.BASE_URL + APIConstants.API_PURCHASE_COURSE, payload,
                AppConstants.RequestConstants.REQUEST_PURCHASE_COURSE);
    }

    public void getMyCourses() {
        mHandlerCallback.onStart();
        VolleyInvokeWebService service = new VolleyInvokeWebService(VolleyInvokeWebService.JSON_TYPE_REQUEST,
                this, Request.Method.GET);
        service.hitWebService(BuildConfig.BASE_URL + APIConstants.API_GET_MY_COURSE + SharedPreferenceManager.getUserId(), null,
                AppConstants.RequestConstants.REQUEST_MY_COURSE);
    }

    public void getSavedCourses() {
        mHandlerCallback.onStart();
        VolleyInvokeWebService service = new VolleyInvokeWebService(VolleyInvokeWebService.JSON_TYPE_REQUEST,
                this, Request.Method.GET);
        service.hitWebService(BuildConfig.BASE_URL + APIConstants.API_GET_SAVED_COURSE + SharedPreferenceManager.getUserId(), null,
                AppConstants.RequestConstants.REQUEST_SAVED_COURSE);
    }

    public void saveUnsaveCourses(int isSave, String courseId, String userId) {
        mHandlerCallback.onStart();
        RequestSaveUnsaveCourse request = new RequestSaveUnsaveCourse(isSave, courseId, userId);
        JSONObject payload = null;
        try {
            payload = new JSONObject(new Gson().toJson(request, RequestSaveUnsaveCourse.class));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        VolleyInvokeWebService service = new VolleyInvokeWebService(VolleyInvokeWebService.JSON_TYPE_REQUEST,
                this, Request.Method.POST);
        service.hitWebService(BuildConfig.BASE_URL + APIConstants.API_SAVE_UNSAVE_COURSE, payload,
                AppConstants.RequestConstants.REQUEST_SAVE_UNSAVE_COURSE);
    }

    public void getChapterwiseCourse(String courseId) {
        mHandlerCallback.onStart();
        VolleyInvokeWebService service = new VolleyInvokeWebService(VolleyInvokeWebService.JSON_TYPE_REQUEST,
                this, Request.Method.GET);
        service.hitWebService(BuildConfig.BASE_URL + APIConstants.API_CHAPTERWISE_COURSE + courseId, null,
                AppConstants.RequestConstants.REQUEST_CHAPTERWISE_COURSE);
    }

    public void getRazorPayOrderId(JSONObject orderRequest) {
        mHandlerCallback.onStart();
        VolleyInvokeWebService service = new VolleyInvokeWebService(VolleyInvokeWebService.JSON_TYPE_REQUEST,
                this, Request.Method.POST);
        service.hitWebService(BuildConfig.PAYMENT_URL + APIConstants.API_GET_ORDER_ID, orderRequest,
                AppConstants.RequestConstants.REQUEST_GET_ORDER_ID);
    }
}

