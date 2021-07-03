package com.kiaraacademy.ui.signin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.android.volley.VolleyError;
import com.kiaraacademy.R;
import com.kiaraacademy.data.SharedPreferenceManager;
import com.kiaraacademy.data.response.ResponseSignin;
import com.kiaraacademy.ui.dashboard.DashboardActivity;
import com.kiaraacademy.ui.forgetpassword.ForgetPasswordActivity;
import com.kiaraacademy.ui.signup.SignupActivity;
import com.kiaraacademy.utils.AppConstants;
import com.kiaraacademy.utils.AppUtils;
import com.kiaraacademy.utils.ValidationError;
import com.kiaraacademy.utils.ValidationManager;

import java.util.List;

public class SigninActivity extends AppCompatActivity implements SigninView {

    private SigninPresenter mPresenter;
    private ProgressDialog progressDialog;
    private EditText etMobile;
    private EditText etPassword;
    private AppCompatTextView tv_error_email;
    private AppCompatTextView tv_error_pwd;
    private AppCompatTextView tvForgetPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_signin);

        initComponents();
        attachPresenter();
    }

    public void loginClick(View view) {
        String email = etMobile.getText().toString().trim();
        String pwd = etPassword.getText().toString().trim();
        List<ValidationError> validationErrorArrayList = ValidationManager.validateUserId(email, pwd);
        if (validateUser(validationErrorArrayList)) {
            AppUtils.hideSoftKeyboard(SigninActivity.this);
            mPresenter.signInUser(email, pwd, "12345");
        }
    }

    private void handleError(TextView tvErrorView, String msg) {
        tvErrorView.setVisibility(View.VISIBLE);
        tvErrorView.setText(msg);
    }

    private void resetErrorView(TextView etErrorEmail, TextView etErrorPwd) {
        etErrorEmail.setVisibility(View.GONE);
        etErrorPwd.setVisibility(View.GONE);
    }

    private boolean validateUser(List<ValidationError> validationErrorArrayList) {
        boolean isValidUserData = true;
        if (!validationErrorArrayList.isEmpty()) {
            int errorLength = validationErrorArrayList.size();
            resetErrorView(tv_error_email, tv_error_pwd);
            for (int index = 0; index < errorLength; index++) {
                ValidationError error = validationErrorArrayList.get(index);

                switch (error.errorCode) {
                    case AppConstants.ValidationConstants.ERROR_CODE_INVALID_EMAIL:
                        isValidUserData = false;
                        handleError(tv_error_email, "Fill a valid email id");
                        break;
                    case AppConstants.ValidationConstants.ERROR_CODE_INVALID_PASSWORD:
                        isValidUserData = false;
                        handleError(tv_error_pwd, "Password must be og 6 letters");
                        break;
                    default:
                        break;
                }
            }
        }
        return isValidUserData;
    }

    public void moveToSignupPage(View view) {
        Intent intent;
        intent = new Intent(this, SignupActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void attachPresenter() {
        mPresenter = new SigninPresenter();
        mPresenter.attachView(this);
    }

    private void initComponents() {
        etMobile = findViewById(R.id.et_mobile);
        etPassword = findViewById(R.id.et_password);
        tv_error_email = findViewById(R.id.tv_error_email);
        tv_error_pwd = findViewById(R.id.tv_error_pwd);
        tvForgetPwd = findViewById(R.id.tv_forget_pwd);
    }


    @Override
    public void onSuccess(Object object, Integer tag) {
        if (tag == AppConstants.RequestConstants.REQUEST_SIGN_IN) {
            if (object instanceof ResponseSignin) {
                ResponseSignin responseSignin = (ResponseSignin) object;
                if (responseSignin.code.equalsIgnoreCase(AppConstants.VolleyConstants.CODE_SUCCESS)) {
                    SharedPreferenceManager.saveLoginResponse(responseSignin.response);
                    SharedPreferenceManager.setSigninComplete(true);
                    navigateToDashboard();
                }
            }
        }
    }

    private void navigateToDashboard() {
        Intent intent;
        intent = new Intent(this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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

    public void forgetPassword(View view) {
        Intent intent = new Intent(SigninActivity.this, ForgetPasswordActivity.class);
        startActivity(intent);
    }
}
