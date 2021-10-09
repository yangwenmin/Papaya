package com.kylin.core.web.chromeclient;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;


import com.kylin.core.app.Latte;
import com.kylin.core.initbase.InitFragment;
import com.kylin.core.initbase.InitValues;
import com.kylin.core.utils.dbtutil.DateUtil;

import java.io.File;
import java.util.Date;

import androidx.core.content.FileProvider;

/**
 * Created by ywm
 */

public class WebChromeClientImpl extends WebChromeClient {

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        return super.onJsAlert(view, url, message, result);
    }

    // 请求地理位置 2018年11月26日19:53:32
    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
        super.onReceivedIcon(view, icon);
    }

    // 请求地理位置 2018年11月26日19:53:32
    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        callback.invoke(origin, true, false);
        super.onGeolocationPermissionsShowPrompt(origin, callback);
    }




    public static ValueCallback<Uri> uriValueCallback;
    public static ValueCallback<Uri[]> valueCallbacks;
    private InitFragment activity;

    public static final int FILECHOOSER_RESULTCODE = 1314;

    public static Uri myImageUri=null;



    @SuppressWarnings("deprecation")
    public WebChromeClientImpl(InitFragment fragment ) {
        this.activity = fragment;
    }

    //5.0+
    @Override
    public boolean onShowFileChooser(WebView webView,
                                     ValueCallback<Uri[]> filePathCallback,
                                     FileChooserParams fileChooserParams) {
        // TODO 自动生成的方法存根
        valueCallbacks = filePathCallback;
        this.activity.startActivityForResult(createCameraIntent(), this.FILECHOOSER_RESULTCODE);
        return true;
    }

    //4.0+
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        uriValueCallback = uploadMsg;
        this.activity.startActivityForResult(createCameraIntent(), this.FILECHOOSER_RESULTCODE);
    }

    // 3.0 +
    @SuppressWarnings("static-access")
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {

        uriValueCallback = uploadMsg;
        this.activity.startActivityForResult(createCameraIntent(),this.FILECHOOSER_RESULTCODE);
    }


    // Android < 3.0
    @SuppressWarnings("static-access")
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        uriValueCallback = uploadMsg;
        this.activity.startActivityForResult(createCameraIntent(),  this.FILECHOOSER_RESULTCODE);
    }

    /**
     * 跳转选择界面
     * @return
     */
    private Intent createDefaultOpenableIntent() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        return i;

    }

    private Intent createChooserIntent(Intent... intents) {
        Intent chooser = new Intent(Intent.ACTION_CHOOSER);
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents);
        chooser.putExtra(Intent.EXTRA_TITLE, "选择图片");
        return chooser;
    }

    /**
     * 调用系统相机拍照
     * @return
     */
    @SuppressWarnings("static-access")
    private Intent createCameraIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        this.myImageUri=getFileUri();
        cameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,myImageUri);
        return cameraIntent;

    }

    //Uri获取 支持Android7.0
    private Uri getFileUri(){
        Uri imageUri = null;
        String path=getFilePath();
        File file = new File(path);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//Android版本>=7.0
            try {
                // imageUri = FileProvider.getUriForFile(this.activity,"cxy.tsingtaopad.fileProvider", file);
                // imageUri = FileProvider.getUriForFile(Latte.getApplicationContext(),"cxy.tsingtaopad.fileProvider", file);
                imageUri = FileProvider.getUriForFile(Latte.getApplicationContext(),InitValues.PROVIDER, file);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            imageUri = Uri.fromFile(file);
        }
        return imageUri;
    }

    private String getFilePath(){//图片 存储路径
        File externalDataDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File cameraDataDir = new File(externalDataDir.getAbsolutePath() + File.separator + "UPUPUP");
        cameraDataDir.mkdirs();//路径:DCIM/UPUPUP
        // String mCameraFilePath = cameraDataDir.getAbsolutePath() + File.separator + "send_new_image.jpg";
        String mCameraFilePath = cameraDataDir.getAbsolutePath() + File.separator + DateUtil.formatDate(new Date(), null) + ".jpg";
        return mCameraFilePath;
    }

    public static void update(Uri[] uris) {//上传

        if ( valueCallbacks != null
                && uris[0] != null) {
            valueCallbacks.onReceiveValue(uris);
            valueCallbacks = null;
        }

        if(uriValueCallback != null
                && uris[0] != null){
            uriValueCallback.onReceiveValue(uris[0]);
            uriValueCallback = null;
        }
    }

}
