package com.kiaraacademy.data.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestSaveUnsaveCourse extends BaseRequest {
    @SerializedName("courseId")
    @Expose
    private String courseId;

    @SerializedName("userId")
    @Expose
    private String userId;


    @SerializedName("isSave")
    @Expose
    private int isSave;

    public RequestSaveUnsaveCourse(int isSave, String courseId, String userId) {
        this.isSave = isSave;
        this.courseId = courseId;
        this.userId = userId;
    }
}
