package com.kylin.core.net.interceptors;


import java.io.IOException;

import androidx.annotation.NonNull;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 请求拦截器,重写intercept()方法,返回自定义json
 *
 * Created by yangwenmin on 2017/10/17.
 */

public class AddCookiesInterceptor extends BaseInterceptor {

    private final String DEBUG_URL;// 模拟url,
    private final int DEBUG_RAW_ID;// 模拟返回的json数据,放在raw文件夹下

    // 构造
    public AddCookiesInterceptor(String debugUrl, int rawId) {
        this.DEBUG_URL = debugUrl;
        this.DEBUG_RAW_ID = rawId;
    }

    // 重写请求拦截
    @Override
    public Response intercept(@NonNull Interceptor.Chain chain) throws IOException {


        final Request.Builder builder = chain.request().newBuilder();
        // 设置Cookie  测试通过,然后注释掉 用的时候再说
        // SharedPreferences sharedPreferences = Latte.getApplicationContext().getSharedPreferences("cookie", Context.MODE_PRIVATE);
        // builder.addHeader("Cookie", "123456");
        return chain.proceed(builder.build());
    }
}
