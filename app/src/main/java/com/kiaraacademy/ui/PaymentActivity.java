package com.kiaraacademy.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.android.volley.VolleyError;
import com.kiaraacademy.R;
import com.kiaraacademy.data.SharedPreferenceManager;
import com.kiaraacademy.data.models.Order;
import com.kiaraacademy.data.response.ResponseMainCourse;
import com.kiaraacademy.ui.dashboard.DashboardPresenter;
import com.kiaraacademy.ui.dashboard.DashboardView;
import com.kiaraacademy.utils.AppConstants;
import com.kiaraacademy.utils.AppUtils;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener, DashboardView {

    private static final String TAG = PaymentActivity.class.getSimpleName();
    private AppCompatButton btnPayemt;
    private AppCompatImageView ivCourseTitleImage;
    private AppCompatTextView tvCourseName;
    private AppCompatTextView tvCourseFee;
    private AppCompatTextView tvCourseTax;
    private AppCompatTextView tvCourseTotalPrice;
    private AppCompatTextView tvAuthorName;
    private AppCompatTextView tvTotalTime;
    private ResponseMainCourse course = null;
    private int totalPrice;
    private ProgressDialog progressDialog;
    private DashboardPresenter mPresenter;
    private Order orderResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_payment);

        initizeView();
        attachPresenter();
        initListener();
        getIntentData();
        bindSelectedCourseData();

        Checkout.preload(PaymentActivity.this);
    }

    private void attachPresenter() {
        mPresenter = new DashboardPresenter();
        mPresenter.attachView(PaymentActivity.this);
    }

    private void bindSelectedCourseData() {
        tvAuthorName.setText(course.getAuthor());
        tvTotalTime.setText(course.getDuration());
        if (course.getThumbnailUrl() == null || course.getThumbnailUrl().equals(""))
            ivCourseTitleImage.setImageDrawable(getResources().getDrawable(R.drawable.launcher));
        else
            Picasso.get().load(course.getThumbnailUrl()).into(ivCourseTitleImage);

        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.getDefault());
        tvCourseName.setText(course.getCourse_name());
        if (course.getCoursePrice() == null || course.getCoursePrice().equals(""))
            tvCourseFee.setText(formatter.format(0));
        else
            tvCourseFee.setText(formatter.format(Integer.parseInt(course.getCoursePrice())));
        if (course.getCourseTax() == null || course.getCourseTax().equals(""))
            tvCourseTax.setText(formatter.format(0));
        else
            tvCourseTax.setText(formatter.format(course.getCourseTax()));
        if (!course.getCoursePrice().equals("0")) {
            totalPrice = Integer.parseInt(course.getCoursePrice());
            tvCourseTotalPrice.setText(formatter.format(totalPrice));
        } else
            tvCourseTotalPrice.setText(formatter.format(0));

    }

    private void getIntentData() {
        Intent intent = getIntent();
        course = (ResponseMainCourse) intent.getSerializableExtra("course");
    }

    private void initListener() {
        btnPayemt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject orderRequest = new JSONObject();
                    orderRequest.put("amount", course.getCoursePrice() + "00");
                    orderRequest.put("currency", "INR");
                    mPresenter.getRazorpayOrderid(orderRequest);
                } catch (JSONException e) {
                    System.out.println(e.getMessage());
                }
            }
        });
    }

    private void initizeView() {
        btnPayemt = findViewById(R.id.btn_payemt);
        ivCourseTitleImage = findViewById(R.id.iv_course_title_image);
        tvCourseName = findViewById(R.id.tv_course_name);
        tvCourseFee = findViewById(R.id.tv_course_fee);
        tvCourseTax = findViewById(R.id.tv_course_tax);
        tvCourseTotalPrice = findViewById(R.id.tv_course_total_price);
        tvAuthorName = findViewById(R.id.tv_author_name);
        tvTotalTime = findViewById(R.id.tv_total_time);
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Log.d(TAG, "onPaymentSuccess: " + razorpayPaymentID);
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
            mPresenter.purchaseCourse(course.getId(), SharedPreferenceManager.getUserId(), course.getCoursePrice(), orderResponse.order_id, razorpayPaymentID);
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onPaymentError: " + response);
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }

    @Override
    public void onSuccess(Object object, Integer tag) {
        switch (tag) {
            case AppConstants.RequestConstants.REQUEST_GET_ORDER_ID:
                if (object instanceof Order) {
                    orderResponse = (Order) object;
                    startPayment(orderResponse.order_id);
                }
                break;
            default:
                break;
        }
    }

    private void startPayment(String order_id) {

        final Checkout co = new Checkout();
        co.setKeyID(AppConstants.AuthenticationConstants.API_KEY_ID);

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Kiara Academy");
            options.put("description", "Purchase : " + course.getCourse_name());
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", course.getCoursePrice() + "00");
            options.put("order_id", order_id);

            JSONObject preFill = new JSONObject();
            preFill.put("email", SharedPreferenceManager.getEmailId());
            preFill.put("contact", SharedPreferenceManager.getMobileNo());
            options.put("prefill", preFill);

            co.open(PaymentActivity.this, options);
        } catch (Exception e) {
            Toast.makeText(PaymentActivity.this, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
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
        if (!this.isFinishing())
            progressDialog = AppUtils.generateProgressDialog(this, R.string.empty_title, message,
                    false, false);
    }

    @Override
    public void hideProgress() {
        AppUtils.dismissProgressDialog(progressDialog);
    }

    @Override
    public void showNetworkDialog(Integer tag) {
        AppUtils.showAlertToEnableMobileData(PaymentActivity.this);
    }
}