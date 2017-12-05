//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.timediffproject.network;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.InputStreamImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.timediffproject.application.MyClient;
import com.timediffproject.origin.MainApplication;
import com.timediffproject.storage.StorageManager;
import com.timediffproject.util.NetworkUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HttpRequestManager implements IRequest {
    public static final int TYPE_STR = 0;
    public static final int TYPE_FAV = 1;
    private static HttpRequestManager mIntance;
    private RequestQueue mRequestQueue;
    private Context mContext;

    public HttpRequestManager(Context context) {
        this.init(context);
    }

    public void init(Context context) {
        this.mContext = context.getApplicationContext();
        File strCacheDir = new File(StorageManager.getStrCachePath());
        this.mRequestQueue = Volley.newRequestQueue(this.mContext, (HttpStack) null, strCacheDir);
    }

    private StringRequest requestForString(String url, int method, final boolean isShowToast, final IRequestCallback callback, boolean cache) {


        final String finalUrl = url;
        IpinStringRequest request = new IpinStringRequest(method, url, new Response.Listener<String>() {
            public void onResponse(String response) {

                if (callback != null) {
                    callback.onResponseSuccess(response);
                }

            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    String msg = error.getMessage();
                    if (TextUtils.isEmpty(msg)) {
                        msg = error.getClass().getName();
                    }

                    if (error.networkResponse != null) {
                        msg = msg + ":" + error.networkResponse.statusCode;
                    }

                    HashMap map = new HashMap();
                    map.put("http_fail_msg", msg);
                    map.put("http_fail_data_id", finalUrl);
                    map.put("http_fail_time", String.valueOf(error.getNetworkTimeMs()));
//                    StatManager.statEventNum(HttpRequestManager.this.mContext, "http_fail", map);
                }


                if (callback != null) {
                    if (error.networkResponse != null) {
                        callback.onResponseError(error.networkResponse.statusCode);
                    } else {
                        callback.onResponseError(-1);
                    }
                }

            }
        }, "");
        request.setShouldCache(cache);
        return request;
    }

    private Request requestForJson(String url, int method, final boolean isShowToast, final IRequestCallback callback, boolean cache, String token) {


        Object request = null;
        if (!TextUtils.isEmpty(token)) {
            final String finalUrl = url;
            request = new TokenStringRequestJsonResult(method, url, new Response.Listener<JSONObject>() {
                public void onResponse(JSONObject response) {

                    if (callback != null) {
                        callback.onResponseSuccess(response);
                    }

                }
            }, new ErrorListener() {
                public void onErrorResponse(VolleyError error) {
                    if (error != null) {
                        String msg = error.getMessage();
                        if (TextUtils.isEmpty(msg)) {
                            msg = error.getClass().getName();
                        }

                        if (error.networkResponse != null) {
                            msg = msg + ":" + error.networkResponse.statusCode;
                        }

                        HashMap map = new HashMap();
                        map.put("http_fail_msg", msg);
                        map.put("http_fail_data_id", finalUrl);
                        map.put("http_fail_time", String.valueOf(error.getNetworkTimeMs()));
//                        StatManager.statEventNum(HttpRequestManager.this.mContext, "http_fail", map);
                    }

                    if (callback != null) {
                        if (error.networkResponse != null) {
                            callback.onResponseError(error.networkResponse.statusCode);
                        } else {
                            callback.onResponseError(-1);
                        }
                    }

                }
            }, token);
        } else {
            request = new StringRequestJsonResult(method, url, new Response.Listener<JSONObject>() {
                public void onResponse(JSONObject response) {

                    if (callback != null) {
                        callback.onResponseSuccess(response);
                    }

                }
            }, new ErrorListener() {
                public void onErrorResponse(VolleyError error) {
                    if (error != null) {
                        String msg = error.getMessage();
                        if (TextUtils.isEmpty(msg)) {
                            msg = error.getClass().getName();
                        }

                        if (error.networkResponse != null) {
                            msg = msg + ":" + error.networkResponse.statusCode;
                        }

                        HashMap map = new HashMap();
                        map.put("http_fail_msg", msg);
                        map.put("http_fail_time", String.valueOf(error.getNetworkTimeMs()));
//                        StatManager.statEventNum(HttpRequestManager.this.mContext, "http_fail", map);
                    }

                    if (callback != null) {
                        callback.onResponseError(error.networkResponse == null ? -1 : error.networkResponse.statusCode);
                    }

                }
            });
        }

        ((Request) request).setShouldCache(cache);
        return (Request) request;
    }

    private void doAddQueue(StringRequest request, CacheDir dir) {
        this.mRequestQueue.add(request);
    }

    private void doAddQueue(Request request) {
        this.mRequestQueue.add(request);
    }

    public void sendRequestForGet(String url, Map<String, String> param, IRequestCallback callback) {
        this.sendRequestForGet(url, param, true, callback, false);
    }

    public void sendRequestForGet(String url, Map<String, String> param, boolean isShowToast, IRequestCallback callback) {
        this.sendRequestForGet(url, param, isShowToast, callback, false);
    }

    public void sendRequestForGet(String url, Map<String, String> param, IRequestCallback callback, boolean cache) {
        this.sendRequestForGet(url, param, true, callback, cache);
    }

    public void sendRequestForGet(String url, Map<String, String> param, boolean isShowToast, IRequestCallback callback, boolean cache) {
//        String tagertUrl = ApiParamUtil.wrappeUrlParam(url, param);
        this.doAddQueue(this.requestForString(url, 0, isShowToast, callback, cache), CacheDir.COMMON_STR);
    }

    public void sendRequestForGetWithJson(String url, Map<String, String> param, IRequestCallback callback) {
        this.sendRequestForGetWithJson(url, param, true, callback, false, (String) null);
    }

    public void sendRequestForGetWithJson(String url, Map<String, String> param, boolean isShowToast, IRequestCallback callback) {
        this.sendRequestForGetWithJson(url, param, isShowToast, callback, false, (String) null);
    }

    public void sendRequestForGetWithJson(String url, Map<String, String> param, IRequestCallback callback, boolean cache) {
        this.sendRequestForGetWithJson(url, param, true, callback, cache, (String) null);
    }

    public void sendRequestForGetWithJson(String url, Map<String, String> param, boolean isShowToast, IRequestCallback callback, boolean cache) {
        this.sendRequestForGetWithJson(url, param, isShowToast, callback, cache, (String) null);
    }

    public void sendRequestForGetWithJson(String url, Map<String, String> param, IRequestCallback callback, boolean cache, String token) {
        this.sendRequestForGetWithJson(url, param, true, callback, cache, token);
    }

    public void sendRequestForGetWithJson(String url, Map<String, String> param, boolean isShowToast, IRequestCallback callback, boolean cache, String token) {
        this.doAddQueue(this.requestForJson(url, 0, isShowToast, callback, cache, token));
    }

    public void sendRequestForPost(String url, Map<String, String> param, IRequestCallback callback) {
        this.sendRequestForPost(url, param, true, callback);
    }

    public void sendRequestForPost(String url, Map<String, String> param, boolean isShowToast, IRequestCallback callback) {
        this.doAddQueue(this.requestForPost(url, param, isShowToast, callback), CacheDir.COMMON_STR);
    }

    public void sendRequestForPostWithJson(String url, Map<String, String> param, IRequestCallback callback) {
        this.sendRequestForPostWithJson(url, param, true, callback);
    }

    public void sendRequestForPostWithJson(String url, Map<String, String> param, boolean isShowToast, IRequestCallback callback) {
        this.doAddQueue(this.requestForPostWithJson(url, param, isShowToast, callback));
    }

    public void sendImageRequest(InputStreamImageRequest request) {
        this.doAddQueue(request);
    }

    private StringRequest requestForPost(String url, final Map<String, String> param, final boolean isShowToast, final IRequestCallback callback) {


        final String finalUrl = url;
        StringRequest request = new StringRequest(1, url, new Response.Listener<String>() {
            public void onResponse(String response) {

                if (callback != null) {
                    callback.onResponseSuccess(response);
                }

            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {

                if (error != null) {
                    String msg = error.getMessage();
                    if (TextUtils.isEmpty(msg)) {
                        msg = error.getClass().getName();
                    }

                    if (error.networkResponse != null) {
                        msg = msg + ":" + error.networkResponse.statusCode;
                    }

                    HashMap map = new HashMap();
                    map.put("http_fail_msg", msg);
                    map.put("http_fail_data_id", finalUrl);
                    map.put("http_fail_time", String.valueOf(error.getNetworkTimeMs()));
//                    StatManager.statEventNum(HttpRequestManager.this.mContext, "http_fail", map);
                }

                if (callback != null) {
                    if (error.networkResponse != null) {
                        callback.onResponseError(error.networkResponse.statusCode);
                    } else {
                        callback.onResponseError(-1);
                    }
                }

            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                return param;
            }
        };
        request.setShouldCache(false);
        return request;
    }

    private Request requestForPostWithJson(String url, final Map<String, String> param, final boolean isShowToast, final IRequestCallback callback) {



        final String finalUrl = url;

//        final Map wrappMap = ApiParamUtil.wrappeBaseParam(param);

        StringRequestJsonResult request = new StringRequestJsonResult(1,url, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {

                if (callback != null) {
                    callback.onResponseSuccess(response);
                }

            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {

                if (error != null) {
                    String msg = error.getMessage();
                    HashMap map = new HashMap();
                    if (TextUtils.isEmpty(msg)) {
                        msg = error.getClass().getName();
                    }

                    if (error.networkResponse != null) {
                        msg = msg + ":" + error.networkResponse.statusCode;
                    }

                    map.put("http_fail_msg", msg);
                    map.put("http_fail_data_id", finalUrl);
                    map.put("http_fail_time", String.valueOf(error.getNetworkTimeMs()));
//                    StatManager.statEventNum(HttpRequestManager.this.mContext, "http_fail", map);
                }

                if (callback != null) {
                    if (error.networkResponse != null) {
                        callback.onResponseError(error.networkResponse.statusCode);
                    } else {
                        callback.onResponseError(-1);
                    }
                }

            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                return param;
            }
        };
        request.setShouldCache(false);
        return request;
    }

    private Map<String, String> checkAndRejectNullParam(Map<String, String> param) {
        HashMap retMap = new HashMap();
        if (param == null) {
            return retMap;
        } else {
            Iterator it = param.keySet().iterator();

            while (it.hasNext()) {
                String key = (String) it.next();
                String value = (String) param.get(key);
                if (!TextUtils.isEmpty(key)) {
                    if (TextUtils.isEmpty(value)) {
                        retMap.put(key, "");
                    } else {
                        retMap.put(key, value);
                    }
                }
            }

            return retMap;
        }
    }

    public void release() {
        if (this.mRequestQueue != null) {
            this.mRequestQueue.stop();
        }

    }
}
