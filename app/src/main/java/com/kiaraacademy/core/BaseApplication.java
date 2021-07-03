package com.kiaraacademy.core;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class BaseApplication extends Application {
    private static BaseApplication mInstance;
    private RequestQueue mRequestQueue;

    public static synchronized BaseApplication getInstance() {
        return mInstance;
    }

    private static synchronized void setInstance(BaseApplication baseApplication) {
        mInstance = baseApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests() {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    return true;
                }
            });
        }
    }
}