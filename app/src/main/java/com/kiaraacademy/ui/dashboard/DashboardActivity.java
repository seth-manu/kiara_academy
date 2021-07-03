package com.kiaraacademy.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kiaraacademy.R;
import com.kiaraacademy.data.SharedPreferenceManager;
import com.kiaraacademy.ui.dashboard.fragment.home.HomeFragment;
import com.kiaraacademy.ui.dashboard.fragment.mycourse.MyCourseFragment;
import com.kiaraacademy.ui.dashboard.fragment.settings.SettingFragment;
import com.kiaraacademy.ui.dashboard.fragment.video.VideoFragment;
import com.kiaraacademy.ui.dashboard.fragment.withlist.WithlistFragment;
import com.kiaraacademy.ui.signin.SigninActivity;

public class DashboardActivity extends AppCompatActivity {

    RecyclerView topRecommedation;
    RecyclerView mostViewed;
    BottomNavigationView navigation;
    private FragmentManager mFragmentmanager;
    private TextView windowTitle;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment mFragment;
            switch (item.getItemId()) {
                case R.id.navigation_withlist:
                    mFragment = new MyCourseFragment();
                    break;
                case R.id.navigation_cart:
                    mFragment = new WithlistFragment();
                    break;
                case R.id.navigation_shop:
                    mFragment = new HomeFragment();
                    break;
                case R.id.navigation_video:
                    mFragment = new VideoFragment();
                    break;
                case R.id.navigation_setting:
                    mFragment = new SettingFragment();
                    break;
                default:
                    mFragment = new HomeFragment();

            }
            mFragmentmanager.beginTransaction().replace(R.id.rl_fragment_container, mFragment).commit();
            return true;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_dashboard);

        initializeView();
        mFragmentmanager = getSupportFragmentManager();

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_shop);
    }

    private void initializeView() {
        topRecommedation = findViewById(R.id.rv_top_recommedation);
        mostViewed = findViewById(R.id.rv_most_viewed);
        navigation = findViewById(R.id.bottom_navigation);
    }

    private void moveToLogin() {
        SharedPreferenceManager.setAccessToken(null);
        Intent intent = new Intent(this, SigninActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
