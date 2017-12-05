package com.timediffproject.network;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by leihongshi on 15/7/25.
 */
public class TokenStringRequestJsonResult extends StringRequestJsonResult {

    private String mToken;

    public TokenStringRequestJsonResult(int method, String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }


    public TokenStringRequestJsonResult(int method, String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, String token) {
        super(method, url, listener, errorListener);
        this.mToken = token;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (TextUtils.isEmpty(mToken)) {
            return super.getHeaders();
        }
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Cookie", "token=" + mToken);
        return super.getHeaders();
    }

}
