package com.papaya.low;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;


import com.kylin.core.utils.flyn.Eyes;
import com.papaya.R;
import com.papaya.application.ConstValues;
import com.papaya.base.BaseActivity;

import java.lang.ref.SoftReference;

import androidx.appcompat.widget.AppCompatTextView;


public class LowActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout backBtn;
    private RelativeLayout confirmBtn;
    private AppCompatTextView confirmTv;
    private AppCompatTextView backTv;
    private AppCompatTextView titleTv;

    private String username;
    private String password;

    MyHandler handler;

    /**
     * 接收子线程消息的 Handler
     */
    public  class MyHandler extends Handler {

        // 软引用
        SoftReference<LowActivity> fragmentRef;

        public MyHandler(LowActivity fragment) {
            fragmentRef = new SoftReference<LowActivity>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            LowActivity fragment = fragmentRef.get();
            if (fragment == null) {
                return;
            }

            Bundle bundle = msg.getData();
            super.handleMessage(msg);
            // 处理UI 变化
            switch (msg.what) {
                // 提示信息
                case ConstValues.WAIT0:
                    //
                    // fragment.getPicSuc(bundle);
                    break;
                case ConstValues.WAIT1:
                    // 关闭缓冲界面
                    // LatteLoader.stopLoading();
                    break;

                default:
                    break;
            }
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.low_activity);

        // 标题栏白底黑字
        Eyes.setStatusBarLightMode(this, Color.WHITE);

        // 接收传递过来的参数
        initCreate();
        // 初始化视图
        initView();
        // 初始化数据
        initData();
    }

    private void initCreate(){

        /*// 上一页面设置数据
        Intent intent = null;
        intent = new Intent(this, RePwdActivity.class);
        Bundle bundl = new Bundle();
        intent.putExtra("username", "");
        intent.putExtra("password", "");
        startActivity(intent);*/

        // 本页面获取数据
        Intent i =getIntent();
        username = (String)i.getSerializableExtra("username");
        password = (String)i.getSerializableExtra("password");
    }

    private void initView() {

        backBtn = (RelativeLayout) findViewById(R.id.top_navigation_rl_back);
        confirmBtn = (RelativeLayout) findViewById(R.id.top_navigation_rl_confirm);
        confirmTv = (AppCompatTextView) findViewById(R.id.top_navigation_bt_confirm);
        backTv = (AppCompatTextView) findViewById(R.id.top_navigation_bt_back);
        titleTv = (AppCompatTextView) findViewById(R.id.top_navigation_tv_title);
        confirmBtn.setVisibility(View.INVISIBLE);
        //confirmBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);


    }

    private void initData() {
        handler = new MyHandler(this);
        titleTv.setText("主界面");

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 返回
            case R.id.top_navigation_rl_back:
                this.finish();
                break;
            case R.id.top_navigation_rl_confirm://
                break;
        }
    }

    @Override
    public void onBackPressedSupport() {
        // 对于 4个类别的主Fragment内的回退back逻辑,已经在其onBackPressedSupport里各自处理了
        super.onBackPressedSupport();
        this.finish();
    }
}
