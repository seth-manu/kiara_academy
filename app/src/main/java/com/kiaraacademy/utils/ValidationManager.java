package com.kiaraacademy.utils;

import java.util.ArrayList;
import java.util.List;

public class ValidationManager {
    private ValidationManager() {
    }

    private static boolean isFieldEmpty(String data) {
        boolean isFieldEmpty = false;
        if (data == null || data.equals("")) {
            isFieldEmpty = true;
        }
        return isFieldEmpty;
    }

    private static boolean isEmailValid(String email) {
        String emailRegex = AppConstants.VALID_EMAIL_REGEX;
        boolean isFieldValid = true;
        if (!email.isEmpty() && !email.matches(emailRegex)) {
            isFieldValid = false;
        }
        return isFieldValid;
    }

    private static boolean isValidMobileNumber(String mobileNumber) {
        boolean isFieldValid = true;
        if (mobileNumber.length() < 10) {
            isFieldValid = false;
        }
        return isFieldValid;
    }

    private static List<ValidationError> validateMobileNumber(String firstName, String lastName, String email, String mobile, String pwd, String confPwd, List<ValidationError> validationErrorArrayList) {
        if (isFieldEmpty(firstName)) {
            ValidationError validationError = prepareValidationObject(AppConstants.ValidationConstants.ERROR_CODE_INVALID_FNAME);
            validationErrorArrayList.add(validationError);
        }
        if (isFieldEmpty(lastName)) {
            ValidationError validationError = prepareValidationObject(AppConstants.ValidationConstants.ERROR_CODE_INVALID_LNAME);
            validationErrorArrayList.add(validationError);
        }

        if (isFieldEmpty(email) || !isEmailValid(email)) {
            ValidationError validationError = prepareValidationObject(AppConstants.ValidationConstants.ERROR_CODE_INVALID_EMAIL);
            validationErrorArrayList.add(validationError);
        }

//        if (!isFieldEmpty(mobile)) {
//            ValidationError validationError = prepareValidationObject(AppConstants.ValidationConstants.ERROR_CODE_INVALID_MOBILE_NUMBER);
//            validationErrorArrayList.add(validationError);
//        }
//
        if (isFieldEmpty(pwd)) {
            ValidationError validationError = prepareValidationObject(AppConstants.ValidationConstants.ERROR_CODE_INVALID_PASSWORD);
            validationErrorArrayList.add(validationError);
        }
        if (isFieldEmpty(confPwd)) {
            ValidationError validationError = prepareValidationObject(AppConstants.ValidationConstants.ERROR_CODE_INVALID_CONF_PWD);
            validationErrorArrayList.add(validationError);
        }

        if (!isMatched(pwd, confPwd)) {
            ValidationError validationError = prepareValidationObject(AppConstants.ValidationConstants.ERROR_CODE_PWD_NOT_MATCHED);
            validationErrorArrayList.add(validationError);
        }
        return validationErrorArrayList;
    }

    private static boolean isMatched(String pwd, String confPwd) {
        return pwd.equals(confPwd);
    }

    public static List<ValidationError> validateUserSignUp(String firstName, String lastName, String email, String mobile, String pwd, String confPwd) {
        List<ValidationError> validationErrorArrayList = new ArrayList<>();
        return validateMobileNumber(firstName, lastName, email, mobile, pwd, confPwd, validationErrorArrayList);
    }

    private static ValidationError prepareValidationObject(int errorCode) {
        ValidationError validationError = new ValidationError();
        validationError.errorCode = errorCode;

        return validationError;
    }

    public static List<ValidationError> validateUserId(String userId, String pwd) {
        List<ValidationError> validationErrorArrayList = new ArrayList<>();
        if (isFieldEmpty(userId)) {
            ValidationError validationError = prepareValidationObject(AppConstants.ValidationConstants.ERROR_CODE_INVALID_USER_ID);
            validationErrorArrayList.add(validationError);
        }
        if (isFieldEmpty(pwd) && isValidPwd(pwd)) {
            ValidationError validationError = prepareValidationObject(AppConstants.ValidationConstants.ERROR_CODE_INVALID_PASSWORD);
            validationErrorArrayList.add(validationError);
        }
        return validationErrorArrayList;
    }

    public static List<ValidationError> validateEmail(String userId) {
        List<ValidationError> validationErrorArrayList = new ArrayList<>();
        if (isFieldEmpty(userId)) {
            ValidationError validationError = prepareValidationObject(AppConstants.ValidationConstants.ERROR_CODE_INVALID_EMAIL);
            validationErrorArrayList.add(validationError);
        }
        return validationErrorArrayList;
    }

    public static List<ValidationError> validatePwd(String newPwd, String confPwd) {
        List<ValidationError> validationErrorArrayList = new ArrayList<>();
        if (isFieldEmpty(newPwd)) {
            ValidationError validationError = prepareValidationObject(AppConstants.ValidationConstants.ERROR_CODE_PWD_BLANK);
            validationErrorArrayList.add(validationError);
        }
        if (isFieldEmpty(confPwd)) {
            ValidationError validationError = prepareValidationObject(AppConstants.ValidationConstants.ERROR_CODE_CONF_PWD_BLANK);
            validationErrorArrayList.add(validationError);
        }
        if (!isMatched(newPwd, confPwd)) {
            ValidationError validationError = prepareValidationObject(AppConstants.ValidationConstants.ERROR_CODE_PWD_NOT_MATCHED);
            validationErrorArrayList.add(validationError);
        }
        return validationErrorArrayList;
    }

    private static boolean isValidPwd(String pwd) {
        boolean isValidPwd = true;
        if (pwd != null) {
            if (pwd.length() < 6) {
                isValidPwd = false;
            }
        }
        return isValidPwd;
    }
}
