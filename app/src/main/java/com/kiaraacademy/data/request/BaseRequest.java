package com.kiaraacademy.data.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class BaseRequest {
    @SerializedName("posRequestPojo")
    @Expose
    RequestLogin posRequestPojo;
}
