package com.kiaraacademy.ui.forgetpassword;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.android.volley.VolleyError;
import com.kiaraacademy.R;
import com.kiaraacademy.data.response.BaseResponse;
import com.kiaraacademy.data.response.ResponseSignin;
import com.kiaraacademy.ui.signin.SigninActivity;
import com.kiaraacademy.utils.AppConstants;
import com.kiaraacademy.utils.AppUtils;
import com.kiaraacademy.utils.ValidationError;
import com.kiaraacademy.utils.ValidationManager;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import java.util.List;

public class ForgetPasswordActivity extends AppCompatActivity implements ForgetPwdView {

    private LinearLayoutCompat ll_email_id;
    private AppCompatEditText et_email;
    private AppCompatTextView tv_error_email;
    private LinearLayoutCompat ll_change_pwd;
    private AppCompatEditText et_password;
    private AppCompatTextView tv_error_new_pwd;
    private AppCompatEditText et_conf_password;
    private AppCompatTextView tv_error_conf_pwd;
    private OtpView otp_view;
    private AppCompatButton btn_next;
    private ProgressDialog progressDialog;
    private ForgetPwdPresenter mPresenter;
    private int isStep = 1;
    private String email;
    private String newPassword;
    private String confPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        initComponents();
        attachPresenter();
        addListener();
    }

    private void addListener() {
        otp_view.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                AppUtils.hideSoftKeyboard(ForgetPasswordActivity.this);
                mPresenter.matchOTP(email, otp);
            }
        });
    }

    private void initComponents() {
        ll_email_id = findViewById(R.id.ll_email_id);
        et_email = findViewById(R.id.et_email);
        tv_error_email = findViewById(R.id.tv_error_email);
        ll_change_pwd = findViewById(R.id.ll_change_pwd);
        et_password = findViewById(R.id.et_password);
        tv_error_new_pwd = findViewById(R.id.tv_error_new_pwd);
        et_conf_password = findViewById(R.id.et_conf_password);
        tv_error_conf_pwd = findViewById(R.id.tv_error_conf_pwd);
        otp_view = findViewById(R.id.otp_view);
        btn_next = findViewById(R.id.btn_next);
        ll_email_id.setVisibility(View.VISIBLE);
        otp_view.setVisibility(View.GONE);
        ll_change_pwd.setVisibility(View.GONE);
    }

    private void attachPresenter() {
        mPresenter = new ForgetPwdPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void onSuccess(Object object, Integer tag) {
        if (tag == AppConstants.RequestConstants.REQUEST_FORGET_PWD) {
            if (object instanceof ResponseSignin) {
                ResponseSignin responseSignin = (ResponseSignin) object;
                if (responseSignin.code.equalsIgnoreCase(AppConstants.VolleyConstants.CODE_SUCCESS)) {
                    AppUtils.showToast(ForgetPasswordActivity.this, responseSignin.msg);
                    isStep = 2;
                    ll_email_id.setVisibility(View.GONE);
                    otp_view.setVisibility(View.VISIBLE);
                    ll_change_pwd.setVisibility(View.GONE);
                    btn_next.setVisibility(View.GONE);
                }
            }
        }
        if (tag == AppConstants.RequestConstants.REQUEST_MATCH_OTP) {
            if (object instanceof BaseResponse) {
                BaseResponse response = (BaseResponse) object;
                if (response.code.equalsIgnoreCase(AppConstants.VolleyConstants.CODE_SUCCESS)) {
                    AppUtils.showToast(ForgetPasswordActivity.this, response.msg);
                    isStep = 3;
                    ll_email_id.setVisibility(View.GONE);
                    otp_view.setVisibility(View.GONE);
                    ll_change_pwd.setVisibility(View.VISIBLE);
                    btn_next.setVisibility(View.VISIBLE);
                }
            }
        }
        if (tag == AppConstants.RequestConstants.REQUEST_RESET_PWD) {
            if (object instanceof BaseResponse) {
                BaseResponse response = (BaseResponse) object;
                if (response.code.equalsIgnoreCase(AppConstants.VolleyConstants.CODE_SUCCESS)) {
                    AppUtils.showToast(ForgetPasswordActivity.this, response.msg);
                    navigateToLogin();
                }
            }
        }
    }

    private void navigateToLogin() {
        Intent intent = new Intent(ForgetPasswordActivity.this, SigninActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onError(Object object, Integer tag) {
        if (object instanceof VolleyError) {
            VolleyError volleyError = (VolleyError) object;
            String strMsg = volleyError.getMessage() != null ? volleyError.getMessage() : "";
            if (!TextUtils.isEmpty(strMsg) && strMsg.split(">").length > 1) {
                String msg = strMsg.split(">")[1];
                AppUtils.showToast(this, msg);
            } else
                AppUtils.showErrorToast(this);
        }
    }

    @Override
    public void showProgress(String message) {
        if (!isFinishing())
            progressDialog = AppUtils.generateProgressDialog(this, R.string.empty_title, message,
                    false, false);
    }

    @Override
    public void hideProgress() {
        AppUtils.dismissProgressDialog(progressDialog);
    }

    @Override
    public void showNetworkDialog(Integer tag) {
        AppUtils.showAlertToEnableMobileData(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    public void nextClick(View view) {
        if (isStep == 1) {
            email = et_email.getText().toString().trim();
            List<ValidationError> validationErrorArrayList = ValidationManager.validateEmail(email);
            if (validateUser(validationErrorArrayList)) {
                AppUtils.hideSoftKeyboard(ForgetPasswordActivity.this);
                mPresenter.forgetPassword(email);
            }
        } else if (isStep == 3) {
            newPassword = et_password.getText().toString().trim();
            confPassword = et_conf_password.getText().toString().trim();
            List<ValidationError> validationErrorArrayList = ValidationManager.validatePwd(newPassword, confPassword);
            if (validateUser(validationErrorArrayList)) {
                AppUtils.hideSoftKeyboard(ForgetPasswordActivity.this);
                mPresenter.resetPassword(email, newPassword);
            }
        }
    }

    private boolean validateUser(List<ValidationError> validationErrorArrayList) {
        boolean isValidUserData = true;
        if (!validationErrorArrayList.isEmpty()) {
            int errorLength = validationErrorArrayList.size();
            resetErrorView(tv_error_email, tv_error_new_pwd, tv_error_conf_pwd);
            for (int index = 0; index < errorLength; index++) {
                ValidationError error = validationErrorArrayList.get(index);

                switch (error.errorCode) {
                    case AppConstants.ValidationConstants.ERROR_CODE_INVALID_EMAIL:
                        isValidUserData = false;
                        handleError(tv_error_email, "Fill a valid email id");
                        break;
                    case AppConstants.ValidationConstants.ERROR_CODE_INVALID_PASSWORD:
                        isValidUserData = false;
                        handleError(tv_error_new_pwd, "Password must be of 6 letters");
                        break;
                    case AppConstants.ValidationConstants.ERROR_CODE_CONF_PWD_BLANK:
                        isValidUserData = false;
                        handleError(tv_error_conf_pwd, "Password must be of 6 letters");
                        break;
                    case AppConstants.ValidationConstants.ERROR_CODE_PWD_NOT_MATCHED:
                        isValidUserData = false;
                        handleError(tv_error_new_pwd, "Both passsword should be same.");
                        handleError(tv_error_conf_pwd, "Both passsword should be same.");
                        break;
                    default:
                        break;
                }
            }
        }
        return isValidUserData;
    }

    private void handleError(TextView tvErrorView, String msg) {
        tvErrorView.setVisibility(View.VISIBLE);
        tvErrorView.setText(msg);
    }

    private void resetErrorView(TextView etErrorEmail, TextView etErrorPwd, TextView etErrorConfPwd) {
        etErrorEmail.setVisibility(View.GONE);
        etErrorPwd.setVisibility(View.GONE);
    }
}
