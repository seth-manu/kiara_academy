package com.kiaraacademy.data.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestLogin extends BaseRequest {

    @SerializedName("email_id")
    @Expose
    private String emailId;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("device_token")
    @Expose
    private String device_token;
    @SerializedName("verification_code")
    @Expose
    private String verificationCode;

    private boolean isMatchOtp;

    public RequestLogin(String emailId, String password, String device_token) {
        this.emailId = emailId;
        this.password = password;
        this.device_token = device_token;
    }

    public RequestLogin(String emailId) {
        this.emailId = emailId;
    }

    public RequestLogin(String emailId, String verificationCode, boolean isMatchOtp) {
        this.isMatchOtp = isMatchOtp;
        if (isMatchOtp) {
            this.emailId = emailId;
            this.verificationCode = verificationCode;
        } else {
            this.emailId = emailId;
            this.password = verificationCode;
        }
    }
}
