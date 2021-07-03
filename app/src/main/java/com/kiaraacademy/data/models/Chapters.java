package com.kiaraacademy.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Chapters implements Serializable {

    @SerializedName("chapter_name")
    @Expose
    public String chapterName;
    @SerializedName("chapter_desc")
    @Expose
    public String chapterDesc;
    @SerializedName("_id")
    @Expose
    public String id;
    @SerializedName("course_id")
    @Expose
    public String courseId;
    @SerializedName("chapter_url_video")
    @Expose
    public String chapterUrlVideo;
    @SerializedName("createdAt")
    @Expose
    public String createdAt;
    @SerializedName("updatedAt")
    @Expose
    public String updatedAt;
    @SerializedName("__v")
    @Expose
    public String __v;
    @SerializedName("is_free")
    @Expose
    public String isFree;
}
