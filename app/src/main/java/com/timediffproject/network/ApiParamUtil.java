package com.timediffproject.network;

import android.text.TextUtils;

import com.timediffproject.origin.MainApplication;
import com.timediffproject.util.DeviceInfoManager;
import com.timediffproject.util.NetworkUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 根据不同的接口使用不同的参数配置 上cdn来降低服务器压力
 * Created by eddy on 2015/4/22.
 */
public class ApiParamUtil {

    public static final int FLAG_BASE = 1;
    public static final int FLAG_STAT = 2;
    public static final int FLAG_UDPATE = 4;


    private static String checkUrlSuffix(String url) {
        if (TextUtils.isEmpty(url)) {
            return url;
        }

        int index = url.lastIndexOf("?");
        if (index == -1) {
            //整个链接没有?
            if (url.endsWith("?")) {
                return url;
            } else {
                return url + "?";
            }
        } else {

            if (url.endsWith("?")) {
                return url;
            } else if (url.endsWith("&")) {
                return url;
            } else {
                return url + "&";
            }
        }

    }

    public static String filterUrl(String url){

        if (TextUtils.isEmpty(url)){
            return url;
        }

        int index = url.indexOf("?");

        if (index == -1){
            //整个链接没有问号

            index = url.indexOf("&");

            if (index == -1){
                return url;
            }else {
                return url.substring(0,index);
            }

        }else {

            String tmpUrl = url.substring(0,index);
            return tmpUrl;
        }



    }

    /**
     * 是否为官方链接
     * @param url
     * @return
     */
    public static boolean isOfficalUrl(String url){
        if (TextUtils.isEmpty(url)){
            return false;
        }

        return url.contains("wmzy.com");

    }

    public static String getUrlId(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }

        int index = url.lastIndexOf("/");
        if (index == -1) {
            return url;
        }
//        Log.d("debug","url = "+url +"   data id = "+url.substring(index+1,url.length()-1));
        return url.substring(index + 1, url.length() - 1);
    }

    /**
     * 重新处理链接的参数，保证所有参数唯一
     *
     * @param url
     * @return
     */
    public static String filterParamOnlyOne(String url) {
        if (TextUtils.isEmpty(url)) {
            return url;
        }

        String baseUrl = getBaseUrl(url);
//        Log.d(Log.TAG,"base Url "+baseUrl);
        Map<String, String> map = getParamFromUrl(url);
        return wrappeUrlParam(baseUrl, map);

    }

    private static String getBaseUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return url;
        }

        int index = url.indexOf("?");
        return url.substring(0, index + 1);

    }

    private static Map<String, String> getParamFromUrl(String url) {

        Map<String, String> map = new HashMap<>();

        if (TextUtils.isEmpty(url)) {
            return map;
        }

        int index = url.indexOf("?");
        String param = url.substring(index + 1);
        if (TextUtils.isEmpty(param)) {
            return map;
        }

        String[] listStr = param.split("&");
        String key;
        String value;

        for (String str : listStr) {
            int i = str.indexOf("=");
            key = str.substring(0, i);
            value = str.substring(i + 1);
//            Log.d(Log.TAG,"key = "+key+" value = "+value);
            if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                map.put(key, value);
            }
        }

        return map;
    }


    public static String replaceToTest(String originUrl) {
        if (TextUtils.isEmpty(originUrl)) {
            return null;
        }

        if (originUrl.contains("api2")) {
            //api2接口不替换
            return originUrl;
        }

        return originUrl.replace(UrlConstant.NEW_DOMAIN_URL_V2, UrlConstant.TEST_URL);
    }

    public static String replaceToPrePublic(String originUrl) {
        if (TextUtils.isEmpty(originUrl)) {
            return null;
        }

        if (originUrl.contains("api2")) {
            //api2接口不替换
            return originUrl;
        }

        return originUrl.replace(UrlConstant.NEW_DOMAIN_URL_V2, UrlConstant.TEST_PRE_URL);
    }

    public static String wrappeUrlParamUnEncode(String url, Map<String, String> map) {

        if (TextUtils.isEmpty(url)) {
            return null;
        }

        if (map == null || map.isEmpty()) {
            return url;
        }

        url = checkUrlSuffix(url);

        StringBuilder sb = new StringBuilder(url);
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            String value = map.get(key);
            sb.append(key);
            sb.append("=");
            sb.append(value);
            sb.append("&");
        }
        String retStr = sb.toString();
        retStr = retStr.substring(0, retStr.length() - 1);
//        Log.d(Log.TAG_REQUEST, "ApiParamUtil#wrappeUrlParam retStr : " + retStr);
        return retStr;
    }

    public static String setupCookiesParam(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (String key : map.keySet()) {
            builder.append(key);
            builder.append("=");
            builder.append(map.get(key));
            builder.append(",");
        }
        String result = builder.toString();
        return result.substring(0, result.length() - 1);
    }

    public static String wrappeUrlParam(String url, Map<String, String> map) {

        if (TextUtils.isEmpty(url)) {
            return null;
        }

        if (map == null || map.isEmpty()) {
            return url;
        }

        map = ApiParamUtil.wrappeBaseParam(map);

        url = checkUrlSuffix(url);

        StringBuilder sb = new StringBuilder(url);
        sb.append(encodeParameters(map, "UTF-8"));
        String retStr = sb.toString();
        retStr = retStr.substring(0, retStr.length() - 1);
//        Log.d(Log.TAG_REQUEST, "ApiParamUtil#wrappeUrlParam retStr : " + retStr);
        return retStr;
    }


    public static Map<String, String> wrappeBaseParam(Map<String, String> map) {

//        Map<String, String> retMap = getBaseParam();
        Map<String, String> retMap = new HashMap<>();
        if (map == null || map.isEmpty()) {
            return retMap;
        }


        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            String value = map.get(key);
            if (!TextUtils.isEmpty(key)) {
                if (TextUtils.isEmpty(value)) {
                    value = "";
                }
                retMap.put(key, value);
            }
        }


        return retMap;
    }


    public static String encodeParameters(Map<String, String> params, String paramsEncoding) {
        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
                encodedParams.append('=');
                if (entry.getValue() != null) {
                    encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
                } else {
                    encodedParams.append("");
                }
                encodedParams.append('&');

            }
            return encodedParams.toString();
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
        }
    }


    //TODO 未确定具体参数
    private static HashMap<String, String> getParamMap(int flag) {

        HashMap<String, String> ret = new HashMap<String, String>();
        if ((flag & FLAG_BASE) != 0) {
            ret.putAll(getBaseParam());
        }

        if ((flag & FLAG_STAT) != 0) {
            ret.putAll(getStatParam());
        }

        return ret;
    }

    public static HashMap<String, String> getBaseParam() {
        HashMap<String, String> mDaemonMap = new HashMap<>();
        mDaemonMap.put(UrlConstant.PARAM_KEY_APP_ID, DeviceInfoManager.APP_ID);
        mDaemonMap.put(UrlConstant.PARAM_KEY_PLATFORM, DeviceInfoManager.APP_PLATFORM);
        mDaemonMap.put(UrlConstant.PARAM_KEY_INTERFACE_VERSION, DeviceInfoManager.INTERFACE_VERSION);
        mDaemonMap.put(UrlConstant.PARAM_KEY_DEVICE_UID, DeviceInfoManager.mDeviceUid);
        if (!TextUtils.isEmpty(DeviceInfoManager.CHANNEL)) {
            mDaemonMap.put(UrlConstant.PARAM_KEY_CHANNEL, DeviceInfoManager.CHANNEL);
        }
        mDaemonMap.put(UrlConstant.PARAM_KEY_VERSION_CODE, String.valueOf(DeviceInfoManager.VERSION_CODE));
        mDaemonMap.put(UrlConstant.PARAM_KEY_VERSION_NAME, String.valueOf(DeviceInfoManager.VERSION_NAME));
        mDaemonMap.put(UrlConstant.PARAM_KEY_BRAND, String.valueOf(DeviceInfoManager.BRAND + " " + DeviceInfoManager.MODEL));
        mDaemonMap.put(UrlConstant.PARAM_KEY_NET_TYPE, NetworkUtils.getMobileNetWrokTypeName(MainApplication.getContext()));
        mDaemonMap.put(UrlConstant.PARAM_KEY_SYS_VERSION, DeviceInfoManager.SYS_VERSION);
        return mDaemonMap;
    }

    public static HashMap<String, String> getBaseParamWithoutLocation() {
        HashMap<String, String> mDaemonMap = new HashMap<>();
        mDaemonMap.put(UrlConstant.PARAM_KEY_APP_ID, DeviceInfoManager.APP_ID);
        mDaemonMap.put(UrlConstant.PARAM_KEY_PLATFORM, DeviceInfoManager.APP_PLATFORM);

        mDaemonMap.put(UrlConstant.PARAM_KEY_VERSION_CODE, String.valueOf(DeviceInfoManager.VERSION_CODE));
        return mDaemonMap;
    }

    public static HashMap<String, String> getWebParam() {
        HashMap<String, String> map = getBaseParam();
        map.put(UrlConstant.PARAM_KEY_CLEAR_VISIT_HISIORY, "true");
        return map;
    }


    private static HashMap<String, String> getStatParam() {
        HashMap<String, String> mDaemonMap = new HashMap<>();
        return mDaemonMap;
    }

    public static HashMap<String, String> getUpdateParam() {
        return getBaseParam();
    }

    /**
     * 添加浙江版需要的分数
     */
//    public static Map<String, String> wrapZheJiangScore(Map<String, String> map) {
//        if (map == null) return null;
//        String examProvince = IpinClient.getInstance().getAccountManager().getExamProvince();
//        if (IpinApplication.getContext().getString(R.string.prov_zhejiang).equals(examProvince)) {
//            AccountManager accountManager = IpinClient.getInstance().getAccountManager();
//            int baseScore = accountManager.getBaseScore();
//            int generalScore = accountManager.getGeneralScore();
//            int optionScore = accountManager.getOptionScore();
//            int techScore = accountManager.getTechScore();
//            map.put(UrlConstant.PARAM_KEY_BASE_SCORE, String.valueOf(baseScore));
//            map.put(UrlConstant.PARAM_KEY_OPTIONAL_SCORE, String.valueOf(optionScore));
//            map.put(UrlConstant.PARAM_KEY_GENERAL_SCORE, String.valueOf(generalScore));
//            map.put(UrlConstant.PARAM_KEY_TECH_SCORE, String.valueOf(techScore));
//            map.put(UrlConstant.PARAM_KEY_SCORE_TYPE, String.valueOf(accountManager.getScoreType()));
//            map.put(UrlConstant.PARAM_KEY_SCORE_RANK, String.valueOf(accountManager.getScoreRank()));
//        }
//        return map;
//    }

    /**
     * 添加浙江版需要的分数
     * 根据传入字段
     */
//    public static Map<String, String> wrapZheJiangScore(Map<String, String> map, int mBaseScore, int mGeneralScore, int mTechScore, int mOptionalScore) {
//        if (map == null) return null;
//
//        map.put(UrlConstant.PARAM_KEY_BASE_SCORE, String.valueOf(mBaseScore));
//        map.put(UrlConstant.PARAM_KEY_OPTIONAL_SCORE, String.valueOf(mOptionalScore));
//        map.put(UrlConstant.PARAM_KEY_GENERAL_SCORE, String.valueOf(mGeneralScore));
//        map.put(UrlConstant.PARAM_KEY_TECH_SCORE, String.valueOf(mTechScore));
//
//        return map;
//    }

    public static HashMap<String,String> addBasicParam(){
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(UrlConstant.PARAM_KEY_VERSION_CODE, String.valueOf(DeviceInfoManager.VERSION_CODE));
        map.put(UrlConstant.PARAM_KEY_PLATFORM, DeviceInfoManager.APP_PLATFORM);
        map.put(UrlConstant.PARAM_KEY_APP_ID, DeviceInfoManager.APP_ID);
        return map;
    }


}
