package com.kiaraacademy.data.handlers;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kiaraacademy.BuildConfig;
import com.kiaraacademy.data.network.APIConstants;
import com.kiaraacademy.data.network.VolleyInvokeWebService;
import com.kiaraacademy.data.network.VolleyResponseInterface;
import com.kiaraacademy.data.request.RequestPurchaseCourse;
import com.kiaraacademy.data.response.ResponseCourse;
import com.kiaraacademy.utils.AppConstants;
import com.kiaraacademy.utils.HandlerCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class ChaptetwiseHandler implements VolleyResponseInterface {

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
        if (tag == AppConstants.RequestConstants.REQUEST_PURCHASE_COURSE) {
            if (object != null) {
                ResponseCourse response = new Gson().fromJson(object.toString(), ResponseCourse.class);
                mHandlerCallback.onSuccess(response, tag);
            }
        }
    }

    @Override
    public void onError(VolleyError error, int tag) {
        mHandlerCallback.onError(error, tag);
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
}


