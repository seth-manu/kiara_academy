package com.kiaraacademy.core;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkManager {

    private NetworkManager() {

    }

    /*
     * This method checks the internet connectivity.
     * */
    public static boolean isConnectedToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) BaseApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (NetworkInfo netInfo : info) {
                    if ((netInfo.getTypeName().equalsIgnoreCase("MOBILE") && netInfo.isConnected()) || (netInfo.getTypeName().equalsIgnoreCase("WIFI") && netInfo.isConnected())) {
                        Log.e("Network : ", "isConnectedToInternet:Connected to Mobile Data " + info.length);
                        return true;
                    }
                }
        }
        return false;
    }
}