package com.kiaraacademy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.ct7ct7ct7.androidvimeoplayer.view.VimeoPlayerView;
import com.kiaraacademy.R;
import com.kiaraacademy.data.response.ResponseMainCourse;

public class CourseDetailActivity extends AppCompatActivity {
    private VimeoPlayerView vimeoPlayer;
    private ImageButton ib_play;
    private LinearLayoutCompat ll_course_name;
    private TextView tv_course_name;
    private LinearLayoutCompat ll_course_type;
    private TextView tv_course_type;
    private LinearLayoutCompat ll_class;
    private TextView tv_class;
    private LinearLayoutCompat ll_board;
    private TextView tv_board;
    private LinearLayoutCompat ll_course_desc;
    private TextView tv_course_desc;
    private ResponseMainCourse course = null;
    private AppCompatImageButton ibBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_course_detail);

        getIntentData();
        initiateViews();
        initiateListener();
        bindCourseData();
        setupView(course.getCourse_url_video());

    }

    private void initiateListener() {
        ibBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setupView(String course_url_video) {
        vimeoPlayer.initialize(Integer.parseInt(course_url_video.split("https://vimeo.com/")[1]), course_url_video);
        vimeoPlayer.setFullscreenVisibility(true);

        vimeoPlayer.setFullscreenClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseDetailActivity.this, FullscreenActivity.class);
                intent.putExtra("video_url", course.getCourse_url_video());
                startActivity(intent);
            }
        });
    }

    private void bindCourseData() {
        if (course != null) {
            if (course.getCourse_name() == null || course.getCourse_name().equalsIgnoreCase("")) {
                ll_course_name.setVisibility(View.GONE);
            }
            if (course.getCourse_type() == null || course.getCourse_type().equalsIgnoreCase("")) {
                ll_course_type.setVisibility(View.GONE);
            }
            if (course.getClasses() == null || course.getClasses().equalsIgnoreCase("")) {
                ll_class.setVisibility(View.GONE);
            }
            if (course.getBoard() == null || course.getBoard().equalsIgnoreCase("")) {
                ll_board.setVisibility(View.GONE);
            }
            if (course.getCourse_desc() == null || course.getCourse_desc().equalsIgnoreCase("")) {
                ll_course_desc.setVisibility(View.GONE);
            }

            tv_course_name.setText(course.getCourse_name());
            tv_course_type.setText(course.getCourse_type());
            tv_class.setText(course.getClasses());
            tv_board.setText(course.getBoard());
            tv_course_desc.setText(course.getCourse_desc());
        }
    }

    private void getIntentData() {
        Intent intent = getIntent();
        course = (ResponseMainCourse) intent.getSerializableExtra("course");
    }

    private void initiateViews() {
        vimeoPlayer = findViewById(R.id.vimeoPlayer);
        ll_course_name = findViewById(R.id.ll_course_name);
        tv_course_name = findViewById(R.id.tv_course_name);
        ll_course_type = findViewById(R.id.ll_course_type);
        tv_course_type = findViewById(R.id.tv_course_type);
        ll_class = findViewById(R.id.ll_class);
        tv_class = findViewById(R.id.tv_class);
        ll_board = findViewById(R.id.ll_board);
        tv_board = findViewById(R.id.tv_board);
        ll_course_desc = findViewById(R.id.ll_course_desc);
        tv_course_desc = findViewById(R.id.tv_course_desc);
        ibBackButton = findViewById(R.id.ib_back_button);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
