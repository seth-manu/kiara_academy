package com.kiaraacademy.data.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseResponse {
    @SerializedName("code")
    @Expose
    public String code;
    @SerializedName("message")
    @Expose
    public String msg;
}
