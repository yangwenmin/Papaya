package com.kylin.core.net.download;

import android.os.AsyncTask;

import com.kylin.core.net.RestCreator;
import com.kylin.core.net.callback.IError;
import com.kylin.core.net.callback.IFailure;
import com.kylin.core.net.callback.IRequest;
import com.kylin.core.net.callback.ISuccess;
import com.kylin.core.net.callback.OnDownLoadProgress;

import java.util.WeakHashMap;

import androidx.annotation.NonNull;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 傅令杰 on 2017/4/2
 */

public final class DownloadHandler {


    private final String URL;// 请求网址
    // private static final WeakHashMap<String, Object> PARAMS = RestCreator.getParams();// 请求参数
    private static final WeakHashMap<String, Object> PARAMS  = new WeakHashMap<>();// 请求参数  // 因为下载不需要参数了
    private final IRequest REQUEST;
    private final String DOWNLOAD_DIR;// 文件下载路径
    private final String EXTENSION;// 后缀名
    private final String NAME;// 文件名称
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;
    private final OnDownLoadProgress ONDOWNLOADPROGRESS;


    public DownloadHandler(String url,
                           IRequest request,
                           String download_dir,
                           String extension,
                           String name,
                           ISuccess success,
                           IFailure failure,
                           IError error,
                           OnDownLoadProgress ondownloadprogress) {
        this.URL = url;
        this.REQUEST = request;
        this.DOWNLOAD_DIR = download_dir;
        this.EXTENSION = extension;
        this.NAME = name;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;
        this.ONDOWNLOADPROGRESS = ondownloadprogress;
    }

    // 进行下载
    public final void handleDownload() {

        if (REQUEST != null) {
            REQUEST.onRequestStart();
        }
        RestCreator.getRestService().download(URL, PARAMS)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        if (response.isSuccessful()) {
                            final ResponseBody responseBody = response.body();
                            final SaveFileTask task = new SaveFileTask(REQUEST, SUCCESS,ONDOWNLOADPROGRESS);
                            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, DOWNLOAD_DIR, EXTENSION, responseBody, NAME);

                            // 这里一定要注意,否则文件下载不全
                            if (task.isCancelled()) {
                                if (REQUEST != null) {
                                    REQUEST.onRequestEnd();
                                }
                            }
                        } else {
                            if (ERROR != null) {
                                ERROR.onError(response.code(), response.message());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        if (FAILURE != null) {
                            FAILURE.onFailure();
                        }
                    }
                });

    }
}
