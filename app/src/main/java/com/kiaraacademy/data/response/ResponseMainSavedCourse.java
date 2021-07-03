package com.kiaraacademy.data.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseMainSavedCourse extends ResponseSignin {
    @SerializedName("saved_courses")
    @Expose
    ArrayList<ResponseMainCourse> mainCourses = new ArrayList<>();

    public ArrayList<ResponseMainCourse> getMainCourses() {
        return mainCourses;
    }

    public void setMainCourses(ArrayList<ResponseMainCourse> mainCourses) {
        this.mainCourses = mainCourses;
    }
}


