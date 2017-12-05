package com.timediffproject.network;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by eddy on 2015/4/24.
 */
public interface IRequestCallback {

    public void onResponseSuccess(JSONObject jsonObject);

    public void onResponseSuccess(String str);

    public void onResponseError(int code);

}
