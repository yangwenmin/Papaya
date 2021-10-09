package com.kylin.core.net;

import android.content.Context;


import com.kylin.core.net.callback.IError;
import com.kylin.core.net.callback.IFailure;
import com.kylin.core.net.callback.IRequest;
import com.kylin.core.net.callback.ISuccess;
import com.kylin.core.net.callback.OnDownLoadProgress;
import com.kylin.core.net.callback.RequestCallbacks;
import com.kylin.core.net.download.DownloadHandler;
import com.kylin.core.ui.loader.LatteLoader;
import com.kylin.core.ui.loader.LoaderStyle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by 傅令杰 on 2017/4/2
 */

public final class RestClient {

    private final WeakHashMap<String, Object> PARAMS = new WeakHashMap<>();// 请求参数

    private final String URL;// 请求网址
    private final IRequest REQUEST;
    private final String DOWNLOAD_DIR;// 文件下载路径
    private final String EXTENSION;// 后缀名
    private final String NAME;// 文件名称
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;
    private final RequestBody BODY;// 请求体 json
    private final LoaderStyle LOADER_STYLE;// 需要展示loading页面 (不同type类型,展示不同loading页)
    private final File FILE;// 将上传的文件
    private final Context CONTEXT;
    private final OnDownLoadProgress ONDOWNLOADPROGRESS;


    public RestClient(String url,
                      Map<String, Object> params,
                      String download_dir,
                      String extension,
                      String name,
                      IRequest irequest,
                      ISuccess isuccess,
                      IFailure ifailure,
                      IError ierror,
                      RequestBody body,
                      File file,
                      Context context,
                      OnDownLoadProgress ondownloadprogress,
                      LoaderStyle loaderStyle) {
        this.URL = url;
        PARAMS.clear();
        PARAMS.putAll(params);
        this.REQUEST = irequest;
        this.DOWNLOAD_DIR = download_dir;
        this.EXTENSION = extension;
        this.NAME = name;
        this.SUCCESS = isuccess;
        this.FAILURE = ifailure;
        this.ERROR = ierror;
        this.BODY = body;
        this.FILE = file;
        this.CONTEXT = context;
        this.ONDOWNLOADPROGRESS = ondownloadprogress;
        this.LOADER_STYLE = loaderStyle;
    }

    public static RestClientBuilder builder() {
        return new RestClientBuilder();
    }

    private void request(HttpMethod method) {
        // 加载RestCreator,并获取RestService
        final RestService service = RestCreator.getRestService();
        Call<String> call = null;

        if (REQUEST != null) {
            REQUEST.onRequestStart();
        }

        if (LOADER_STYLE != null) {
            LatteLoader.showLoading(CONTEXT, LOADER_STYLE);
        }

        switch (method) {
            case GET:
                call = service.get(URL, PARAMS);
                break;
            case POST:
                call = service.post(URL, PARAMS);
                break;
            case POST_RAW:
                call = service.postRaw(URL, BODY);
                break;
            case PUT:
                call = service.put(URL, PARAMS);
                break;
            case PUT_RAW:
                call = service.putRaw(URL, BODY);
                break;
            case DELETE:
                call = service.delete(URL, PARAMS);
                break;
            case UPLOAD:
                /*final RequestBody requestBody =
                        RequestBody.create(MediaType.parse(MultipartBody.FORM.toString()), FILE);
                final MultipartBody.Part body =
                        MultipartBody.Part.createFormData("file", FILE.getName(), requestBody);
                call = service.upload(URL, body);*/

                final RequestBody requestBody = RequestBody.create(MediaType.parse(MultipartBody.FORM.toString()), FILE);
                final MultipartBody.Part body = MultipartBody.Part.createFormData("file", FILE.getName(), requestBody);
                MultipartBody.Part usercode =MultipartBody.Part.createFormData("usercode", "20000");
                MultipartBody.Part device =MultipartBody.Part.createFormData("device", "Android");
                List<MultipartBody.Part > files = new ArrayList<>();
                files.add(body);
                files.add(usercode);
                files.add(device);
                //call = RestCreator.getRestService().upload(URL, body);
                call = RestCreator.getRestService().upload(URL, files);
                break;
            default:
                break;
        }

        if (call != null) {
            call.enqueue(getRequestCallback());
        }
    }

    private Callback<String> getRequestCallback() {
        return new RequestCallbacks(
                REQUEST,
                SUCCESS,
                FAILURE,
                ERROR,
                LOADER_STYLE
        );
    }

    public final void get() {
        request(HttpMethod.GET);
    }

    public final void post() {
        if (BODY == null) {
            request(HttpMethod.POST);
        } else {
            if (!PARAMS.isEmpty()) {
                throw new RuntimeException("params must be null!");
            }
            request(HttpMethod.POST_RAW);
        }
    }

    public final void put() {
        if (BODY == null) {
            request(HttpMethod.PUT);
        } else {
            if (!PARAMS.isEmpty()) {
                throw new RuntimeException("params must be null!");
            }
            request(HttpMethod.PUT_RAW);
        }
    }

    public final void delete() {
        request(HttpMethod.DELETE);
    }

    public final void upload() {
        request(HttpMethod.UPLOAD);
    }

    public final void download() {
        new DownloadHandler(URL, REQUEST, DOWNLOAD_DIR, EXTENSION, NAME, SUCCESS, FAILURE, ERROR,ONDOWNLOADPROGRESS).handleDownload();
    }
}
