package com.kylin.core.app;

import android.app.Activity;
import android.os.Handler;

import com.joanzapata.iconify.IconFontDescriptor;
import com.joanzapata.iconify.Iconify;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import okhttp3.Interceptor;

/**
 * 全局信息配置器,可在Application中调用配置器
 *
 * Created by 傅令杰 on 2017/3/29
 */

public final class Configurator {

    // 创建存放配置信息的Map
    private static final HashMap<Object, Object> LATTE_CONFIGS = new HashMap<>();
    // 一个全局的handler
    private static final Handler HANDLER = new Handler();
    // 初始化图标字体的使用空间
    private static final ArrayList<IconFontDescriptor> ICONS = new ArrayList<>();
    // 初始化存放拦截器指示器的Map
    private static final ArrayList<Interceptor> INTERCEPTORS = new ArrayList<>();
    private static final StringBuilder USER_AGENT_STRING_BUILDER = new StringBuilder();


    // 构造函数  开始初始化
    private Configurator() {
        LATTE_CONFIGS.put(ConfigKeys.CONFIG_READY, false);
        LATTE_CONFIGS.put(ConfigKeys.HANDLER, HANDLER);
    }

    // -- ↓ 经典懒汉式的单例模式 ↓-------------------------------------------------
    static Configurator getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final Configurator INSTANCE = new Configurator();
    }
    // -- ↑ 经典懒汉式的单例模式 ↑-------------------------------------------------

    // 获取配置信息
    final HashMap<Object, Object> getLatteConfigs() {
        return LATTE_CONFIGS;
    }



    // 配置ApiHost
    public final Configurator withApiHost(String host){
        LATTE_CONFIGS.put(ConfigKeys.API_HOST,host);
        return this;
    }

    // 配置NativeApiHost
    public final Configurator withNativeApiHost(String host) {
        LATTE_CONFIGS.put(ConfigKeys.NATIVE_API_HOST, host);
        return this;
    }

    // 配置WebApiHost
    public final Configurator withWebApiHost(String host) {
        //只留下域名，否则无法同步cookie，不能带http://或末尾的/
        String hostName = host
                .replace("http://", "")
                .replace("https://", "");
        hostName = hostName.substring(0, hostName.lastIndexOf('/'));
        LATTE_CONFIGS.put(ConfigKeys.WEB_API_HOST, hostName);
        return this;
    }

    // 配置过渡界面的延时
    public final Configurator withLoaderDelayed(long delayed) {
        LATTE_CONFIGS.put(ConfigKeys.LOADER_DELAYED, delayed);
        return this;
    }

    // 配置加载第三方图片字体// https://github.com/JoanZapata/android-iconify
    private void initIcons() {
        if (ICONS.size() > 0) {
            final Iconify.IconifyInitializer initializer = Iconify.with(ICONS.get(0));
            for (int i = 1; i < ICONS.size(); i++) {
                initializer.with(ICONS.get(i));
            }
        }
    }

    // 将图片字体添加到集合中 , 之后执行configure()会将图片字体统一初始化到项目中
    public final Configurator withIcon(IconFontDescriptor descriptor) {
        ICONS.add(descriptor);
        return this;
    }

    // 添加单个拦截器
    public final Configurator withInterceptor(Interceptor interceptor) {
        INTERCEPTORS.add(interceptor);
        LATTE_CONFIGS.put(ConfigKeys.INTERCEPTOR, INTERCEPTORS);
        return this;
    }

    // 添加拦截器集合
    public final Configurator withInterceptors(ArrayList<Interceptor> interceptors) {
        INTERCEPTORS.addAll(interceptors);
        LATTE_CONFIGS.put(ConfigKeys.INTERCEPTOR, INTERCEPTORS);
        return this;
    }

    public final Configurator withWeChatAppId(String appId) {
        LATTE_CONFIGS.put(ConfigKeys.WE_CHAT_APP_ID, appId);
        return this;
    }

    public final Configurator withWeChatAppSecret(String appSecret) {
        LATTE_CONFIGS.put(ConfigKeys.WE_CHAT_APP_SECRET, appSecret);
        return this;
    }

    public final Configurator withActivity(Activity activity) {
        LATTE_CONFIGS.put(ConfigKeys.ACTIVITY, activity);
        return this;
    }

    public Configurator withJavascriptInterface(@NonNull String name) {
        LATTE_CONFIGS.put(ConfigKeys.JAVASCRIPT_INTERFACE, name);
        return this;
    }

    /*public Configurator withWebEvent(@NonNull String name, @NonNull Event event) {
        final EventManager manager = EventManager.getInstance();
        manager.addEvent(name, event);
        return this;
    }*/

    //浏览器加载的HOST
    public Configurator withWebHost(String host) {
        LATTE_CONFIGS.put(ConfigKeys.WEB_HOST, host);
        return this;
    }

    public Configurator addWebUserAgent(String userAgent) {
        USER_AGENT_STRING_BUILDER.append(userAgent).append(" ");
        LATTE_CONFIGS.put(ConfigKeys.USER_AGENTS, USER_AGENT_STRING_BUILDER);
        return this;
    }

    // 检测配置信息 是否完成
    private void checkConfiguration() {
        final boolean isReady = (boolean) LATTE_CONFIGS.get(ConfigKeys.CONFIG_READY);
        if (!isReady) {
            throw new RuntimeException("Configuration is not ready,call configure");
        }
    }

    // 通过key获取配置的数据
    @SuppressWarnings("unchecked")
    final <T> T getConfiguration(Object key) {
        checkConfiguration();
        final Object value = LATTE_CONFIGS.get(key);
        if (value == null) {
            throw new NullPointerException(key.toString() + " IS NULL");
        }
        return (T) LATTE_CONFIGS.get(key);
    }

    // 初始化完成
    public final void configure() {
        initIcons();
        Logger.addLogAdapter(new AndroidLogAdapter());
        LATTE_CONFIGS.put(ConfigKeys.CONFIG_READY, true);
        // Utils.init(Latte.getApplicationContext());
    }
}
