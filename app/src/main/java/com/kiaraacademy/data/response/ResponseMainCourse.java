package com.kiaraacademy.data.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseMainCourse implements Serializable {

    @SerializedName("course_name")
    @Expose
    String course_name;
    @SerializedName("class")
    @Expose
    String classes;
    @SerializedName("board")
    @Expose
    String board;
    @SerializedName("course_type")
    @Expose
    String course_type;
    @SerializedName("course_desc")
    @Expose
    String course_desc;
    @SerializedName("_id")
    @Expose
    String id;
    @SerializedName("course_url_video")
    @Expose
    String course_url_video;
    @SerializedName("createdAt")
    @Expose
    String createdAt;
    @SerializedName("updatedAt")
    @Expose
    String updatedAt;
    @SerializedName("__v")
    @Expose
    String v;
    @SerializedName("course_price")
    @Expose
    String coursePrice;
    @SerializedName("course_tax")
    @Expose
    String courseTax;
    @SerializedName("thumbnail_url")
    @Expose
    String thumbnailUrl;
    @SerializedName("auther_name")
    @Expose
    String author;
    @SerializedName("duration")
    @Expose
    String duration;
    @SerializedName("course_order_id")
    @Expose
    String courseOrderId;
    @SerializedName("payment_success_id")
    @Expose
    String paymentSuccessId;

    public String getCourseOrderId() {
        return courseOrderId;
    }

    public void setCourseOrderId(String courseOrderId) {
        this.courseOrderId = courseOrderId;
    }

    public String getPaymentSuccessId() {
        return paymentSuccessId;
    }

    public void setPaymentSuccessId(String paymentSuccessId) {
        this.paymentSuccessId = paymentSuccessId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCoursePrice() {
        return coursePrice;
    }

    public void setCoursePrice(String coursePrice) {
        this.coursePrice = coursePrice;
    }

    public String getCourseTax() {
        return courseTax;
    }

    public void setCourseTax(String courseTax) {
        this.courseTax = courseTax;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getCourse_type() {
        return course_type;
    }

    public void setCourse_type(String course_type) {
        this.course_type = course_type;
    }

    public String getCourse_desc() {
        return course_desc;
    }

    public void setCourse_desc(String course_desc) {
        this.course_desc = course_desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getCourse_url_video() {
        return course_url_video;
    }

    public void setCourse_url_video(String course_url_video) {
        this.course_url_video = course_url_video;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }
}
