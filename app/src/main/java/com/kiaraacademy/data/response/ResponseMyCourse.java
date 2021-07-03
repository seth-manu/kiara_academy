package com.kiaraacademy.data.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseMyCourse extends BaseResponse {
    @SerializedName("data")
    @Expose
    public ResponseMainMyCourse data;
}
