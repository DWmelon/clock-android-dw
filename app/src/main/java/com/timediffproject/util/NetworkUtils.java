package com.timediffproject.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.telephony.TelephonyManager;


/**
 * create by eddy lei on 2015/04/21
 */
public class NetworkUtils {
    private static final String TAG = "network";

    /**
     * 检查WIFI是否已经连接
     */
    public static boolean isWifiAvailable(Context ctx) {
        ConnectivityManager conMan = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMan == null) {
            return false;
        }

        NetworkInfo wifiInfo = null;
        try {
            wifiInfo = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return (wifiInfo != null && wifiInfo.getState() == State.CONNECTED);
    }

    /**
     * 检查网络是否连接，WIFI或者手机网络其一
     */
    public static boolean isNetworkAvailable(Context ctx) {
        if (ctx == null) return false;
        ConnectivityManager conMan = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMan == null) {
            return false;
        }

        NetworkInfo mobileInfo = null;
        try {
            mobileInfo = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if (mobileInfo != null && mobileInfo.isConnectedOrConnecting()) {
            return true;
        }

        NetworkInfo wifiInfo = null;
        try {
            wifiInfo = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if (wifiInfo != null && wifiInfo.isConnectedOrConnecting()) {
            return true;
        }

        NetworkInfo activeInfo = null;
        try {
            activeInfo = conMan.getActiveNetworkInfo();
        } catch (NullPointerException e) {

        }
        if (activeInfo != null && activeInfo.isConnectedOrConnecting()) {
            return true;
        }

        return false;
    }


    public static boolean isNetwork2G(Context context) {
        int subType = getMobileNetworkType(context);
        return (subType == TelephonyManager.NETWORK_TYPE_CDMA
                || subType == TelephonyManager.NETWORK_TYPE_EDGE
                || subType == TelephonyManager.NETWORK_TYPE_GPRS
                || subType == TelephonyManager.NETWORK_TYPE_1xRTT
                || subType == TelephonyManager.NETWORK_TYPE_IDEN);
    }

    /**
     * @param context
     * @return TYPE 0 PHONE_TYPE_NONE;1 GSM_PHONE;2 CDMA_PHONE
     */
    public static int getPhoneType(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (manager == null) {
            return TelephonyManager.PHONE_TYPE_NONE;
        }

        return manager.getPhoneType();

    }

    public static boolean isNetwork3G(Context context) {
        int subType = getMobileNetworkType(context);
        return (subType == TelephonyManager.NETWORK_TYPE_UMTS
                || subType == TelephonyManager.NETWORK_TYPE_HSDPA
                || subType == TelephonyManager.NETWORK_TYPE_EVDO_0
                || subType == TelephonyManager.NETWORK_TYPE_EVDO_A
                || subType == TelephonyManager.NETWORK_TYPE_EVDO_B
                || subType == TelephonyManager.NETWORK_TYPE_HSUPA
                || subType == TelephonyManager.NETWORK_TYPE_HSPA
                || subType == TelephonyManager.NETWORK_TYPE_HSPAP); //NETWORK_TYPE_HSPAP 为HSPA+// TODO: 移动3G如何判断?
    }

    public static boolean isNetwork4G(Context context) {
        int subType = getMobileNetworkType(context);
        return subType == TelephonyManager.NETWORK_TYPE_LTE;
    }

    public static boolean isNetworkWifi(Context context){
        return !isNetwork2G(context) && !isNetwork3G(context) && !isNetwork4G(context);
    }

    public static int getMobileNetworkType(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectMgr == null) {
            return -1;
        }
        NetworkInfo info = null;
        try {
            info = connectMgr.getActiveNetworkInfo();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if (info != null && info.getType() == ConnectivityManager.TYPE_MOBILE) {
            return info.getSubtype();
        }
        return -1;
    }

    public static String getMobileNetWrokTypeName(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) return "";
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return networkInfo.getTypeName();
        }
        return "";
    }

    private NetworkUtils() {
    }

}
