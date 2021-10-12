package com.kylin.core.initbase;

import android.Manifest;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.Serializable;

/**
 * 系统常量配置
 */
public class InitValues implements Serializable {

    // 版本序列号
    private static final long serialVersionUID = 3714072522003250341L;

    // 是否上线版本  ture：发布线上版本，不记录日志  |  false：测试版本，记录日志
    public static boolean isOnline = true;

    // 是否正在上传新增终端失败的终端
    public static boolean isUploadTermAdd = false;

    //今日要事是否允许提醒
    public static boolean isDayThingWarn = false;

    // 常用标识
    public static final String FLAG_0 = "0";
    public static final String FLAG_1 = "1";
    public static final String FLAG_2 = "2";
    public static final String FLAG_3 = "3";
    public static final String FLAG_4 = "4";

    // 成功、失败消息字符串常量
    public static final String SUCCESS = "Y";
    public static final String ERROR = "N";// 系统异常请联系管理员  后台直接出现异常 返回的数据不是规则的
    public static final String EXCEPTION = "E";//  后台异常,返回的数据是规则的

    // 登录后同步数据
    public static final String SYNCDATA = "syncdata";
    public static final String SYNCDATA_CXY = "syncdata_cxy";// 促销员
    public static final String SYNCDATA_XIAOZU = "syncdata_xiaozu";//  小组促销管理
    public static final String SYNCDATA_BUMEN = "syncdata_bumen";//  部门促销管理
    public static final String SYNCDATA_DAQU = "syncdata_daqu";//  大区促销主管
    public static final String SYNCDATA_SHENGQU = "syncdata_shengqu";//  省区促销管理
    public static final String SYNCDATA_ZX = "syncdata_zx";//  中心促销管理
    public static final String SYNCDATA_OTHER = "syncdata_other";//  其他 比如:主管


    public static final String SYNCDATA_SQ = "syncdata_sq";

    // 点击路程 同步数据
    public static final String SYNCDATALINE = "syncdataline";

    // handler wait
    public static final int WAIT0 = 0;
    public static final int WAIT1 = 1;
    public static final int WAIT2 = 2;
    public static final int WAIT3 = 3;
    public static final int WAIT4 = 4;
    public static final int WAIT5 = 5;
    public static final int WAIT6 = 6;
    public static final int WAIT7 = 7;
    public static final int WAIT8 = 8;
    public static final int WAIT9 = 9;
    public static final int WAIT10 = 10;

    // 指标标删除状态（解决重复指标）
    public static final String delFlag = "8";

    // 登录信息缓存Key
    public static final String LOGINSESSIONKEY = "loginSesion";

    // 权限相关 ↓--------------------------------------------------------------------------
    /**
     * 权限常量相关
     */
    public static final int WRITE_READ_EXTERNAL_CODE = 0x01;// 读写内存卡权限请求码
    public static final int HARDWEAR_CAMERA_CODE = 0x02;// 相机权限请求码
    public static final int LOCAL_CODE = 0x03;// 定位请求码
    public static final int WRITE_LOCAL_CODE = 0x04;// 拍照+ 读写
    public static final int LOCATION_ALL_CODE = 0x05;// 所有权限   拍照 + 读写内存卡 + 定位 电话
    public static final int LOCATION_WEB_CODE = 0x06;// web定位所有权限
    public static final int READ_CALL_LOG_CODE = 0x07;// 获取通话记录
    public static final int CALL_PHONE_CODE = 0x08;// 拨打电话

    // 读写内存卡权限
    public static final String[] WRITE_READ_EXTERNAL_PERMISSION = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    // 相机权限
    public static final String[] HARDWEAR_CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA};
    // 定位权限
    public static final String[] LOCAL_PERMISSION = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
    //public static final String[] LOCAL_PERMISSION = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION};
    // 读写内存卡权限,相机权限,定位权限
    public static final String[] WRITE_EXTERNAL_CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    // 全部权限
    public static final String[] LOCATION_ALL_CODE_PERMISSION = new String[]{
            Manifest.permission.CAMERA,// 拍照相机
            Manifest.permission.WRITE_EXTERNAL_STORAGE, // 写入内存卡
            Manifest.permission.READ_EXTERNAL_STORAGE, // 读取内存卡
            Manifest.permission.ACCESS_COARSE_LOCATION,// 网络定位
            Manifest.permission.READ_PHONE_STATE,// 手机状态
            Manifest.permission.CALL_PHONE,// 拨打电话
            Manifest.permission.READ_CONTACTS,// 获取联系人
            Manifest.permission.READ_CALL_LOG,// 获取通话记录
            Manifest.permission.RECORD_AUDIO,// 录音
            Manifest.permission.ACCESS_FINE_LOCATION};// GPS定位

    // web定位权限
    public static final String[] LOCATION_WEB_PERMISSION = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};// WRITE_EXTERNAL_STORAGE


    // 获取通话记录权限权限
    public static final String[] READ_CALL_LOG_PERMISSION = new String[]{Manifest.permission.READ_CALL_LOG};

    // 拨打电话权限
    public static final String[] CALL_PHONE_PERMISSION = new String[]{Manifest.permission.CALL_PHONE};

    // 权限相关 ↑--------------------------------------------------------------------------

    // fileProvider
    public static final String PROVIDER = "com.papaya";



}
