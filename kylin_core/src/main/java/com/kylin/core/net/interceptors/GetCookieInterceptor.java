package com.kylin.core.net.interceptors;

import android.content.Context;
import android.content.SharedPreferences;

import com.kylin.core.app.Latte;

import java.io.IOException;

import androidx.annotation.NonNull;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 请求拦截器,重写intercept()方法,返回自定义json
 *
 * Created by yangwenmin on 2017/10/17.
 */

public class GetCookieInterceptor extends BaseInterceptor {

    private final String DEBUG_URL;// 模拟url,
    private final int DEBUG_RAW_ID;// 模拟返回的json数据,放在raw文件夹下

    // 构造
    public GetCookieInterceptor(String debugUrl, int rawId) {
        this.DEBUG_URL = debugUrl;
        this.DEBUG_RAW_ID = rawId;
    }



    // 重写请求拦截
    @Override
    public Response intercept(@NonNull Interceptor.Chain chain) throws IOException {

        Response originalResponse = chain.proceed(chain.request());

        //这里获取请求返回的cookie
        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            final StringBuffer cookieBuffer = new StringBuffer();
            /*//最近在学习RxJava,这里用了RxJava的相关API大家可以忽略,用自己逻辑实现即可.大家可以用别的方法保存cookie数据
            Observable.from(originalResponse.headers("Set-Cookie"))
                    .map(new Func1<String, String>() {
                        @Override
                        public String call(String s) {
                            String[] cookieArray = s.split(";");
                            return cookieArray[0];
                        }
                    })
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String cookie) {
                            cookieBuffer.append(cookie).append(";");
                        }
                    });

            //解析Cookie
            for (String header : originalResponse.headers("Set-Cookie")) {
                cookieBuffer.append(cookie);
                if(header.contains("JSESSIONID")){
                    NetClient.COOKIE = header.substring(header.indexOf("JSESSIONID"), header.indexOf(";"));

                }
            }*/

            SharedPreferences sharedPreferences = Latte.getApplicationContext().getSharedPreferences("cookie", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("cookie", cookieBuffer.toString());
            editor.commit();
        }

        return originalResponse;
    }
}
