package com.kiaraacademy.data.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kiaraacademy.data.response.ResponseMainSignup;

import java.io.File;

public class RequestSignup {

    @SerializedName("response")
    @Expose
    public ResponseMainSignup response;
    @SerializedName("first_name")
    @Expose
    private String first_name;
    @SerializedName("last_name")
    @Expose
    private String last_name;
    @SerializedName("email_id")
    @Expose
    private String email_id;
    @SerializedName("country_code")
    @Expose
    private String country_code;
    @SerializedName("mobile_number")
    @Expose
    private String mobile_number;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("profile_image")
    @Expose
    private File profile_image;
    @SerializedName("class")
    @Expose
    private String classes;

    public RequestSignup(String first_name, String last_name, String email_id, String country_code, String mobile_number, String password, String classes, File profile_image) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email_id = email_id;
        this.country_code = country_code;
        this.mobile_number = mobile_number;
        this.password = password;
        this.classes = classes;
        this.profile_image = profile_image;
    }
}
