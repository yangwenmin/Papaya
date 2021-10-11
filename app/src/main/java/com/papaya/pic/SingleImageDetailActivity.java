package com.papaya.pic;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.papaya.R;
import com.papaya.base.BaseActivity;

import java.lang.ref.SoftReference;


/**
 * Activity
 */
public class SingleImageDetailActivity extends BaseActivity implements View.OnClickListener {


    private ImageView img;
    MyHandler handler;

    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;

    private String imgurl;


    /**
     * 接收子线程消息的 Handler
     */
    public static class MyHandler extends Handler {

        // 软引用
        SoftReference<SingleImageDetailActivity> fragmentRef;

        public MyHandler(SingleImageDetailActivity fragment) {
            fragmentRef = new SoftReference<SingleImageDetailActivity>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            SingleImageDetailActivity fragment = fragmentRef.get();
            if (fragment == null) {
                return;
            }
            Bundle bundle = msg.getData();

            // 处理UI 变化
            switch (msg.what) {
                case 0:
                    // fragment.finishSuc();
                    break;
                case 1:
                    // fragment.showShareWx(bundle);
                    break;
                case 2:
                    // fragment.showTzAdapter();
                    break;
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_imagedetail);

        initView();
        initData();

    }

    private void initView() {
        img = (ImageView) findViewById(R.id.imagedetail_img_show);
    }

    private void initData() {
        handler = new MyHandler(this);

        // 获取上一页传递过来的数据
        Intent i = getIntent();
        imgurl = i.getStringExtra("imgurl");

        Glide.with(SingleImageDetailActivity.this)
                .load(imgurl)
                .thumbnail(Glide.with(SingleImageDetailActivity.this).load(R.drawable.gif_loading))// 缩略图
                .centerCrop()// 按比例放大/缩小,铺满整个ImageView控件
                .into(img);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.button://
                Intent intent = new Intent(MsgWebActivity.this, PromoWebActivity.class);
                // intent.putExtra("suffix", promotionTxtStc.getFiletype());
                startActivity(intent);
                break;*/
        }
    }

    @Override
    public void onBackPressedSupport() {
        super.onBackPressedSupport();
    }
}
