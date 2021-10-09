package com.kylin.core.web.client;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.kylin.core.app.ConfigKeys;
import com.kylin.core.app.Latte;
import com.kylin.core.loader.LatteLoader;
import com.kylin.core.utils.dbtutil.DateUtil;
import com.kylin.core.utils.log.LatteLogger;
import com.kylin.core.utils.storage.LattePreference;
import com.kylin.core.web.IPageLoadListener;
import com.kylin.core.web.WebDelegate;
import com.kylin.core.web.route.Router;


/**
 * Created by ywm
 */

public class WebViewClientImpl extends WebViewClient {

    private final String TAG = "WebViewClientImpl";

    private final WebDelegate DELEGATE;
    private IPageLoadListener mIPageLoadListener = null;
    private static final Handler HANDLER = Latte.getHandler();
    private int count = 0;// 未知名的错误设置了这个int 2018年12月3日18:26:37

    public void setPageLoadListener(IPageLoadListener listener) {
        this.mIPageLoadListener = listener;
    }

    public WebViewClientImpl(WebDelegate delegate) {
        this.DELEGATE = delegate;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        LatteLogger.d("shouldOverrideUrlLoading", url);
        return Router.getInstance().handleWebUrl(DELEGATE, url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        Log.d(TAG,"=====onPageStarted="+ DateUtil.getDateTimeStr(9));
        if (mIPageLoadListener != null) {
            mIPageLoadListener.onLoadStart();
        }
        count = 0;
        // 默认展示的 Loading页面 // false: 点击不可隐藏滚动条  true:点击可隐藏滚动
        LatteLoader.showLoading(view.getContext(),true);
    }

    //获取浏览器cookie
    private void syncCookie() {
        final CookieManager manager = CookieManager.getInstance();
        /*
          注意，这里的Cookie和API请求的Cookie是不一样的，这个在网页不可见
         */
        final String webHost = Latte.getConfiguration(ConfigKeys.WEB_HOST);
        if (webHost != null) {
            if (manager.hasCookies()) {
                final String cookieStr = manager.getCookie(webHost);
                if (cookieStr != null && !cookieStr.equals("")) {
                    LattePreference.addCustomAppProfile("cookie", cookieStr);
                }
            }
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        Log.d(TAG,"=====onPageFinished="+ DateUtil.getDateTimeStr(9));
        syncCookie();
        if (mIPageLoadListener != null) {
            mIPageLoadListener.onLoadEnd();
        }
        HANDLER.postDelayed(new Runnable() {
            @Override
            public void run() {
                LatteLoader.stopLoading();
            }
        }, 1000);
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        //回调失败的相关操作
        if(count < 1){// 回退只执行一次
            DELEGATE.pop();
            count++;
            Toast.makeText(Latte.getApplicationContext(),"网络异常,请检查网络",Toast.LENGTH_SHORT).show();
        }
        // Toast.makeText(Latte.getApplicationContext(),"网络异常,请检查网络",Toast.LENGTH_SHORT).show();
    }


}
