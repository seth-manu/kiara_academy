package com.kiaraacademy.data.network;

import android.util.Base64;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.kiaraacademy.core.BaseApplication;
import com.kiaraacademy.data.response.BaseResponse;
import com.kiaraacademy.utils.AppConstants;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VolleyInvokeWebService {

    public static final int JSON_TYPE_REQUEST = 1;
    private static final String TAG = "E-Learning: ";
    public static String videoUrl = null;
    private final int mRequestType;
    private final VolleyResponseInterface mListener;
    private final int mRequestMethod;
    private Map<String, String> headers = null;
    private int requestTag;

    public VolleyInvokeWebService(int requestType, VolleyResponseInterface listener, int method) {
        mRequestType = requestType;
        mListener = listener;
        mRequestMethod = method;
    }

    public void hitWebService(String url, JSONObject payload, int requestTag) {
        if (mRequestType == JSON_TYPE_REQUEST) {
            this.requestTag = requestTag;
            fetchJSONDataFromWebService(url, payload, requestTag);
        }
    }

    private void fetchJSONDataFromWebService(final String URL, JSONObject jsonPayload, final int requestTag) {
        Log.e(TAG, "URL :" + URL);
        if (jsonPayload != null)
            Log.e(TAG, "Request :" + jsonPayload.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(mRequestMethod, URL, jsonPayload,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "Response :" + response.toString());
                        if (requestTag != 109) {
                            BaseResponse baseResponse = new Gson().fromJson(response.toString(), BaseResponse.class);
                            if (baseResponse != null && baseResponse.code != null) {
                                if (baseResponse.code.equals(AppConstants.VolleyConstants.CODE_SUCCESS))
                                    mListener.onJsonResponse(response, requestTag);
                                else
                                    mListener.onError(new VolleyError(baseResponse.code + ">" + baseResponse.msg), requestTag);
                            }
                        } else {
                            mListener.onJsonResponse(response, requestTag);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error :" + error.toString());
                        mListener.onError(error, requestTag);
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() {
                headers = getCustomHeaders();
                Log.e(TAG, "Header :" + headers.toString());
                return headers;
            }
        };
        jsonObjReq.setTag(requestTag);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        BaseApplication.getInstance().addToRequestQueue(jsonObjReq);
    }

    private Map<String, String> getCustomHeaders() {
        if (headers == null)
            headers = new HashMap<>();
        if (requestTag == 109) {
            String credentials = AppConstants.AuthenticationConstants.API_KEY_ID + ":" + AppConstants.AuthenticationConstants.API_KEY_SECRET;
            String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            headers.put("Authorization", auth);
        }
        return headers;
    }
}
