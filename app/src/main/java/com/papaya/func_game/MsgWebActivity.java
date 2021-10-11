package com.papaya.func_game;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;


import com.kylin.core.loader.LatteLoader;
import com.papaya.R;
import com.papaya.base.BaseActivity;
import com.papaya.websetting.JsBridge;
import com.papaya.websetting.JsInterface;

import java.lang.ref.SoftReference;


/**
 * web Activity
 */
public class MsgWebActivity extends BaseActivity implements View.OnClickListener, JsBridge {


    private ImageView img;
    private WebView web;
    MyHandler handler;

    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;

    private String weburl;


    /**
     * 接收子线程消息的 Handler
     */
    public static class MyHandler extends Handler {

        // 软引用
        SoftReference<MsgWebActivity> fragmentRef;

        public MyHandler(MsgWebActivity fragment) {
            fragmentRef = new SoftReference<MsgWebActivity>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            MsgWebActivity fragment = fragmentRef.get();
            if (fragment == null) {
                return;
            }
            Bundle bundle = msg.getData();

            // 处理UI 变化
            switch (msg.what) {
                case 0:
                    fragment.finishSuc();
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
        setContentView(R.layout.activity_msgweb);


        img = (ImageView) findViewById(R.id.plus_img_msgweb);
        web = (WebView) findViewById(R.id.plus_y_msgweb);


        /*// 获取屏幕长宽
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        int width = (int) ((screenHeight / 9) * 16);
        web.setMinimumWidth(width);
        RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) web.getLayoutParams(); //取控件textView当前的布局参数
        linearParams.width = width;// 控件的高强制设成20
        web.setLayoutParams(linearParams); //使设置好的布局参数应用到控件*/


        handler = new MyHandler(this);


        // 获取上一页传递过来的数据
        Intent i = getIntent();
        weburl = i.getStringExtra("weburl");

        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setSupportZoom(true);
        web.getSettings().setBuiltInZoomControls(true);
        web.getSettings().setUseWideViewPort(true);//关键点
        web.getSettings().setLoadWithOverviewMode(true);
        web.getSettings().setDisplayZoomControls(false);//隐藏缩放工具
        web.getSettings().setBlockNetworkImage(false);//解决图片不显示


        //其他细节操作
        web.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE); //不使用缓存，只从网络获取数据.
        web.getSettings().setAllowFileAccess(true); //设置可以访问文件
        web.getSettings().setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        web.getSettings().setLoadsImagesAutomatically(true); //支持自动加载图片
        web.getSettings().setDefaultTextEncodingName("utf-8");//设置编码格式

        web.getSettings().setPluginState(WebSettings.PluginState.ON);
        web.setWebChromeClient(new WebChromeClient());


        // 关键性代码，这里要给webview添加这行代码，才可以点击之后正常播放音频。记录一下。
        web.getSettings().setMediaPlaybackRequiresUserGesture(false);

        // 通过addJavascriptInterface()将Java对象映射到JS对象
        //参数1：Javascript对象名
        //参数2：Java对象名
        web.addJavascriptInterface(new JsInterface(this), "latte");//AndroidtoJS类对象映射到js的test对象

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // WebView.setWebContentsDebuggingEnabled(true);
            web.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }


        web.setWebViewClient(new WebViewClient() {
            /*@Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // view.loadUrl(url);
                web.loadUrl(url);
                return true;
            }*/

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //设定加载开始的操作
                LatteLoader.showLoading(MsgWebActivity.this);

                // img.setVisibility(View.VISIBLE);


            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //设定加载结束的操作
                LatteLoader.stopLoading();


                // img.setVisibility(View.GONE);
            }


            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();    //表示等待证书响应
                // handler.cancel();      //表示挂起连接，为默认方式
                // handler.handleMessage(null);    //可做其他处理
            }
        });

        web.setOnLongClickListener(new View.OnLongClickListener() {

            public boolean onLongClick(View v) {
                WebView.HitTestResult result = web.getHitTestResult();
                if (null == result)
                    return false;
                int type = result.getType();
                switch (type) {
                    case WebView.HitTestResult.EDIT_TEXT_TYPE: // 选中的文字类型
                        break;
                    case WebView.HitTestResult.PHONE_TYPE: // 处理拨号
                        break;
                    case WebView.HitTestResult.EMAIL_TYPE: // 处理Email
                        break;
                    case WebView.HitTestResult.GEO_TYPE: // 　地图类型
                        break;
                    case WebView.HitTestResult.SRC_ANCHOR_TYPE: // 超链接
                        break;
                    case WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE: // 带有链接的图片类型
                    case WebView.HitTestResult.IMAGE_TYPE: // 处理长按图片的菜单项
                        String url = result.getExtra();
                        // downPicDate(url);
                        return true;
                    case WebView.HitTestResult.UNKNOWN_TYPE: //未知
                        break;
                }
                return false;
            }
        });

        // 视频播放
        // web.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        // web.setLayerType(View.LAYER_TYPE_HARDWARE, null);// 注释1 不注释0

        // web.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE);

        // web.loadUrl("http://smp.tsingtao.com.cn/ter_promotion/?t=" + DateUtil.getDateTimeStr(9) + "#/");
        // web.loadUrl("http://wxx.dbtplus.com/ter_promotion/?t=" + DateUtil.getDateTimeStr(9) + "#/");
        // web.loadUrl("https://pic.netbian.com/4kdongman");
        // web.loadUrl("https://pvp.qq.com/");
        // web.loadUrl("https://game.gtimg.cn/images/yxzj/web201706/images/comm/code.png");
        web.loadUrl(weburl);
        // web.loadUrl("http://wxx.dbtplus.com/ter_promotion/static/media/times.mp4");
    }


    @Override
    public void setTextValue(String value) {

        /*handler.post(new Runnable() {
            @Override
            public void run() {
                YdWebActivity.this.finish();
            }
        });*/

        handler.sendEmptyMessage(0);

    }

    @Override
    public void setCallShare(String value) {

        /*handler.post(new Runnable() {
            @Override
            public void run() {
                YdWebActivity.this.finish();
            }
        });*/

        Bundle bundle = new Bundle();
        bundle.putString("formjson", value);
        Message msg = new Message();
        msg.what = 1;
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    /*private void showShareWx(Bundle bundle) {
        String value = bundle.getString("formjson");
        RddcxStc stc = (RddcxStc) JsonUtil.parseJson(value, RddcxStc.class);
        sendToWx(stc);
    }

    private void sendToWx(final RddcxStc stc) {

        Intent intent = new Intent(MsgWebActivity.this, WXEntryActivity.class);
        intent.putExtra("flag", "2");
        intent.putExtra("RddcxStc", stc);
        startActivity(intent);
    }*/

    private void finishSuc() {
        MsgWebActivity.this.finish();
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
    protected void onResume() {
        super.onResume();

        try {
            web.getClass().getMethod("onResume").invoke(web, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            web.getClass().getMethod("onPause").invoke(web, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressedSupport() {


        if (web != null) {
            web.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            web.clearHistory();

            ((ViewGroup) web.getParent()).removeView(web);
            web.destroy();
            web = null;
        }

        super.onBackPressedSupport();


    }


    @Override
    protected void onDestroy() {
        if (web != null) {
            web.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            web.clearHistory();

            ((ViewGroup) web.getParent()).removeView(web);
            web.destroy();
            web = null;
        }
        super.onDestroy();
    }
}
