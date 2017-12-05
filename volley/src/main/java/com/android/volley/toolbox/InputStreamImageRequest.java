package com.android.volley.toolbox;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by tx on 2015/7/28.
 */
public class InputStreamImageRequest extends Request<InputStream> {

    private final Response.Listener<InputStream> mListener;
    /**
     * Decoding lock so that we don't decode more than one image at a time (to avoid OOM's)
     */
    private static final Object sDecodeLock = new Object();

    public InputStreamImageRequest(String url, Response.Listener<InputStream> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        mListener = listener;
    }

    @Override
    protected Response<InputStream> parseNetworkResponse(NetworkResponse response) {
        // Serialize all decode on a global lock to reduce concurrent heap usage.
        synchronized (sDecodeLock) {
            return doParse(response);
        }
    }

    private Response<InputStream> doParse(NetworkResponse response) {
        byte[] bytes = response.data;
        InputStream inputStream = new ByteArrayInputStream(bytes);
        return Response.success(inputStream, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(InputStream response) {
        this.mListener.onResponse(response);
    }
}
