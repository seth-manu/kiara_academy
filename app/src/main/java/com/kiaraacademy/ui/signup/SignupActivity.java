package com.kiaraacademy.ui.signup;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.android.volley.VolleyError;
import com.kiaraacademy.R;
import com.kiaraacademy.data.SharedPreferenceManager;
import com.kiaraacademy.data.response.ResponseSignUp;
import com.kiaraacademy.ui.signin.SigninActivity;
import com.kiaraacademy.utils.AppConstants;
import com.kiaraacademy.utils.AppUtils;
import com.kiaraacademy.utils.ConfimationListener;
import com.kiaraacademy.utils.ValidationError;
import com.kiaraacademy.utils.ValidationManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

public class SignupActivity extends AppCompatActivity implements SignupView {

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 12345;
    private static final String TAG = "SignupActivity";
    private EditText etFirstname;
    private EditText etLastName;
    private EditText etEmail;
    private EditText etMobile;
    private AppCompatImageButton ibBackButton;
    private AppCompatTextView etErrorFirstname;
    private AppCompatTextView etErrorLastName;
    private AppCompatTextView etErrorMobile;
    private AppCompatTextView etErrorEmail;
    private AppCompatTextView etErrorPwd;
    private AppCompatTextView etErrorConfPwd;
    private Spinner etClass;
    private EditText etPwd;
    private EditText etConfPwd;
    private SignupPresenter mPresenter;
    private ProgressDialog progressDialog;
    private String[] stringClass = {"First", "Second", "Third", "Fourth", "Fifth", "Sixth", "Seventh", "Eighth"};
    private ArrayAdapter arrayAdapter;
    private CircleImageView profileImage;
    private int CAMERA_REQUEST = 1001;
    private Bitmap photo;
    private Retrofit mRetrofit;
    private MultipartBody.Part part;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_signup);

        initComponents();
        setListeners();
        attachPresenter();
    }

    private void setListeners() {
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCameraRequest();
            }
        });

        ibBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void checkCameraRequest() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, AppConstants.PermissionConstants.PERMISSION_REQUEST_CAMERA);
            } else {
                clickImageFromCamera();
            }
        }
    }

    private void clickImageFromCamera() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    private void attachPresenter() {
        mPresenter = new SignupPresenter();
        mPresenter.attachView(this);
    }

    private void initComponents() {
        profileImage = findViewById(R.id.profile_image);
        etFirstname = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_last_name);
        etMobile = findViewById(R.id.et_mobile);
        etClass = findViewById(R.id.spin_class);
        etPwd = findViewById(R.id.et_pwd);
        etConfPwd = findViewById(R.id.et_conf_pwd);
        etEmail = findViewById(R.id.et_email);
        ibBackButton = findViewById(R.id.ib_back_button);
        etErrorFirstname = findViewById(R.id.tv_error_first_name);
        etErrorLastName = findViewById(R.id.tv_error_last_name);
        etErrorEmail = findViewById(R.id.tv_error_email);
        etErrorMobile = findViewById(R.id.tv_error_mobile);
        etErrorPwd = findViewById(R.id.tv_error_pwd);
        etErrorConfPwd = findViewById(R.id.tv_error_conf_pwd);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, stringClass);
        etClass.setAdapter(arrayAdapter);

    }

    public void signUpClick(View view) {
        String firstName = etFirstname.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String mobile = etMobile.getText().toString().trim();
        String pwd = etPwd.getText().toString().trim();
        String confPwd = etConfPwd.getText().toString().trim();
        String classes = etClass.getSelectedItem().toString();

        List<ValidationError> validationErrorArrayList = ValidationManager.validateUserSignUp(firstName, lastName, email, mobile, pwd, confPwd);
        if (validateUser(validationErrorArrayList)) {
            AppUtils.hideSoftKeyboard(SignupActivity.this);

            mPresenter.signUpUser(firstName, lastName, email, "91", mobile, pwd, classes, null);

        }
    }

    private void handleError(TextView tvErrorView, String msg) {
        tvErrorView.setVisibility(View.VISIBLE);
        tvErrorView.setText(msg);
    }

    private void resetErrorView(TextView etErrorFirstname, TextView etErrorLastName, TextView etErrorEmail, TextView etErrorMobile, TextView etErrorPwd, TextView etErrorConfPwd) {
        etErrorFirstname.setVisibility(View.GONE);
        etErrorLastName.setVisibility(View.GONE);
        etErrorEmail.setVisibility(View.GONE);
        etErrorMobile.setVisibility(View.GONE);
        etErrorPwd.setVisibility(View.GONE);
        etErrorConfPwd.setVisibility(View.GONE);
    }

    private boolean validateUser(List<ValidationError> validationErrorArrayList) {
        boolean isValidUserData = true;
        if (!validationErrorArrayList.isEmpty()) {
            int errorLength = validationErrorArrayList.size();
            resetErrorView(etErrorFirstname, etErrorLastName, etErrorEmail, etErrorMobile, etErrorPwd, etErrorConfPwd);
            for (int index = 0; index < errorLength; index++) {
                ValidationError error = validationErrorArrayList.get(index);

                switch (error.errorCode) {
                    case AppConstants.ValidationConstants.ERROR_CODE_INVALID_FNAME:
                        isValidUserData = false;
                        handleError(etErrorFirstname, "First name can not be blank");
                        break;
                    case AppConstants.ValidationConstants.ERROR_CODE_INVALID_LNAME:
                        isValidUserData = false;
                        handleError(etErrorLastName, "Last name can not be blank");
                        break;
                    case AppConstants.ValidationConstants.ERROR_CODE_INVALID_EMAIL:
                        isValidUserData = false;
                        handleError(etErrorEmail, "Fill a valid email id");
                        break;
                    case AppConstants.ValidationConstants.ERROR_CODE_INVALID_MOBILE_NUMBER:
                        isValidUserData = false;
                        handleError(etErrorMobile, "Fill a valid mobile no");
                        break;
                    case AppConstants.ValidationConstants.ERROR_CODE_INVALID_PASSWORD:
                        isValidUserData = false;
                        handleError(etErrorPwd, "Password must be of 6 letters");
                        break;

                    case AppConstants.ValidationConstants.ERROR_CODE_INVALID_CONF_PWD:
                        isValidUserData = false;
                        handleError(etErrorConfPwd, "Password must be of 6 letters");
                        break;
                    case AppConstants.ValidationConstants.ERROR_CODE_PWD_NOT_MATCHED:
                        isValidUserData = false;
                        handleError(etErrorConfPwd, "Both passsword should be same.");
                        handleError(etErrorPwd, "Both passsword should be same.");
                        break;
                    default:
                        break;
                }
            }
        }
        return isValidUserData;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Uri path = data.getData();

            try {
                photo = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                createMultiPartdata(photo);
            } catch (IOException e) {
                e.printStackTrace();
            }

            AppUtils.showImageConfirmationDialog(this, new ConfimationListener() {
                @Override
                public void onOkClick() {
                    profileImage.setImageBitmap(photo);
                }

                @Override
                public void onCancelCLick() {
                    checkCameraRequest();
                }
            });
        }
    }

    @Override
    public void onSuccess(Object object, Integer tag) {
        if (tag == AppConstants.RequestConstants.REQUEST_SIGN_UP) {
            if (object instanceof ResponseSignUp) {
                ResponseSignUp responseSignUp = (ResponseSignUp) object;
                if (responseSignUp.code.equalsIgnoreCase(AppConstants.VolleyConstants.CODE_SUCCESS)) {
                    SharedPreferenceManager.setSignUpComplete();
                    SharedPreferenceManager.setAccessToken(responseSignUp.response.access_token);
                    SharedPreferenceManager.setProfileURL(responseSignUp.response.profile_image);
                    navigateToLogin();
                }
            }
        }
    }

    private void navigateToLogin() {
        Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppConstants.PermissionConstants.PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                clickImageFromCamera();
            else {
                handleDeniedCameraPermission();
            }
        }

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // do your stuff
            } else {
                Toast.makeText(SignupActivity.this, "GET_ACCOUNTS Denied",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handleDeniedCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA))
                goToSettings();
        }
    }

    private void goToSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + getPackageName()));
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void moveToSignin(View view) {
        Intent intent = new Intent(this, SigninActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void createMultiPartdata(Bitmap bitmap) {
        part = buildImageBodyPart("profile_image", bitmap);
    }

    private MultipartBody.Part buildImageBodyPart(String fileName, Bitmap bitmap) {
        File profileImageFile = convertBitmapToFile(fileName, bitmap);
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), profileImageFile);
        return MultipartBody.Part.createFormData(fileName, profileImageFile.getName(), reqFile);
    }


    private File convertBitmapToFile(String fileName, Bitmap bitmap) {
        File file = new File(getCacheDir(), fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream mOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, mOutputStream);

        byte[] bytes = mOutputStream.toByteArray();

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }
}
