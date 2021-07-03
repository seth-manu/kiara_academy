package com.kiaraacademy.data.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseCourse extends BaseResponse {
    @SerializedName("response")
    @Expose
    ArrayList<ResponseMainCourse> response = new ArrayList<>();

    public ArrayList<ResponseMainCourse> getResponse() {
        return response;
    }

}
