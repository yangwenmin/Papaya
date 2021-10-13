package com.papaya.func_video.player;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kylin.core.videoplayer.SpeedGSYVideoPlayer;
import com.papaya.R;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import androidx.appcompat.app.AppCompatActivity;
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager;

public class PlayerActivity extends AppCompatActivity {


    SpeedGSYVideoPlayer videoPlayer;

    OrientationUtils orientationUtils;

    private String videoname;
    private String videourl;
    private String imageurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 全屏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gsydemo_player);

        init();
    }


    private void init() {

        videoPlayer = (SpeedGSYVideoPlayer) findViewById(R.id.player_player);


        // 获取上一页传递过来的数据
        Intent i = getIntent();
        videoname = i.getStringExtra("videoname");
        videourl = i.getStringExtra("videourl");
        imageurl = i.getStringExtra("imageurl");

        // 播放本地部分视频非常慢 https://github.com/CarGuo/GSYVideoPlayer/issues/3358
        PlayerFactory.setPlayManager(Exo2PlayerManager.class);
        // PlayerFactory.setPlayManager(new IjkPlayerManager());//ijk模式

        videoPlayer.setUp(videourl, true, videoname);

        //增加封面
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Glide.with(PlayerActivity.this)
                .load(imageurl)
                .into(imageView);

        // imageView.setImageResource(R.mipmap.bg_splash_open);
        videoPlayer.setThumbImageView(imageView);
        //增加title
        videoPlayer.getTitleTextView().setVisibility(View.VISIBLE);
        //设置返回键
        videoPlayer.getBackButton().setVisibility(View.GONE);
        //设置旋转
        orientationUtils = new OrientationUtils(this, videoPlayer);

        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 直接横屏

                // 不需要屏幕旋转
                // videoPlayer.setNeedOrientationUtils(false);

                orientationUtils.resolveByClick();

                // 设置全屏 第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                videoPlayer.startWindowFullscreen(PlayerActivity.this, true, true);
            }
        });

        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(true);


        //设置返回按键功能
        videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // 播放速度 默认正常1.0f
        // videoPlayer.setSpeedPlaying(1.3f, true);

        // 循环播放
        videoPlayer.setLooping(true);

        // 开始播放
        videoPlayer.startPlayLogic();
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoPlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoPlayer.onVideoResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }

    @Override
    public void onBackPressed() {
        /*// 需要回归竖屏
        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            videoPlayer.getFullscreenButton().performClick();
            return;
        }*/



        // 需要回归竖屏
        if (GSYVideoManager.backFromWindowFull(this)) {
            // videoPlayer.getFullscreenButton().performClick();
            videoPlayer.onBackFullscreen();
            // GSYVideoManager.backFromWindowFull(this);
            return;
        }

        //释放所有
        videoPlayer.setVideoAllCallBack(null);
        super.onBackPressed();
    }
}
