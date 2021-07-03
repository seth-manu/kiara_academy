package com.kiaraacademy.utils;

public interface AppConstants {
    String E_LEARNING_SHARED_PREFERENCES = "e_learning_shared_preferences";
    String VALID_EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    String STR_LOADING = "Loading... Please wait.";
    String STR_UPLOADING = "Uploading... Please wait.";
    String DATE_FORMAT_DOB = "dd-MM-yyyy";

    interface SharedPreferences {
        String IS_SIGN_UP = "is_sign_up";
        String IS_SIGN_IN = "is_sign_in";
        String ACCESS_TOKEN = "access_token";
        String VERIFICATION_CODE = "verification_code";
        String DEVICE_TOKEN = "device_token";
        String MOBILE_NO = "mobile_number";
        String EMAIL_ID = "email_id";
        String ID = "_id";
        String FIRST_NAME = "first_name";
        String LAST_NAME = "last_name";
        String PROFILE_URL = "profileUrl";
    }

    interface ValidationConstants {
        int ERROR_CODE_BLANK_MOBILE_NUMBER = 1001;
        int ERROR_CODE_INVALID_MOBILE_NUMBER = 1002;
        int ERROR_CODE_INVALID_USER_ID = 1003;
        int ERROR_CODE_INVALID_PASSWORD = 1004;
        int ERROR_CODE_PWD_BLANK = 1005;
        int ERROR_CODE_INVALID_FNAME = 1006;
        int ERROR_CODE_INVALID_LNAME = 1007;
        int ERROR_INVALID_CLASS_SELECTED = 1008;
        int ERROR_CODE_INVALID_EMAIL = 1009;
        int ERROR_CODE_INVALID_CONF_PWD = 1010;
        int ERROR_CODE_PWD_NOT_MATCHED = 1011;
        int ERROR_CODE_CONF_PWD_BLANK = 1012;
    }

    interface AuthenticationConstants {
        String API_KEY_ID = "rzp_test_MmWNp2SjgCEjRV";
        String API_KEY_SECRET = "mhj8xmVxWL4DAk8MlapanmdS";
    }

    interface VolleyConstants {
        String CODE_SUCCESS = "ST_001";
    }

    interface RequestConstants {
        int REQUEST_SIGN_UP = 101;
        int REQUEST_SIGN_IN = 102;
        int REQUEST_GET_ALL_COURSE = 103;
        int REQUEST_PURCHASE_COURSE = 105;
        int REQUEST_MY_COURSE = 104;
        int REQUEST_SAVED_COURSE = 106;
        int REQUEST_SAVE_UNSAVE_COURSE = 107;
        int REQUEST_CHAPTERWISE_COURSE = 108;
        int REQUEST_GET_ORDER_ID = 109;
        int REQUEST_FORGET_PWD = 110;
        int REQUEST_MATCH_OTP = 111;
        int REQUEST_RESET_PWD = 112;
    }

    interface MainConstants {
        int IS_SAVE = 1;
        int IS_UNSAVE = 0;
    }

    interface PermissionConstants {
        int PERMISSION_REQUEST_CAMERA = 1;
    }
}
