package com.kiaraacademy.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.kiaraacademy.core.BaseApplication;
import com.kiaraacademy.data.response.ResponseMainSignin;
import com.kiaraacademy.utils.AppConstants;

/*
 * This class is a manager to save data in the Shared Preferences.
 * */
public class SharedPreferenceManager {
    private static SharedPreferences mSharedPreferences;

    private SharedPreferenceManager() {
    }

    private static SharedPreferences getInstance() {
        if (mSharedPreferences == null) {
            mSharedPreferences = BaseApplication.getInstance().getSharedPreferences(AppConstants.E_LEARNING_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        }
        return mSharedPreferences;
    }

    public static void setSignUpComplete() {
        if (mSharedPreferences == null)
            mSharedPreferences = getInstance();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(AppConstants.SharedPreferences.IS_SIGN_UP, true);
        editor.apply();
    }

    public static void setSigninComplete(boolean val) {
        if (mSharedPreferences == null)
            mSharedPreferences = getInstance();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(AppConstants.SharedPreferences.IS_SIGN_IN, val);
        editor.apply();
    }

    public static Boolean isSignUpComplete() {
        if (mSharedPreferences == null)
            mSharedPreferences = getInstance();
        return mSharedPreferences.getBoolean(AppConstants.SharedPreferences.IS_SIGN_UP, false);
    }

    public static Boolean isSigninComplete() {
        if (mSharedPreferences == null)
            mSharedPreferences = getInstance();
        return mSharedPreferences.getBoolean(AppConstants.SharedPreferences.IS_SIGN_IN, false);
    }

    public static String getProfileURL() {
        if (mSharedPreferences == null)
            mSharedPreferences = getInstance();
        return mSharedPreferences.getString(AppConstants.SharedPreferences.PROFILE_URL, "");
    }

    public static void setProfileURL(String profileURL) {
        if (mSharedPreferences == null)
            mSharedPreferences = getInstance();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(AppConstants.SharedPreferences.PROFILE_URL, profileURL);
        editor.apply();
    }

    public static void setAccessToken(String accessToken) {
        if (mSharedPreferences == null)
            mSharedPreferences = getInstance();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(AppConstants.SharedPreferences.ACCESS_TOKEN, accessToken);
        editor.apply();
    }

    public static void saveLoginResponse(ResponseMainSignin response) {
        if (mSharedPreferences == null)
            mSharedPreferences = getInstance();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(AppConstants.SharedPreferences.ACCESS_TOKEN, response.getAccess_token());
        editor.putString(AppConstants.SharedPreferences.VERIFICATION_CODE, response.getVerification_code());
        editor.putString(AppConstants.SharedPreferences.DEVICE_TOKEN, response.getDevice_token());
        editor.putString(AppConstants.SharedPreferences.MOBILE_NO, response.getMobile_number());
        editor.putString(AppConstants.SharedPreferences.EMAIL_ID, response.getEmail_id());
        editor.putString(AppConstants.SharedPreferences.ID, response.getId());
        editor.putString(AppConstants.SharedPreferences.FIRST_NAME, response.getFirst_name());
        editor.putString(AppConstants.SharedPreferences.LAST_NAME, response.getLast_name());
        editor.apply();
    }

    public static String getUserId() {
        if (mSharedPreferences == null)
            mSharedPreferences = getInstance();
        return mSharedPreferences.getString(AppConstants.SharedPreferences.ID, "");
    }

    public static String getUserName() {
        if (mSharedPreferences == null)
            mSharedPreferences = getInstance();
        return mSharedPreferences.getString(AppConstants.SharedPreferences.FIRST_NAME, "");
    }

    public static String getUserLastName() {
        if (mSharedPreferences == null)
            mSharedPreferences = getInstance();
        return mSharedPreferences.getString(AppConstants.SharedPreferences.LAST_NAME, "");
    }

    public static String getEmailId() {
        if (mSharedPreferences == null)
            mSharedPreferences = getInstance();
        return mSharedPreferences.getString(AppConstants.SharedPreferences.EMAIL_ID, "");
    }

    public static String getMobileNo() {
        if (mSharedPreferences == null)
            mSharedPreferences = getInstance();
        return mSharedPreferences.getString(AppConstants.SharedPreferences.MOBILE_NO, "");
    }
}
