package com.kiaraacademy.data.models;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Course implements Serializable {

    @SerializedName("CourseId")
    @Expose
    public String CourseId;

    @SerializedName("CourseName")
    @Expose
    public String CourseName;
    @SerializedName("AuthorName")
    @Expose
    public String AuthorName;
    @SerializedName("CoursesImage")
    @Expose
    public Bitmap CoursesImage;

    public String getCourseId() {
        return CourseId;
    }

    public void setCourseId(String courseId) {
        CourseId = courseId;
    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String courseName) {
        CourseName = courseName;
    }

    public String getAuthorName() {
        return AuthorName;
    }

    public void setAuthorName(String authorName) {
        AuthorName = authorName;
    }

}
