package com.kiaraacademy.utils;

public interface OtpListener {
    void onResendClick();

    void onSubmitClick(String otp, String transactionId);
}
