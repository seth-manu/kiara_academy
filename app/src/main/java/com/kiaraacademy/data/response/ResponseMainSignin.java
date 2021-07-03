package com.kiaraacademy.data.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseMainSignin {
    @SerializedName("verification_code")
    @Expose
    String verification_code;
    @SerializedName("is_verified")
    @Expose
    String is_verified;
    @SerializedName("device_token")
    @Expose
    String device_token;
    @SerializedName("password")
    @Expose
    String password;
    @SerializedName("mobile_number")
    @Expose
    String mobile_number;
    @SerializedName("country_code")
    @Expose
    String country_code;
    @SerializedName("profile_image")
    @Expose
    String profile_image;
    @SerializedName("access_token")
    @Expose
    String access_token;
    @SerializedName("email_id")
    @Expose
    String email_id;
    @SerializedName("last_name")
    @Expose
    String last_name;
    @SerializedName("first_name")
    @Expose
    String first_name;
    @SerializedName("_id")
    @Expose
    String id;
    @SerializedName("createdAt")
    @Expose
    String createdAt;
    @SerializedName("updatedAt")
    @Expose
    String updatedAt;
    @SerializedName("__v")
    @Expose
    String v;

    public String getVerification_code() {
        return verification_code;
    }

    public void setVerification_code(String verification_code) {
        this.verification_code = verification_code;
    }

    public String getIs_verified() {
        return is_verified;
    }

    public void setIs_verified(String is_verified) {
        this.is_verified = is_verified;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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