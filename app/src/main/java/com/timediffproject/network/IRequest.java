package com.timediffproject.network;

import com.android.volley.toolbox.InputStreamImageRequest;

import java.util.Map;

/**
 * Created by eddy on 2015/4/24.
 */
public interface IRequest extends IInterface{

    public enum CacheDir{
        FAV,COMMON_STR;
    }

    public void sendRequestForGet(String url, Map<String, String> param, IRequestCallback callback);
    public void sendRequestForGet(String url, Map<String, String> param, IRequestCallback callback, boolean cache);
    public void sendRequestForGetWithJson(String url, Map<String, String> param, IRequestCallback callback);
    public void sendRequestForGetWithJson(String url, Map<String, String> param, IRequestCallback callback, boolean cache);
    public void sendRequestForGetWithJson(String url, Map<String, String> param, IRequestCallback callback, boolean cache, String token);
    //    public void sendRequestForGet(String url,HashMap<String,String> param,IRequestCallback callback,boolean cache,CacheDir dir);
    public void sendRequestForPost(String url, Map<String, String> param, IRequestCallback callback);
    public void sendRequestForPostWithJson(String url, Map<String, String> param, IRequestCallback callback);
    public void sendImageRequest(InputStreamImageRequest request);



    public void sendRequestForGet(String url, Map<String, String> param, boolean isShowToast, IRequestCallback callback);
    public void sendRequestForGet(String url, Map<String, String> param, boolean isShowToast, IRequestCallback callback, boolean cache);
    public void sendRequestForGetWithJson(String url, Map<String, String> param, boolean isShowToast, IRequestCallback callback);
    public void sendRequestForGetWithJson(String url, Map<String, String> param, boolean isShowToast, IRequestCallback callback, boolean cache);
    public void sendRequestForGetWithJson(String url, Map<String, String> param, boolean isShowToast, IRequestCallback callback, boolean cache, String token);
    public void sendRequestForPost(String url, Map<String, String> param, boolean isShowToast, IRequestCallback callback);
    public void sendRequestForPostWithJson(String url, Map<String, String> param, boolean isShowToast, IRequestCallback callback);

}
