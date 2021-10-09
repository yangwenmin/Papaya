package com.kylin.core.initbase;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * Created by ywm on 2018/8/20.
 */

public class PermissionActivity extends SupportActivity {

    // 权限相关 ↓--------------------------------------------------------------------------

    // 用户通过了几项权限
    private int count;

    /**
     * 判断是否有指定的权限
     */
    public boolean hasPermission(String... permissions) {

        for (String permisson : permissions) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), permisson) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 申请指定的权限.
     */
    public void requestPermission(int code, String... permissions) {

        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(permissions, code);
        }
        count = 0;
    }


    /**
     * 请求权限后回调的方法
     *
     * @param requestCode  是我们自己定义的权限请求码
     * @param permissions  是我们请求的权限名称数组
     * @param grantResults 是我们在弹出页面后是否允许权限的标识数组，数组的长度对应的是权限名称数组的长度，数组的数据0表示允许权限，-1表示我们点击了禁止权限
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        boolean hasPermissionDismiss = false;//有权限没有通过
        switch (requestCode) {
            case InitValues.HARDWEAR_CAMERA_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doOpenCamera();
                } else {
                    Toast.makeText(getApplicationContext(), "请先开启相机权限", Toast.LENGTH_SHORT).show();
                }
                break;
            case InitValues.WRITE_READ_EXTERNAL_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doWriteSDCard();
                } else {
                    Toast.makeText(getApplicationContext(), "请先开启读写存储权限", Toast.LENGTH_SHORT).show();
                }
                break;
            case InitValues.LOCAL_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doLocation();
                } else {
                    Toast.makeText(getApplicationContext(), "请先开启定位权限", Toast.LENGTH_SHORT).show();
                }
                break;
            case InitValues.WRITE_LOCAL_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doCameraWriteSD();
                } else {
                    Toast.makeText(getApplicationContext(), "请先开启读写存储权限", Toast.LENGTH_SHORT).show();
                }
                break;
            case InitValues.LOCATION_ALL_CODE:

                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == -1) {
                        hasPermissionDismiss = true;
                    }
                }

                //如果有权限没有被允许
                if (hasPermissionDismiss) {
                    showPermissionDialog();//跳转到系统设置权限页面，或者直接关闭页面，不让他继续访问
                    Toast.makeText(getApplicationContext(), "请先开启权限", Toast.LENGTH_SHORT).show();
                } else {
                    //全部权限通过，可以进行下一步操作。。。
                    doDbtThing();
                }

                break;
            case InitValues.LOCATION_WEB_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doWebLocation();
                } else {
                    Toast.makeText(getApplicationContext(), "请先开启定位权限", Toast.LENGTH_SHORT).show();
                }

                break;
            case InitValues.READ_CALL_LOG_CODE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doReadCallLog();
                } else {
                    Toast.makeText(getApplicationContext(), "请先开启获取通话记录权限", Toast.LENGTH_SHORT).show();
                }

                break;
            case InitValues.CALL_PHONE_CODE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doCallPhone();
                } else {
                    Toast.makeText(getApplicationContext(), "请开启拨打电话权限", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }




    /**
     * 不再提示权限时的展示对话框
     */
    AlertDialog mPermissionDialog;
    String mPackName = "cxy.tsingtaopad";

    private void showPermissionDialog() {
        if (mPermissionDialog == null) {
            mPermissionDialog = new AlertDialog.Builder(this)
                    .setMessage("已禁用权限，请手动授予")
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mPermissionDialog.cancel();

                            Uri packageURI = Uri.parse("package:" + mPackName);
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //关闭页面或者做其他操作
                            mPermissionDialog.cancel();
                        }
                    })
                    .create();
        }
        mPermissionDialog.show();
    }


    private void doWebLocation() {

    }

    public void doCameraWriteSD() {

    }

    // 定位
    public void doLocation() {
    }


    // 拍照
    public void doOpenCamera() {

    }

    // 读写SD卡业务逻辑,由具体的子类实现
    public void doWriteSDCard() {

    }

    // 开启所有权限
    public void doDbtThing() {

    }

    // 通话记录权限
    private void doReadCallLog() {

    }

    // 拨打电话
    public void doCallPhone() {

    }
    // 权限相关 ↑--------------------------------------------------------------------------

}
