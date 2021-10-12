package com.papaya;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.kylin.core.net.RestClient;
import com.kylin.core.net.callback.IError;
import com.kylin.core.net.callback.IFailure;
import com.kylin.core.net.callback.ISuccess;
import com.kylin.core.net.callback.OnDownLoadProgress;
import com.kylin.core.utils.dbtutil.DbtLog;
import com.papaya.application.ConstValues;

import java.io.ByteArrayInputStream;
import java.io.InputStream;


/**
 *
 */
public class MainService {

    private final String TAG = "MainService";

    private Context context;
    private Handler handler;

    /**
     *
     */
    public MainService() {

    }

    public MainService(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }


    // ----↓ 下载PDF ↓---------------------------------------------------------------------------------------------


    /**
     * 下载txt
     *
     * @param downloadUrl  下载链接 : http://smp.tsingtao.com.cn/static/ddtracepic/cxyschool/knowledge/逸品纯生产品介绍20210420033745.pdf
     * @param textName     本地文件名称 : 123.txt
     * @param isShowDialog 是否显示进度条 false不显示下载进度,true显示下载进度框
     * @param requestCode  用于Activity界面handle的对应回调接收 比如what1,what2
     */
    public void downloadText(String downloadUrl, String textName, final boolean isShowDialog, final int requestCode) {

        if (isShowDialog) {
            // 展示进度条
            showDownloadDialog();
        }


        RestClient.builder()
                .url(downloadUrl)// http://192.168.31.128:8080/landking/video/LIST.TXT
                // .params("data", jsonZip)
                .loader(context)// 滚动条
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        // 关闭进度条
                        stopDownloadDialog();
                        //用handle通知主线程 下载完成 -> 开始安装
                        Bundle bundle = new Bundle();
                        bundle.putString("formjson", response);
                        bundle.putString("status", ConstValues.SUCCESS);
                        if (handler != null) {
                            Message msg = new Message();
                            msg.what = requestCode;
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        }
                    }
                })
                .onDownLoadProgress(new OnDownLoadProgress() {
                    @Override
                    public void onProgressUpdate(long fileLength, int downLoadedLength) {
                        // 用handle通知主线程刷新进度, progress: 是1-100的正整数

                        Message updateMsg = Message.obtain();
                        updateMsg.obj = fileLength;
                        updateMsg.arg1 = downLoadedLength;

                        if (isShowDialog) {
                            // 进度条进度
                            showDownloading(updateMsg);
                        }


                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {
                        // 关闭进度条
                        stopDownloadDialog();

                        Bundle bundle = new Bundle();
                        bundle.putString("formjson", msg);
                        bundle.putString("status", ConstValues.ERROR);
                        if (handler != null) {
                            Message msg2 = new Message();
                            msg2.what = requestCode;
                            msg2.setData(bundle);
                            handler.sendMessage(msg2);
                        }
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {
                        // 关闭进度条
                        stopDownloadDialog();

                        Bundle bundle = new Bundle();
                        bundle.putString("formjson", "请检查您的网络");
                        bundle.putString("status", ConstValues.EXCEPTION);
                        if (handler != null) {
                            Message msg = new Message();
                            msg.what = requestCode;
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        }
                    }
                })
                .name(textName)
                .dir(ConstValues.LOCALPATH)
                .builde()
                .download();
    }


    private AlertDialog downloadDialog;//正在下载的对话框
    private ProgressBar pb;//下载的进度条
    private TextView tvCur;//当前下载的百分比

    // 展示进度条
    private void showDownloadDialog() {
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setCancelable(false);// 不可消失
        downloadDialog = adb.create();
        View view = View.inflate(context, R.layout.download_pdf_dialog_layout, null);
        downloadDialog.setView(view, 0, 0, 0, 0);
        tvCur = (TextView) view.findViewById(R.id.tv_cursize_pdf);
        pb = (ProgressBar) view.findViewById(R.id.download_pb_pdf);

        downloadDialog.show();
    }

    // 进度条进度
    private void showDownloading(Message msg) {

        // 自己写的
        long totalSize = (long) msg.obj;// 总进度
        int curSize = (int) msg.arg1;// 获取当前进度
        pb.setMax((int) totalSize);
        pb.setProgress(curSize);

    }

    // 关闭进度条
    private void stopDownloadDialog() {
        // Toast.makeText(getActivity(), "下载成功", Toast.LENGTH_SHORT).show();
        if (downloadDialog != null && downloadDialog.isShowing()) {
            downloadDialog.dismiss();
            // supportFragmentManager.popBackStack();
        }
    }
    // ----↑ 下载PDF ↑---------------------------------------------------------------------------------------------


    public void getJsonWorks(String downloadurl) {


        RestClient.builder()
                //.url(PropertiesUtil.getProperties("platform_ip"))
                .url(downloadurl)
                // .params("data", jsonZip)
                //.params("data", jsonZip)
                // .loader(LoginActivity.this)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        if (!"".equals(response)) {
                            /*//ResponseStructBean resObj = HttpParseJson.parseRes(response);
                            ResponseStructBean resObj = new ResponseStructBean();
                            resObj = JsonUtil.parseJson(response, ResponseStructBean.class);
                            if (resObj != null) {
                                if (ConstValues.SUCCESS.equals(resObj.getResHead().getStatus())) {
                                    // 保存信息
                                    String formjson = resObj.getResBody().getContent();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("formjson", formjson);
                                    bundle.putString("status", ConstValues.SUCCESS);
                                    if (handler != null) {
                                        Message msg = new Message();
                                        msg.what = ConstValues.WAIT9;
                                        msg.setData(bundle);
                                        handler.sendMessage(msg);
                                    }
                                } else {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("formjson", resObj.getResBody().getContent());
                                    bundle.putString("status", ConstValues.ERROR);
                                    if (handler != null) {
                                        Message msg = new Message();
                                        msg.what = ConstValues.WAIT9;
                                        msg.setData(bundle);
                                        handler.sendMessage(msg);
                                    }
                                    DbtLog.logUtils("返回数据异常E", response);
                                }
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putString("formjson", "数据返回异常");
                                bundle.putString("status", ConstValues.ERROR);
                                if (handler != null) {
                                    Message msg = new Message();
                                    msg.what = ConstValues.WAIT9;
                                    msg.setData(bundle);
                                    handler.sendMessage(msg);
                                }
                                DbtLog.logUtils("返回数据异常N", response);
                            }*/

                            String jsonString = "";
                            String resultString = "";
                            try {
                                InputStream inputStream = new ByteArrayInputStream(response.getBytes());
                                /*BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
                                while ((jsonString=bufferedReader.readLine())!=null) {
                                    resultString+=jsonString;
                                }*/

                                byte[] buffer = new byte[inputStream.available()];
                                inputStream.read(buffer);
                                // resultString=new String(buffer,"GB2312");
                                resultString = new String(buffer, "UTF-8");

                            } catch (Exception e) {
                                // TODO: handle exception
                            }


                            // 保存信息
                            Bundle bundle = new Bundle();
                            bundle.putString("formjson", resultString);
                            bundle.putString("status", ConstValues.SUCCESS);
                            if (handler != null) {
                                Message msg = new Message();
                                msg.what = ConstValues.WAIT9;
                                msg.setData(bundle);
                                handler.sendMessage(msg);
                            }


                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putString("formjson", "数据返回异常");
                            bundle.putString("status", ConstValues.ERROR);
                            if (handler != null) {
                                Message msg = new Message();
                                msg.what = ConstValues.WAIT9;
                                msg.setData(bundle);
                                handler.sendMessage(msg);
                            }
                            DbtLog.logUtils("返回数据异常R", response);
                        }
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg2) {
                        Bundle bundle = new Bundle();
                        bundle.putString("formjson", msg2);
                        bundle.putString("status", ConstValues.ERROR);
                        if (handler != null) {
                            Message msg = new Message();
                            msg.what = ConstValues.WAIT9;
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        }
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {
                        Bundle bundle = new Bundle();
                        bundle.putString("formjson", "请检查您的网络");
                        bundle.putString("status", ConstValues.EXCEPTION);
                        if (handler != null) {
                            Message msg = new Message();
                            msg.what = ConstValues.WAIT9;
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        }
                    }
                })
                .builde()
                .post();
    }

}
