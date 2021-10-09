package com.kylin.core.initbase;

import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;

/**
 * Created by ywm on 2018/8/18.
 */

public class PermissionFragment extends SwipeBackFragment {
    private static final String TAG = "PermissionFragment";


    // 权限相关 ↓--------------------------------------------------------------------------

    // 用户通过了几项权限
    private int count;

    /**
     * 判断是否有指定的权限
     */
    public boolean hasPermission(String... permissions) {

        for (String permisson : permissions) {
            if (ContextCompat.checkSelfPermission(getActivity(), permisson) != PackageManager.PERMISSION_GRANTED) {
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

    // 定义几个常量

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case InitValues.HARDWEAR_CAMERA_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doOpenCamera();
                } else {
                    Toast.makeText(getActivity(), "请先开启相机权限", Toast.LENGTH_SHORT).show();
                }
                break;
            case InitValues.WRITE_READ_EXTERNAL_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doWriteSDCard();
                } else {
                    Toast.makeText(getActivity(), "请先开启读写存储权限", Toast.LENGTH_SHORT).show();
                }
                break;
            case InitValues.LOCAL_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doLocation();
                } else {
                    Toast.makeText(getActivity(), "请先开启定位权限", Toast.LENGTH_SHORT).show();
                }
                break;
            case InitValues.WRITE_LOCAL_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doCameraWriteSD();
                } else {
                    Toast.makeText(getActivity(), "请先开启读写存储权限", Toast.LENGTH_SHORT).show();
                }
                break;
            case InitValues.LOCATION_ALL_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    count++;// 一共3项权限
                    if(count>=3){
                        doDbtThing();
                    }else{
                        Toast.makeText(getActivity(), "请先开启权限", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "请先开启权限", Toast.LENGTH_SHORT).show();
                }

                break;
            case InitValues.LOCATION_WEB_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doWebLocation();
                } else {
                    Toast.makeText(getActivity(), "请先开启定位权限", Toast.LENGTH_SHORT).show();
                }

                break;

            case InitValues.READ_CALL_LOG_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doReadCallLog();
                } else {
                    Toast.makeText(getActivity(), "请开启获取通话记录权限", Toast.LENGTH_SHORT).show();
                }

                break;

            case InitValues.CALL_PHONE_CODE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doCallPhone();
                } else {
                    Toast.makeText(getActivity(), "请开启拨打电话权限", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    public void doWebLocation() {

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
    public void doReadCallLog() {

    }

    // 拨打电话
    public void doCallPhone() {

    }
    // 权限相关 ↑--------------------------------------------------------------------------


}
