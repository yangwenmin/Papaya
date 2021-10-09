package com.kylin.core.net;


import com.kylin.core.app.ConfigKeys;
import com.kylin.core.app.Latte;
import com.kylin.core.net.rx.RxRestService;

import java.util.ArrayList;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by 傅令杰 on 2017/4/2
 */

public final class RestCreator {

    /*// 懒汉式创建参数存放Map
    private static final class ParamsHolder {
        public static final WeakHashMap<String, Object> PARAMS = new WeakHashMap<>();
    }

    // 获取以存放的参数,(没有参数的话,相当于创建了一个WeakHashMap,表示PARAMS不会为null,但可以为空)
    public static WeakHashMap<String, Object> getParams() {
        return ParamsHolder.PARAMS;
    }*/

    /**
     * 构建OkHttp
     */
    private static final class OKHttpHolder {
        private static final int TIME_OUT = 60;
        private static final OkHttpClient.Builder BUILDER = new OkHttpClient.Builder();
        // 先获取配置的拦截器信息
        private static final ArrayList<Interceptor> INTERCEPTORS = Latte.getConfiguration(ConfigKeys.INTERCEPTOR);

        // 再在OkHttpClient.Builder中添加拦截器
        private static OkHttpClient.Builder addInterceptor() {
            if (INTERCEPTORS != null && !INTERCEPTORS.isEmpty()) {
                for (Interceptor interceptor : INTERCEPTORS) {
                    // 在OkHttpClient.Builder中添加拦截器
                    BUILDER.addInterceptor(interceptor);
                }
            }
            return BUILDER;
        }

        // 创建OkHttpClient客户端
        private static final OkHttpClient OK_HTTP_CLIENT = addInterceptor()
                .readTimeout(TIME_OUT,TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(TIME_OUT,TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(TIME_OUT,TimeUnit.SECONDS)//设置连接超时时间
                .connectionPool(new ConnectionPool(5,1,TimeUnit.SECONDS))
                .build();
    }

    /**
     * 构建全局Retrofit客户端
     */
    private static final class RetrofitHolder {
        private static final String BASE_URL = Latte.getConfiguration(ConfigKeys.API_HOST);
        private static final Retrofit RETROFIT_CLIENT = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OKHttpHolder.OK_HTTP_CLIENT)
                .addConverterFactory(ScalarsConverterFactory.create())
                // .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    /**
     * Service接口
     */
    private static final class RestServiceHolder {
        // 假设1+1=2
        private static final RestService REST_SERVICE =
                RetrofitHolder.RETROFIT_CLIENT.create(RestService.class);
    }

    public static RestService getRestService() {
        return RestServiceHolder.REST_SERVICE;
    }

    /**
     * Service接口
     */
    private static final class RxRestServiceHolder {
        private static final RxRestService REST_SERVICE =
                RetrofitHolder.RETROFIT_CLIENT.create(RxRestService.class);
    }

    public static RxRestService getRxRestService() {
        return RxRestServiceHolder.REST_SERVICE;
    }
}
