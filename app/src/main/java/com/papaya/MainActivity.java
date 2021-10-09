package com.papaya;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kylin.core.app.Latte;
import com.kylin.core.net.RestClient;
import com.kylin.core.net.callback.IError;
import com.kylin.core.net.callback.IFailure;
import com.kylin.core.net.callback.ISuccess;
import com.kylin.core.net.callback.OnDownLoadProgress;
import com.papaya.application.ConstValues;
import com.papaya.base.PermissionActivity;
import com.papaya.demo4.Demo4Activity;
import com.papaya.gsydemo.GsyDemoActivity;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends PermissionActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.main_btn_gsy);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 测试GsyDemo
                // startActivity(new Intent(MainActivity.this, GsyDemoActivity.class));

                // 测试第4节
                // startActivity(new Intent(MainActivity.this, Demo4Activity.class));


                if (hasPermission(ConstValues.WRITE_READ_EXTERNAL_PERMISSION)) {
                    // 拥有了此权限,那么直接执行业务逻辑

                    // 测试下载 // 请求url // 保存本地文件名称 name_LIST.TXT
                    downloadText(ConstValues.HTTPID + ConstValues.VIDEOPATH + ConstValues.TXTNAME, ConstValues.TXTNAME);

                } else {
                    // 还没有对一个权限(请求码,权限数组)这两个参数都事先定义好
                    requestPermission(ConstValues.WRITE_READ_EXTERNAL_CODE, ConstValues.WRITE_READ_EXTERNAL_PERMISSION);
                }

                // 测试Latte.getApplicationContext()
                Toast.makeText(Latte.getApplicationContext(), "成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 手动拥有读写权限后 执行下载文件
    @Override
    public void doWriteSDCard() {
        try {
            // 拥有了此权限,那么直接执行业务逻辑
            // 测试下载 // 请求url // 保存本地文件名称 name_LIST.TXT
            downloadText(ConstValues.HTTPID + ConstValues.VIDEOPATH + ConstValues.TXTNAME, ConstValues.TXTNAME);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 下载txt
     *
     * @param downloadurl 下载链接 : http://smp.tsingtao.com.cn/static/ddtracepic/cxyschool/knowledge/逸品纯生产品介绍20210420033745.pdf
     * @param textName    本地文件名称 : 123.txt
     */
    public void downloadText(String downloadurl, String textName) {

        RestClient.builder()
                .url(downloadurl)// http://192.168.31.128:8080/landking/video/LIST.TXT
                // .params("data", jsonZip)
                .loader(MainActivity.this)// 滚动条
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        //用handle通知主线程 下载完成 -> 开始安装
                        Bundle bundle = new Bundle();
                        bundle.putString("formjson", response);
                        /*// bundle.putString("status", ConstValues.SUCCESS);
                        if (handler != null) {
                            Message msg = new Message();
                            msg.what = whatback;
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        }*/
                    }
                })
                .onDownLoadProgress(new OnDownLoadProgress() {
                    @Override
                    public void onProgressUpdate(long fileLength, int downLoadedLength) {
                        // 用handle通知主线程刷新进度, progress: 是1-100的正整数

                        Message updateMsg = Message.obtain();
                        updateMsg.obj = fileLength;
                        updateMsg.arg1 = downLoadedLength;


                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {

                        Bundle bundle = new Bundle();
                        bundle.putString("formjson", msg);
                        /*bundle.putString("status", ConstValues.ERROR);
                        if (handler != null) {
                            Message msg2 = new Message();
                            msg2.what = whatback;
                            msg2.setData(bundle);
                            handler.sendMessage(msg2);
                        }*/
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {

                        Bundle bundle = new Bundle();
                        bundle.putString("formjson", "请检查您的网络");
                        /*bundle.putString("status", ConstValues.EXCEPTION);
                        if (handler != null) {
                            Message msg = new Message();
                            msg.what = whatback;
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        }*/
                    }
                })
                .name(textName)
                .dir(ConstValues.LOCALPATH)
                .builde()
                .download();
    }
}
