package com.kiaraacademy.data.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseSignUp extends BaseResponse {

    @SerializedName("response")
    @Expose
    public ResponseMainSignup response;
}
