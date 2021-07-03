package com.kiaraacademy.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.kiaraacademy.R;
import com.kiaraacademy.data.SharedPreferenceManager;
import com.kiaraacademy.ui.dashboard.DashboardActivity;
import com.kiaraacademy.ui.signin.SigninActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;

                if (SharedPreferenceManager.isSigninComplete())
                    intent = new Intent(SplashActivity.this, DashboardActivity.class);
                else
                    intent = new Intent(SplashActivity.this, SigninActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}
