package com.timediffproject.network2;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.timediffproject.constants.Constant;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {

    private volatile static RetrofitManager retrofitManager;
    private Retrofit retrofit;
    private OkHttpClient okHttpClient;

    public static RetrofitManager getInstant(){
        if (retrofitManager == null){
            synchronized(RetrofitManager.class){
                if (retrofitManager == null){
                    retrofitManager = new RetrofitManager();
                }
            }
        }
        return retrofitManager;
    }

    private RetrofitManager(){
        okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Interceptor.Chain chain) throws IOException {
//                        Request original = chain.request();
//                        Request request = original.newBuilder()
//                                .addHeader("Content-Type", "application/json;charset=UTF-8")
//                                .method(original.method(), original.body())
//                                .build();
//
//                        return chain.proceed(request);
//                    }
//                })
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.MAIN_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public Retrofit getRetrofit(){
        return retrofit;
    }

    public void requestTest(HashMap<String,String> map){
        MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");

        JSONObject object = new JSONObject();
        for (String key : map.keySet()){
            object.put(key,map.get(key));
        }
        String requestBody = object.toJSONString();

        Request request = new Request.Builder()
                .url("http://119.23.222.106/timediff/exchange_rate/list/nation")
                .post(RequestBody.create(mediaType, requestBody))
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

}
