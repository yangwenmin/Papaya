package com.kylin.core.web;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.kylin.core.initbase.InitFragment;
import com.kylin.core.web.chromeclient.WebChromeClientImpl;
import com.kylin.core.web.client.WebViewClientImpl;
import com.kylin.core.web.route.RouteKeys;
import com.kylin.core.web.route.Router;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by ywm
 */

public class WebDelegateImpl extends WebDelegate {

    private  IPageLoadListener mIPageLoadListener = null;

    public static WebDelegateImpl create(String url) {
        final Bundle args = new Bundle();
        args.putString(RouteKeys.URL.name(), url);
        final WebDelegateImpl delegate = new WebDelegateImpl();
        delegate.setArguments(args);
        return delegate;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        onBindView(savedInstanceState,getWebView());
        return getWebView();
    }

    public void setPageLoadListener(IPageLoadListener listener) {
        this.mIPageLoadListener = listener;
    }

    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        if (getUrl() != null) {
            //用原生的方式模拟Web跳转并进行页面加载
            Router.getInstance().loadPage(this, getUrl());
        }
    }

    @Override
    public IWebViewInitializer setInitializer() {
        return this;
    }

    @Override
    public WebView initWebView(WebView webView) {
        return new WebViewInitializer().createWebView(webView);
    }

    @Override
    public WebViewClient initWebViewClient() {
        final WebViewClientImpl client = new WebViewClientImpl(this);
        client.setPageLoadListener(mIPageLoadListener);
        return client;
    }

    @Override
    public WebChromeClient initWebChromeClient(InitFragment fragment) {
        return new WebChromeClientImpl(fragment);
    }



    /**
     * 返回文件处理
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent intent) {
        //图库选择上传
        if (requestCode == WebChromeClientImpl.FILECHOOSER_RESULTCODE
                &&intent!=null
                 &&resultCode == RESULT_OK
                ) {
            Uri[] uris = new Uri[1];
            uris[0] = intent.getData();
            WebChromeClientImpl.update(uris);
        }
        //拍照上传
        else if(requestCode == WebChromeClientImpl.FILECHOOSER_RESULTCODE
                // &&intent==null
                // &&resultCode == RESULT_OK
                ){
            Uri[] uris = new Uri[1];
            uris[0] =WebChromeClientImpl.myImageUri;
            WebChromeClientImpl.update(uris);
        }
    }
}
