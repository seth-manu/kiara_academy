package com.kiaraacademy.data.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestPurchaseCourse extends BaseRequest {
    @SerializedName("courseId")
    @Expose
    private String courseId;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("course_order_id")
    @Expose
    private String courseOrderId;
    @SerializedName("course_price")
    @Expose
    private String coursePrice;
    @SerializedName("payment_success_id")
    @Expose
    private String paymentSuccessId;

    public RequestPurchaseCourse(String courseId, String userId, String coursePrice, String courseOrderId, String paymentSuccessId) {
        this.courseId = courseId;
        this.userId = userId;
        this.coursePrice = coursePrice;
        this.courseOrderId = courseOrderId;
        this.paymentSuccessId = paymentSuccessId;
    }
}
