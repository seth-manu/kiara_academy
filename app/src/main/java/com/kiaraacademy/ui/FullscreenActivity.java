package com.kiaraacademy.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.ct7ct7ct7.androidvimeoplayer.model.PlayerState;
import com.ct7ct7ct7.androidvimeoplayer.view.VimeoPlayerActivity;
import com.ct7ct7ct7.androidvimeoplayer.view.VimeoPlayerView;
import com.kiaraacademy.R;

public class FullscreenActivity extends Activity {

    int REQUEST_CODE = 1234;
    private VimeoPlayerView vimeoPlayer;
    private String videoUrl;
    private String videoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_fullscreen);

        initializeView();
        getIntentData();
        setupView(videoUrl);

        String requestOrientation = VimeoPlayerActivity.REQUEST_ORIENTATION_LANDSCAPE;
        startActivityForResult(VimeoPlayerActivity.createIntent(FullscreenActivity.this, requestOrientation, vimeoPlayer), REQUEST_CODE);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        videoUrl = intent.getStringExtra("video_url");
        videoName = intent.getStringExtra("video_name");
    }

    private void initializeView() {
        vimeoPlayer = findViewById(R.id.vimeoPlayer);
    }

    private void setupView(String url) {
        vimeoPlayer.initialize(Integer.parseInt(url.split("https://vimeo.com/")[1]), url);
        vimeoPlayer.setFullscreenVisibility(true);

        vimeoPlayer.setFullscreenClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String requestOrientation = VimeoPlayerActivity.REQUEST_ORIENTATION_AUTO;
                startActivityForResult(VimeoPlayerActivity.createIntent(FullscreenActivity.this, requestOrientation, vimeoPlayer), REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && resultCode == REQUEST_CODE) {
            if (data != null) {
                float platAt = data.getFloatExtra(VimeoPlayerActivity.RESULT_STATE_PLAYER_STATE, 0f);
                vimeoPlayer.seekTo(platAt);

                PlayerState playerState = PlayerState.valueOf(data.getStringExtra(VimeoPlayerActivity.RESULT_STATE_PLAYER_STATE));
                switch (playerState) {
                    case PLAYING:
                        vimeoPlayer.play();
                        break;
                    case PAUSED:
                        vimeoPlayer.pause();
                        break;
                    default:
                        vimeoPlayer.play();
                }
            }
        }
    }
}
