package com.kiaraacademy.data.handlers;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kiaraacademy.BuildConfig;
import com.kiaraacademy.data.network.APIConstants;
import com.kiaraacademy.data.network.VolleyInvokeWebService;
import com.kiaraacademy.data.network.VolleyResponseInterface;
import com.kiaraacademy.data.request.RequestLogin;
import com.kiaraacademy.data.response.BaseResponse;
import com.kiaraacademy.data.response.ResponseSignin;
import com.kiaraacademy.utils.AppConstants;
import com.kiaraacademy.utils.HandlerCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgetPwdHandler implements VolleyResponseInterface {

    private HandlerCallback mHandlerCallback;

    public void setHandlerCallBack(HandlerCallback handlerCallback) {
        mHandlerCallback = handlerCallback;
    }

    @Override
    public void onJsonResponse(JSONObject object, int tag) {
        if (tag == AppConstants.RequestConstants.REQUEST_FORGET_PWD) {
            if (object != null) {
                ResponseSignin response = new Gson().fromJson(object.toString(), ResponseSignin.class);
                mHandlerCallback.onSuccess(response, tag);
            }
        }
        if (tag == AppConstants.RequestConstants.REQUEST_MATCH_OTP) {
            if (object != null) {
                BaseResponse response = new Gson().fromJson(object.toString(), BaseResponse.class);
                mHandlerCallback.onSuccess(response, tag);
            }
        }
        if (tag == AppConstants.RequestConstants.REQUEST_RESET_PWD) {
            if (object != null) {
                BaseResponse response = new Gson().fromJson(object.toString(), BaseResponse.class);
                mHandlerCallback.onSuccess(response, tag);
            }
        }
    }

    @Override
    public void onError(VolleyError error, int tag) {
        mHandlerCallback.onError(error, tag);
    }

    public void forgetPassword(String email) {
        mHandlerCallback.onStart();
        RequestLogin request = new RequestLogin(email);
        JSONObject payload = null;
        try {
            payload = new JSONObject(new Gson().toJson(request, RequestLogin.class));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        VolleyInvokeWebService service = new VolleyInvokeWebService(VolleyInvokeWebService.JSON_TYPE_REQUEST,
                this, Request.Method.POST);
        service.hitWebService(BuildConfig.BASE_URL + APIConstants.API_FORGET_PWD, payload,
                AppConstants.RequestConstants.REQUEST_FORGET_PWD);
    }

    public void matchOTP(String email, String otp) {
        mHandlerCallback.onStart();
        RequestLogin request = new RequestLogin(email, otp, true);
        JSONObject payload = null;
        try {
            payload = new JSONObject(new Gson().toJson(request, RequestLogin.class));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        VolleyInvokeWebService service = new VolleyInvokeWebService(VolleyInvokeWebService.JSON_TYPE_REQUEST,
                this, Request.Method.POST);
        service.hitWebService(BuildConfig.BASE_URL + APIConstants.API_MATCH_OTP, payload,
                AppConstants.RequestConstants.REQUEST_MATCH_OTP);
    }

    public void resetPassword(String email, String newPassword) {
        mHandlerCallback.onStart();
        RequestLogin request = new RequestLogin(email, newPassword, false);
        JSONObject payload = null;
        try {
            payload = new JSONObject(new Gson().toJson(request, RequestLogin.class));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        VolleyInvokeWebService service = new VolleyInvokeWebService(VolleyInvokeWebService.JSON_TYPE_REQUEST,
                this, Request.Method.POST);
        service.hitWebService(BuildConfig.BASE_URL + APIConstants.API_RESET_PWD, payload,
                AppConstants.RequestConstants.REQUEST_RESET_PWD);
    }
}

