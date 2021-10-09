package com.papaya.application;

import android.content.Context;


import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.kylin.core.app.Latte;
import com.kylin.core.icon.FontEcModule;
import com.kylin.core.net.interceptors.DebugInterceptor;
import com.papaya.R;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;


/**
 * Created by ywm on 2018/8/17.
 */


public class MyApplication extends MultiDexApplication {


    public static final String TAG = "MyApplication";
    private static Context context;
    private static MyApplication sApplication;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }




    @Override
    public void onCreate() {
        super.onCreate();


        MyApplication.context = getApplicationContext();
        sApplication = this;


        // 通过全局配置器,配置参数
        Latte.init(this)// 配置ApplicationContext,全局handler
                .withIcon(new FontAwesomeModule())// 配置字体图标
                .withIcon(new FontEcModule())// 配置另一种字体图标
                .withApiHost("https://www.baidu.com/")// 配置ApiHost  必须以/结尾
                .withInterceptor(new DebugInterceptor("test", R.raw.test))// 拦截url请求中包含test的url请求
                // .withInterceptor(new AddCookiesInterceptor("test", R.raw.test))// 拦截url请求中包含test的url请求
                .withJavascriptInterface("latte")
                // .withWebEvent("back", new BackEvent())// 点击H5页面上的返回按钮  直接返回  业代运营管理堆头协议店申请
                // .withWebEvent("share", new ShareEvent()) // 点击H5上分享按钮  分享到微信
                // .withWebEvent("confirmback", new ConfirmBackEvent())//点击H5页面上的确定按钮 销毁h5页面 督导日工作记录详情  业代打招呼查看堆头协议
                // .withWebEvent("getAppLocation", new LocationEvent())// 点击H5页面上的按钮,获取app地址,传给H5接收
                // .withWebEvent("zhejiangback", new ZhejiangBackEvent())// 点击H5页面上的提交按钮     浙江需求 需要删除本地文件
                //添加Cookie同步拦截器
                .withWebHost("https://www.baidu.com/")
                .configure();// 修改→配置完成的标记true


        // 崩溃收集
        CrashHandler.getInstance().init(this);


        // Android P（Android 9）出现Detected problems with API compatibility问题解决
        closeAndroidPDialog();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }




    public static MyApplication getApplication() {
        return sApplication;
    }


    private void closeAndroidPDialog(){
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}