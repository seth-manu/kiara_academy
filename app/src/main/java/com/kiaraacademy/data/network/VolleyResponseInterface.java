package com.kiaraacademy.data.network;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface VolleyResponseInterface {

    void onJsonResponse(JSONObject object, int tag);

    void onError(VolleyError error, int tag);
}
