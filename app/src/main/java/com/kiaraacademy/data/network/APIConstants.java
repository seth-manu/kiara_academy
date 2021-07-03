package com.kiaraacademy.data.network;

public interface APIConstants {

    String API_SIGN_UP = "/createProfile";
    String API_SIGN_IN = "/userLogin";
    String API_GET_ALL_USER = "/getAllCourse";
    String API_PURCHASE_COURSE = "/purchaseCourse";
    String API_GET_MY_COURSE = "/getMyCourses/";
    String API_GET_SAVED_COURSE = "/getSavedCourses/";
    String API_SAVE_UNSAVE_COURSE = "/saveUnsaveCourse";
    String API_CHAPTERWISE_COURSE = "/getChpaterByCourseId/";
    String API_GET_ORDER_ID = "/orders";
    String API_FORGET_PWD = "/forgotPassword";
    String API_MATCH_OTP = "/matchOtp";
    String API_RESET_PWD = "/resetPassword";
}
