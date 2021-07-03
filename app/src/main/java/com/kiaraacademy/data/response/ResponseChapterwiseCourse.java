package com.kiaraacademy.data.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kiaraacademy.data.models.Chapters;

import java.util.ArrayList;

public class ResponseChapterwiseCourse extends BaseResponse {
    @SerializedName("data")
    @Expose
    public ArrayList<Chapters> chapters = new ArrayList<>();
}
