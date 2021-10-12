package com.papaya.func_video;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kylin.core.utils.dbtutil.FileUtil;
import com.kylin.core.utils.dbtutil.PrefUtils;
import com.kylin.core.utils.flyn.Eyes;
import com.papaya.MainService;
import com.papaya.R;
import com.papaya.application.ConstValues;
import com.papaya.base.BaseActivity;
import com.papaya.func_video.domain.CategoryStc;
import com.papaya.func_video.haoping.VideoHaopingActivity;
import com.papaya.func_video.haoping.VideoRandomActivity;
import com.papaya.utils.ShanHaiUtil;
import com.papaya.utils.ViewUtil;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Random;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * 视频分类
 */
public class VideoFenleiActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout backBtn;
    private RelativeLayout confirmBtn;
    private AppCompatTextView confirmTv;
    private AppCompatTextView backTv;
    private AppCompatTextView titleTv;

    private GridView platform_gv;

    private MainService service;
    private MyHandler handler;

    // 是否下载所有分类目录文件 默认:true  true:需要下载  false:不需要下载
    private boolean downAllFile;


    private String title;

    private Random rand;

    /**
     * 接收子线程消息的 Handler
     */
    public static class MyHandler extends Handler {

        // 软引用
        SoftReference<VideoFenleiActivity> fragmentRef;

        public MyHandler(VideoFenleiActivity fragment) {
            fragmentRef = new SoftReference<VideoFenleiActivity>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            VideoFenleiActivity fragment = fragmentRef.get();
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
        // 全屏
        // this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_category_layout);

        // 标题栏白底黑字
        Eyes.setStatusBarLightMode(this, Color.WHITE);

        // 初始化视图
        initView();
        // 初始化数据
        initData();
    }

    // 初始化视图
    private void initView() {

        backBtn = (RelativeLayout) findViewById(R.id.top_navigation_rl_back);
        confirmBtn = (RelativeLayout) findViewById(R.id.top_navigation_rl_confirm);
        confirmTv = (AppCompatTextView) findViewById(R.id.top_navigation_bt_confirm);
        backTv = (AppCompatTextView) findViewById(R.id.top_navigation_bt_back);
        titleTv = (AppCompatTextView) findViewById(R.id.top_navigation_tv_title);
        backBtn.setVisibility(View.INVISIBLE);
        confirmBtn.setVisibility(View.VISIBLE);
        backBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);

        platform_gv = (GridView) findViewById(R.id.category_personmanage_gv);
    }

    // 初始化数据
    private void initData() {

        handler = new MyHandler(VideoFenleiActivity.this);
        service = new MainService(VideoFenleiActivity.this, handler);

        // 获取上一页传递过来的数据
        Intent i = getIntent();
        title = i.getStringExtra("title");

        titleTv.setText(title);
        confirmTv.setText("刷新");
        confirmTv.setTextSize(14);

        rand = new Random();

        if (hasPermission(ConstValues.WRITE_READ_EXTERNAL_PERMISSION)) {
            // 拥有了此权限,那么直接执行业务逻辑

            // 读取本地txt
            readTxtFromPhone();

        } else {
            // 还没有对一个权限(请求码,权限数组)这两个参数都事先定义好
            requestPermission(ConstValues.WRITE_READ_EXTERNAL_CODE, ConstValues.WRITE_READ_EXTERNAL_PERMISSION);
        }
    }

    // 读取本地txt
    private void readTxtFromPhone() {

        // 拼接文件路径
        String pdfpath = ViewUtil.parseFilePath(ConstValues.TXTNAME);
        File docFile = new File(pdfpath);
        if (docFile.exists()) {//存在本地;
            try {

                // 传入File文件 读取文件内容
                String result = ShanHaiUtil.parseFileToString(docFile);
                // 传入字符串 将字符串分割成数组
                String[] read = ShanHaiUtil.splitStringToArray(result);
                // 将数组 转成 集合
                final ArrayList<CategoryStc> categoryStcs = ShanHaiUtil.parseStringArrayToList(read);

                if (categoryStcs != null && categoryStcs.size() > 0) {

                    // 是否下载所有分类目录文件 默认:true  true:需要下载  false:不需要下载
                    downAllFile = PrefUtils.getBoolean(VideoFenleiActivity.this, ConstValues.DOWNALLFILE, true);
                    if (downAllFile) {
                        for (CategoryStc categoryStc : categoryStcs) {
                            // 存储本地文件名称
                            String fileName = categoryStc.getCategoryname() + ConstValues.TXTNAME;//

                            // 请求
                            service.downloadText(ConstValues.HTTPID + ConstValues.VIDEOPATH + categoryStc.getCategoryname() + "/" + ConstValues.TXTNAME,// 请求url
                                    fileName, // 本地存储名称
                                    false, // 是否显示进度条 false不显示下载进度,true显示下载进度框
                                    ConstValues.WAIT1);// 用于当前Activity界面handle的对应回调接收 比如what1,what2
                        }
                        // 设置不需要下载所有分类目录文件  true:需要下载  false:不需要下载
                        PrefUtils.putBoolean(VideoFenleiActivity.this, ConstValues.DOWNALLFILE, false);
                    }

                    categoryStcs.add(0, new CategoryStc("大众好评", ConstValues.videoUrlList[rand.nextInt(ConstValues.videoUrlList.length)]));
                    categoryStcs.add(0, new CategoryStc("为您推荐", ConstValues.videoUrlList[rand.nextInt(ConstValues.videoUrlList.length)]));


                    VideoFenleiAdapter jieMianAdapter = new VideoFenleiAdapter(VideoFenleiActivity.this, categoryStcs);
                    platform_gv.setAdapter(jieMianAdapter);
                    // 设置item的点击监听
                    platform_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            CategoryStc stc = categoryStcs.get(position);
                            String name = stc.getCategoryname();

                            if ("为您推荐".equals(name)) {
                                Intent intent = new Intent(VideoFenleiActivity.this, VideoRandomActivity.class);
                                intent.putExtra("name", name);
                                startActivity(intent);
                            } else if ("大众好评".equals(name)) {
                                Intent intent = new Intent(VideoFenleiActivity.this, VideoHaopingActivity.class);
                                intent.putExtra("name", name);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(VideoFenleiActivity.this, VideoListActivity.class);
                                intent.putExtra("name", name);
                                startActivity(intent);
                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(VideoFenleiActivity.this, "没有找到指定文件", Toast.LENGTH_SHORT).show();
            }
        } else {// 本地不存在
            // 下载文件
            downloadFile();
        }
    }


    // ----↓ 下载文件 ↓——————————————————————————————————————————------------------------------------------

    // 若是当前页第一次请求 则开始下载文件
    private void downloadFile() {
        //发起下载文件的请求
        service.downloadText(ConstValues.HTTPID + ConstValues.VIDEOPATH + ConstValues.TXTNAME, // 请求url
                ConstValues.TXTNAME,// 保存本地文件名称 name_LIST.TXT
                true, // 是否显示进度条 false不显示下载进度,true显示下载进度框
                ConstValues.WAIT0);// 用于当前Activity界面handle的对应回调接收 比如what1,what2

    }

    // 手动拥有读写权限后 执行下载文件
    @Override
    public void doWriteSDCard() {
        try {
            // 拥有了此权限,那么直接执行业务逻辑
            // 读取本地txt
            readTxtFromPhone();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    // 下载文件的回调
    private void downloadFileSuc(Bundle bundle) {

        String formjson = bundle.getString("formjson");
        String status = bundle.getString("status");

        if (ConstValues.SUCCESS.equals(status)) {
            // 读取本地txt
            readTxtFromPhone();
        } else {
            Toast.makeText(VideoFenleiActivity.this, formjson, Toast.LENGTH_SHORT).show();
        }
    }

    // ----↑ 下载文件 ↑------------------------------------------------------------------------------------


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_navigation_rl_confirm:// 刷新
                if (ViewUtil.isDoubleClick(v.getId(), 2500))
                    return;
                // delete文件
                File docFile = new File(Environment.getExternalStorageDirectory().getPath() + "/" + ConstValues.LOCALPATH);
                FileUtil.deleteFile(docFile);

                // 设置不需要下载所有分类目录文件  true:需要下载  false:不需要下载
                PrefUtils.putBoolean(VideoFenleiActivity.this, ConstValues.DOWNALLFILE, true);

                //发起下载文件的请求
                downloadFile();
                break;
        }
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置横向(和安卓4.x动画相同)
        return new DefaultHorizontalAnimator();
        // return new DefaultNoAnimator();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }
}
