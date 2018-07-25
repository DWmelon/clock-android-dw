package com.timediffproject.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.timediffproject.origin.MainApplication;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * Created by eddy on 2015/4/20.
 */
public class DeviceInfoManager {
    private static final String TAG = DeviceInfoManager.class.getSimpleName();
    private static final String VIEW_MAC_ADDRESS_COMMAND = "cat /sys/class/net/wlan0/address";
    private static final String CHANNEL_KEY = "UMENG_CHANNEL";


    public static final String APP_PLATFORM = "android";
    public static final String APP_ID = "gaokao";
    public static String mDeviceUid = "";//设备唯一识别码
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    public static String MODEL = "";
    public static String BRAND;
    public static int SDK_INT;
    public static String CHANNEL;//渠道号
    public static int VERSION_CODE;
    public static String VERSION_NAME;
    public static String INTERFACE_VERSION = "7";//接口版本号
    public static String SYS_VERSION;


    private Context mContext;
    private static DeviceInfoManager mInstance;

    private DeviceInfoManager() {

    }

    public static synchronized DeviceInfoManager getInstance() {
        if (mInstance == null) {
            mInstance = new DeviceInfoManager();
        }

        return mInstance;
    }

    public void init() {
        this.mContext = MainApplication.getContext();
        initInfo();
    }

    public void release() {

    }

    private void initInfo() {

        DisplayMetrics metrics = this.mContext.getResources().getDisplayMetrics();

        if (this.mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            SCREEN_HEIGHT = metrics.heightPixels;
            SCREEN_WIDTH = metrics.widthPixels;
        } else {
            SCREEN_HEIGHT = metrics.widthPixels;
            SCREEN_WIDTH = metrics.heightPixels;
        }

        MODEL = Build.MODEL;
        BRAND = Build.BRAND;
        SDK_INT = Build.VERSION.SDK_INT;
        SYS_VERSION = Build.VERSION.RELEASE;

        CHANNEL = PackageUtil.getMetaDataStringValue(mContext, CHANNEL_KEY);
        VERSION_CODE = PackageUtil.getAppVersionCode(mContext);
        VERSION_NAME = PackageUtil.getPackageVersionName(mContext);

        int result = ActivityCompat.checkSelfPermission(MainApplication.getContext(), Manifest.permission.READ_PHONE_STATE);
        if (result != PackageManager.PERMISSION_GRANTED){
            return;
        }

        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);

        //需要android.permission.READ_PHONE_STATE权限

        String uid = tm.getDeviceId();
        if (!TextUtils.isEmpty(uid)) {
//            Log.d(Log.TAG_DEVICE,"getDeviceId:"+uid);
            mDeviceUid = Utils.md5(uid);
            printInfo();
            return;
        }

        String serial = Build.SERIAL;
        if (!TextUtils.isEmpty(serial)) {
//            Log.d(Log.TAG_DEVICE,"serial:"+serial);
            mDeviceUid = Utils.md5(serial);
            printInfo();
            return;
        }

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String mac = getMacAddress();
                    if (!TextUtils.isEmpty(mac)) {
//                        Log.d(Log.TAG_DEVICE,"getMacAddress:"+mac);
                        mDeviceUid = Utils.md5(mac);
                        printInfo();
                        return;
                    }



                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();

    }

    private void printInfo(){
        Log.d("device","DeviceInfoManager#printInfo channel:"+CHANNEL+",model:"+MODEL+",brand:"+BRAND+",mDeviceUid:"+mDeviceUid);
    }



    private String getMacAddress() throws IOException {
        String macSerial = null;
        String str = "";
        try {
            Process pp = Runtime.getRuntime().exec(VIEW_MAC_ADDRESS_COMMAND);
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }
        return macSerial;
    }



}
