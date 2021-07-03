package com.kiaraacademy.data.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseSignin extends BaseResponse {

    @SerializedName("response")
    @Expose
    public ResponseMainSignin response;
}
