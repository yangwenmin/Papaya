package com.kylin.core.net;

import android.content.Context;


import com.kylin.core.net.callback.IError;
import com.kylin.core.net.callback.IFailure;
import com.kylin.core.net.callback.IRequest;
import com.kylin.core.net.callback.ISuccess;
import com.kylin.core.net.callback.OnDownLoadProgress;
import com.kylin.core.loader.LoaderStyle;

import java.io.File;
import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by 傅令杰 on 2017/4/2
 */

public final class RestClientBuilder {

    private final WeakHashMap<String, Object> PARAMS = new WeakHashMap<>();// 请求参数
    private String mUrl = null;// 请求网址
    private String mDownloadDir = null;// 文件下载路径
    private String mExtension = null;// 后缀名
    private String mName = null;// 文件名称
    private IRequest mIRequest = null;
    private ISuccess mISuccess = null;
    private IFailure mIFailure = null;
    private IError mIError = null;
    private RequestBody mBody = null;// 请求体 json
    private LoaderStyle mLoaderStyle = null;// 需要展示loading页面 (不同type类型,展示不同loading页)
    private File mFile = null;// 将上传的文件
    private Context mContext = null;// loading页面需要的上下文

    private OnDownLoadProgress mOnDownLoadProgress = null;



    RestClientBuilder() {
    }

    // 设置url
    public final RestClientBuilder url(String url) {
        this.mUrl = url;
        return this;
    }

    // 添加参数
    public final RestClientBuilder params(WeakHashMap<String, Object> params) {
        PARAMS.clear();
        PARAMS.putAll(params);
        return this;
    }

    // 添加参数(键值对)
    public final RestClientBuilder params(String key, Object value) {
        PARAMS.put(key, value);
        return this;
    }

    // 设置上传的文件
    public final RestClientBuilder file(File file) {
        this.mFile = file;
        return this;
    }

    // 设置上传文件的路径
    public final RestClientBuilder file(String file) {
        this.mFile = new File(file);
        return this;
    }

    // 设置下载文件的名称
    public final RestClientBuilder name(String name) {
        this.mName = name;
        return this;
    }

    // 设置下载文件的路径
    public final RestClientBuilder dir(String dir) {
        this.mDownloadDir = dir;
        return this;
    }

    // 设置下载文件的后缀名
    public final RestClientBuilder extension(String extension) {
        this.mExtension = extension;
        return this;
    }

    // 设置json
    public final RestClientBuilder raw(String raw) {
        this.mBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), raw);
        return this;
    }

    // 设置request
    public final RestClientBuilder onRequest(IRequest iRequest) {
        this.mIRequest = iRequest;
        return this;
    }

    // 成功的回调
    public final RestClientBuilder success(ISuccess iSuccess) {
        this.mISuccess = iSuccess;
        return this;
    }

    // 失败的回调
    public final RestClientBuilder failure(IFailure iFailure) {
        this.mIFailure = iFailure;
        return this;
    }

    // 请求出错的回调
    public final RestClientBuilder error(IError iError) {
        this.mIError = iError;
        return this;
    }

    // 根据LoaderStyle设置loading页面
    public final RestClientBuilder loader(Context context, LoaderStyle style) {
        this.mContext = context;
        this.mLoaderStyle = style;
        return this;
    }

    // 设置默认loading页面
    public final RestClientBuilder loader(Context context) {
        this.mContext = context;
        this.mLoaderStyle = LoaderStyle.BallClipRotatePulseIndicator;
        return this;
    }

    /**
     * 传入下载文件进度回调
     */
    public final RestClientBuilder onDownLoadProgress(OnDownLoadProgress onDownLoadProgress) {
        this.mOnDownLoadProgress = onDownLoadProgress;
        return this;
    }


    // 获取RestClient工具类
    public final RestClient builde() {
        return new RestClient(mUrl,
                PARAMS,
                mDownloadDir,
                mExtension,
                mName,
                mIRequest,
                mISuccess,
                mIFailure,
                mIError,
                mBody,
                mFile,
                mContext,
                mOnDownLoadProgress,
                mLoaderStyle);
    }
}
