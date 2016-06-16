package com.example.tomhansson.flexilast;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by Daniel on 2016-05-26.
 */
public class FallTradActivity extends AppCompatActivity {
    private VideoView mVideoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_falltrad);

        initiateVideoView();
    }





    /* Initiates a videoView. */

    private void initiateVideoView()
    {
        mVideoView = (VideoView) findViewById(R.id.videoFallTrad);

        mVideoView.setVideoPath(
                "android.resource://" + getPackageName() + "/" + R.raw.inst);

        mVideoView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (mVideoView.isPlaying()) {
                    mVideoView.pause();
                    return false;
                } else {
                    mVideoView.start();
                    return false;
                }
            }
        });

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        MediaController mediaController = new MediaController(FallTradActivity.this);
                        mVideoView.setMediaController(mediaController);
                        mediaController.setAnchorView(mVideoView);
                    }
                });
            }
        });
    }


    /* When the back button is pressed the application goes to GuideActivity. */

    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, GuideActivity.class);
        startActivity(i);
        this.finish();
    }
}
