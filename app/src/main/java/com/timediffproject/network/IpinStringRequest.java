package com.timediffproject.network;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by leihongshi on 15/5/21.
 */
public class IpinStringRequest extends StringRequest {

    private String mToken;

    public IpinStringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public IpinStringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener, String token) {
        super(method, url, listener, errorListener);
        this.mToken = token;

    }

    public IpinStringRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    public IpinStringRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener, String token) {
        super(url, listener, errorListener);
        this.mToken = token;

    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {

        if(TextUtils.isEmpty(mToken)){
            return super.getHeaders();
        }
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Cookie", "token="+mToken);

        return super.getHeaders();
    }



}
