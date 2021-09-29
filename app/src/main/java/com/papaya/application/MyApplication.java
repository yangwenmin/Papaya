package com.papaya.application;

import android.content.Context;



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