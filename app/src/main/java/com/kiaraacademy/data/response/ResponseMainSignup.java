package com.kiaraacademy.data.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseMainSignup {
    @SerializedName("profile_image")
    @Expose
    public String profile_image;
    @SerializedName("access_token")
    @Expose
    public String access_token;
    @SerializedName("verification_code")
    @Expose
    String verification_code;
    @SerializedName("is_verified")
    @Expose
    String is_verified;
    @SerializedName("device_token")
    @Expose
    String device_token;
    @SerializedName("password")
    @Expose
    String password;
    @SerializedName("mobile_number")
    @Expose
    String mobile_number;
    @SerializedName("country_code")
    @Expose
    String country_code;
    @SerializedName("email_id")
    @Expose
    String email_id;
    @SerializedName("last_name")
    @Expose
    String last_name;
    @SerializedName("first_name")
    @Expose
    String first_name;
    @SerializedName("_id")
    @Expose
    String id;
}
