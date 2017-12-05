package com.timediffproject.network;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

/**
 * A request for retrieving a T type response body at a given URL that also
 * optionally sends along a JSON body in the request specified.
 *
 * @param <T> JSON type of response expected
 */
public abstract class BaseJsonRequest<T> extends Request<T> {
    /** Default charset for JSON request. */
    protected static final String PROTOCOL_CHARSET = "utf-8";

    /** Content type for request. */
    private static final String PROTOCOL_CONTENT_TYPE =
            String.format("application/json; charset=%s", PROTOCOL_CHARSET);

    private final Response.Listener<T> mListener;
//    private final String mRequestBody;

    /**
     * Deprecated constructor for a JsonRequest which defaults to GET unless {@link #getPostBody()}
     * or {@link #getPostParams()} is overridden (which defaults to POST).
     *
     * @deprecated Use {@link #(int, String , String , Response.Listener, Response.ErrorListener)}.
     */
    public BaseJsonRequest(String url, Response.Listener<T> listener,
                           Response.ErrorListener errorListener) {
        this(Method.DEPRECATED_GET_OR_POST, url, listener, errorListener);
    }

    public BaseJsonRequest(int method, String url, Response.Listener<T> listener,
                           Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
//        mRequestBody = requestBody;
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

    @Override
    abstract protected Response<T> parseNetworkResponse(NetworkResponse response);

    /**
     * @deprecated Use {@link #getBodyContentType()}.
     */
    @Override
    public String getPostBodyContentType() {
        return getBodyContentType();
    }

    /**
//     * @deprecated Use {@link #getBody()}.
//     */
//    @Override
//    public byte[] getPostBody() {
//        return getBody();
//    }

//    @Override
//    public String getBodyContentType() {
//        return PROTOCOL_CONTENT_TYPE;
//    }

//    @Override
//    public byte[] getBody() {
//        try {
//            return mRequestBody == null ? null : mRequestBody.getBytes(PROTOCOL_CHARSET);
//        } catch (UnsupportedEncodingException uee) {
//            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
//                    mRequestBody, PROTOCOL_CHARSET);
//            return null;
//        }
//    }
}
