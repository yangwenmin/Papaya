package com.kylin.core.loader;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.blankj.utilcode.util.ScreenUtils;
import com.kylin.core.R;
import com.kylin.core.utils.dimen.DimenUtil;
import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.Indicator;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatDialog;

/**
 * Created by 傅令杰 on 2017/4/2
 */

public final class LatteLoader {

    // 设置dialog占全屏幕的宽高比
    private static final int LOADER_SIZE_SCALE = 8;
    // 偏移量
    private static final int LOADER_OFFSET_SCALE = 10;

    // 创建一个集合存放Loader,便于管理
    private static final ArrayList<AppCompatDialog> LOADERS = new ArrayList<>();

    // 设置一个默认缓存界面,
    private static final String DEFAULT_LOADER = LoaderStyle.BallClipRotatePulseIndicator.name();

    // ----↓ loading页面第一种 ↓——————————————————————————————————————————------------------------------------------
    private static AppCompatDialog createDialog(
            Context context, AVLoadingIndicatorView avLoadingIndicatorView) {

        final AppCompatDialog dialog = new AppCompatDialog(context, R.style.loading_dialog);
        int deviceWidth = ScreenUtils.getScreenWidth();
        int deviceHeight = ScreenUtils.getScreenHeight();
        final Window dialogWindow = dialog.getWindow();
        dialog.setContentView(avLoadingIndicatorView);

        if (dialogWindow != null) {
            final WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = deviceWidth / LOADER_SIZE_SCALE;
            lp.height = deviceHeight / LOADER_SIZE_SCALE;
            lp.height = lp.height + deviceHeight / LOADER_OFFSET_SCALE;
            lp.gravity = Gravity.CENTER;
        }
        LOADERS.add(dialog);
        return dialog;
    }

    public static void showLoading(Context context, String type) {
        final AVLoadingIndicatorView avLoadingIndicatorView = new AVLoadingIndicatorView(context);
        avLoadingIndicatorView.setIndicator(type);
        createDialog(context, avLoadingIndicatorView).show();
    }

    public static void showLoading(Context context, Indicator indicator) {
        final AVLoadingIndicatorView avLoadingIndicatorView = new AVLoadingIndicatorView(context);
        avLoadingIndicatorView.setIndicator(indicator);
        createDialog(context, avLoadingIndicatorView).show();
    }
    // ----↑ loading页面第一种 ↑------------------------------------------------------------------------------------


    // ----↓ loading页面第2种 ↓——————————————————————————————————————————------------------------------------------
    // 根据传入不同的type,展示不同缓存页面   // protype false: 点击不可隐藏滚动条  true:点击可隐藏滚动
    public static void showLoading(Context context, String type, boolean protype) {
        final AppCompatDialog dialog = new AppCompatDialog(context, R.style.loading_dialog);
        final AVLoadingIndicatorView avLoadingIndicatorView = LoaderCreator.create(type, context);
        dialog.setContentView(avLoadingIndicatorView);
        if (protype) {
            dialog.setCancelable(protype);// 点击不可消失
        } else {
            dialog.setCancelable(false);// 点击不可消失
        }


        int deviceWidth = DimenUtil.getScreenWidth();
        int deviceHeight = DimenUtil.getScreenHeight();

        // 设置dialog窗口大小
        final Window dialogWindow = dialog.getWindow();
        if (dialogWindow != null) {
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            /*lp.width = deviceWidth / LOADER_SIZE_SCALE;
            lp.height = deviceHeight / LOADER_SIZE_SCALE;
            lp.height = lp.height + deviceHeight / LOADER_OFFSET_SCALE;
            lp.gravity = Gravity.CENTER;*/

            lp.width = deviceWidth / LOADER_SIZE_SCALE;
            lp.height = deviceWidth / LOADER_SIZE_SCALE;
        }
        LOADERS.add(dialog);
        dialog.show();
    }

    // 默认展示的 Loading页面 // 点击不可取消
    public static void showLoading(Context context) {
        showLoading(context, DEFAULT_LOADER, false);
    }

    // 默认展示的 Loading页面 // false: 点击不可隐藏滚动条  true:点击可隐藏滚动
    public static void showLoading(Context context, boolean protype) {
        showLoading(context, DEFAULT_LOADER, protype);
    }


    // 根据传入的不同type展示 Loading页面
    public static void showLoading(Context context, Enum<LoaderStyle> type) {
        showLoading(context, type.name(), false);
    }
    // ----↑ loading页面第2种 ↑------------------------------------------------------------------------------------


    // 关闭Loading页面
    public static void stopLoading() {
        for (AppCompatDialog dialog : LOADERS) {
            if (dialog != null) {
                if (dialog.isShowing()) {
                    try {
                        dialog.cancel();
                        // dialog.dismiss();
                        stopAnimation();
                    } catch (Exception e) {

                    }
                }
            }
        }
        LOADERS.clear();
    }

    private static AnimationDrawable animationDrawable;

    private static void stopAnimation() {
        if (animationDrawable != null && animationDrawable.isRunning()) {
            animationDrawable.stop();
        }
    }
}
