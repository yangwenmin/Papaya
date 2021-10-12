package com.papaya.func_video;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kylin.core.utils.flyn.Eyes;
import com.papaya.MainService;
import com.papaya.R;
import com.papaya.application.ConstValues;
import com.papaya.base.BaseActivity;
import com.papaya.func_video.domain.VideoStc;
import com.papaya.test.gsydemo.simple.adapter.SimpleListVideoModeAdapter;
import com.papaya.utils.ShanHaiUtil;
import com.papaya.utils.ViewUtil;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.ArrayList;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * 简单列表实现模式1
 */
public class VideoListActivity extends BaseActivity implements View.OnClickListener{

    private RelativeLayout backBtn;
    private RelativeLayout confirmBtn;
    private AppCompatTextView confirmTv;
    private AppCompatTextView backTv;
    private AppCompatTextView titleTv;

    ListView videoList;

    // 上一页传递过来的名称
    private String name;

    // 是否当前页面 第一次发起请求
    private boolean firstSend = true;
    private String fileName;

    VideoListAdapter listNormalAdapter;

    private MainService service;
    private MyHandler handler;

    /**
     * 接收子线程消息的 Handler
     */
    public static class MyHandler extends Handler {

        // 软引用
        SoftReference<VideoListActivity> fragmentRef;

        public MyHandler(VideoListActivity fragment) {
            fragmentRef = new SoftReference<VideoListActivity>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            VideoListActivity fragment = fragmentRef.get();
            if (fragment == null) {
                return;
            }
            Bundle bundle = msg.getData();

            // 处理UI 变化
            switch (msg.what) {
                case ConstValues.WAIT0: // 下载文件的回调
                    fragment.downloadFileSuc(bundle);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_list_video);
        setContentView(R.layout.activity_listview_normal);

        // 标题栏白底黑字
        Eyes.setStatusBarLightMode(this, Color.WHITE);

        initView();
        initData();
    }

    private void initView() {
        backBtn = (RelativeLayout) findViewById(R.id.top_navigation_rl_back);
        confirmBtn = (RelativeLayout) findViewById(R.id.top_navigation_rl_confirm);
        confirmTv = (AppCompatTextView) findViewById(R.id.top_navigation_bt_confirm);
        backTv = (AppCompatTextView) findViewById(R.id.top_navigation_bt_back);
        titleTv = (AppCompatTextView) findViewById(R.id.top_navigation_tv_title);
        backBtn.setVisibility(View.VISIBLE);
        confirmBtn.setVisibility(View.INVISIBLE);
        backBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);

        videoList = (ListView)findViewById(R.id.listview);
    }

    private void initData() {

        // 获取上一页传递过来的数据
        Intent i = getIntent();
        name = i.getStringExtra("name");

        titleTv.setText(name);

        handler = new MyHandler(VideoListActivity.this);
        service = new MainService(VideoListActivity.this, handler);

        // 本地文件名称
        fileName = name + ConstValues.TXTNAME;

        // 读取本地txt
        readTxtFromPhone();



    }


    // 读取本地txt
    private void readTxtFromPhone() {
        // 拼接文件路径
        String pdfpath = ViewUtil.parseFilePath(fileName);

        File docFile = new File(pdfpath);

        if (docFile.exists()) {//存在本地;
            try {
                // 读取文件内容
                String result = ShanHaiUtil.parseFileToString(docFile);
                // 将字符串分割成数组
                String[] read = ShanHaiUtil.splitStringToArray(result);
                // 将字符串数组转成视频对象集合
                final ArrayList<VideoStc> videoStcs = ShanHaiUtil.getVideoListByVideoArray(name, read);

                if (videoStcs != null && videoStcs.size() > 0) {

                    listNormalAdapter = new VideoListAdapter(this, videoStcs);
                    videoList.setAdapter(listNormalAdapter);

                    videoList.setOnScrollListener(new AbsListView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState) {
                        }

                        @Override
                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                            int lastVisibleItem = firstVisibleItem + visibleItemCount;
                            //大于0说明有播放
                            if (GSYVideoManager.instance().getPlayPosition() >= 0) {
                                //当前播放的位置
                                int position = GSYVideoManager.instance().getPlayPosition();
                                //对应的播放列表TAG
                                if (GSYVideoManager.instance().getPlayTag().equals(SimpleListVideoModeAdapter.TAG)
                                        && (position < firstVisibleItem || position > lastVisibleItem)) {
                                    if(GSYVideoManager.isFullState(VideoListActivity.this)) {
                                        return;
                                    }
                                    //如果滑出去了上面和下面就是否，和今日头条一样
                                    GSYVideoManager.releaseAllVideos();
                                    listNormalAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });



                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(VideoListActivity.this, "没有找到指定文件", Toast.LENGTH_SHORT).show();
            }
        } else {// 本地不存在
            // 下载文件
            sendDownloadFile();
        }
    }

    // ----↓ 下载文件 ↓——————————————————————————————————————————------------------------------------------

    // 若是当前页第一次请求 则开始下载文件
    private void sendDownloadFile() {

        // 是否当前页面 第一次发起请求
        if (!firstSend) {
            return;
        }

        if (hasPermission(ConstValues.WRITE_READ_EXTERNAL_PERMISSION)) {
            // 拥有了此权限,那么直接执行业务逻辑
            sendDownRequest();//发起下载文件的请求

        } else {
            // 还没有对一个权限(请求码,权限数组)这两个参数都事先定义好
            requestPermission(ConstValues.WRITE_READ_EXTERNAL_CODE, ConstValues.WRITE_READ_EXTERNAL_PERMISSION);
        }
    }

    // 手动拥有读写权限后执行
    @Override
    public void doWriteSDCard() {
        try {
            // 拥有了此权限,那么直接执行业务逻辑
            sendDownRequest();//发起下载文件的请求

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //发起下载文件的请求
    private void sendDownRequest() {
        // 请求
        service.downloadText(ConstValues.HTTPID + ConstValues.VIDEOPATH + name + "/" + ConstValues.TXTNAME,// 请求url
                fileName,// 本地存储名称
                true, // 是否显示进度条 false不显示下载进度,true显示下载进度框
                ConstValues.WAIT0);// 用于当前Activity界面handle的对应回调接收 比如what1,what2

    }


    // 下载文件的回调
    private void downloadFileSuc(Bundle bundle) {
        // 是否当前页面 第一次发起请求
        firstSend = false;

        String formjson = bundle.getString("formjson");
        String status = bundle.getString("status");

        if (ConstValues.SUCCESS.equals(status)) {
            // 读取本地txt
            readTxtFromPhone();
        } else {
            Toast.makeText(VideoListActivity.this, formjson, Toast.LENGTH_SHORT).show();
        }
    }

    // ----↑ 下载文件 ↑------------------------------------------------------------------------------------

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_navigation_rl_back://
                if (ViewUtil.isDoubleClick(v.getId(), 2500))
                    return;
                this.finish();
                break;
        }
    }


    @Override
    public void onBackPressedSupport() {
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressedSupport();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
    }
}
