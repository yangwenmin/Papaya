package com.papaya.websetting;

import android.webkit.JavascriptInterface;

/**
 * web Activity
 */
public class JsInterface  {

    private JsBridge bridge;

    public JsInterface(JsBridge bridge) {
        this.bridge = bridge;
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public void callBack(String msg) {
        bridge.setTextValue(msg);
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public void callWx(String msg) {
        bridge.setCallShare(msg);
    }
}
