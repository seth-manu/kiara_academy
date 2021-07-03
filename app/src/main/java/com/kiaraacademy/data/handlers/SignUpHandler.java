package com.kiaraacademy.data.handlers;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kiaraacademy.BuildConfig;
import com.kiaraacademy.data.network.APIConstants;
import com.kiaraacademy.data.network.VolleyInvokeWebService;
import com.kiaraacademy.data.network.VolleyResponseInterface;
import com.kiaraacademy.data.request.RequestSignup;
import com.kiaraacademy.data.response.ResponseSignUp;
import com.kiaraacademy.utils.AppConstants;
import com.kiaraacademy.utils.HandlerCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class SignUpHandler implements VolleyResponseInterface {

    private HandlerCallback mHandlerCallback;

    public void setHandlerCallBack(HandlerCallback handlerCallback) {
        mHandlerCallback = handlerCallback;
    }

    @Override
    public void onJsonResponse(JSONObject object, int tag) {
        if (tag == AppConstants.RequestConstants.REQUEST_SIGN_UP) {
            if (object != null) {
                ResponseSignUp response = new Gson().fromJson(object.toString(), ResponseSignUp.class);
                mHandlerCallback.onSuccess(response, tag);
            }
        }
    }

    @Override
    public void onError(VolleyError error, int tag) {
        mHandlerCallback.onError(error, tag);
    }

    public void signUpUser(String firstName, String lastName, String emailId, String countryCode, String mobile, String pwd, String classes, File file) {
        mHandlerCallback.onStart();
        RequestSignup request = new RequestSignup(firstName, lastName, emailId, countryCode, mobile, pwd, classes, file);
        JSONObject payload = null;
        try {
            payload = new JSONObject(new Gson().toJson(request, RequestSignup.class));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        VolleyInvokeWebService service = new VolleyInvokeWebService(VolleyInvokeWebService.JSON_TYPE_REQUEST,
                this, Request.Method.POST);
        service.hitWebService(BuildConfig.BASE_URL + APIConstants.API_SIGN_UP, payload,
                AppConstants.RequestConstants.REQUEST_SIGN_UP);

    }
}
