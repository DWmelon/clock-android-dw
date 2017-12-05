package com.timediffproject.module.tempdata;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.timediffproject.application.MyClient;
import com.timediffproject.model.CountryModel;
import com.timediffproject.network.IRequest;
import com.timediffproject.network.IRequestCallback;
import com.timediffproject.util.FileUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by melon on 2017/6/26.
 */

public class DataManager {

    private static final String URL_UPLOAD = "http://192.168.2.156:12344/data/country/update";

    private static final String DATA_EUROPE = "europe.txt";
    private static final String DATA_ASIA = "asia.txt";
    private static final String DATA_AFRICA = "africa.txt";
    private static final String DATA_AMERICA = "america.txt";
    private static final String DATA_OCEANIA = "oceania.txt";

    private List<CountryModel> countryModelList = new ArrayList<>();

    private Context context;

    public void init(Context context){
        this.context = context;
//        loadData();
    }

    private void loadData(){
        countryModelList = new ArrayList<>();
        try {
            InputStream in0 = context.getAssets().open(DATA_EUROPE);
            String text0 = FileUtil.readStringFromInputStream(in0);

            InputStream in1 = context.getAssets().open(DATA_AFRICA);
            String text1 = FileUtil.readStringFromInputStream(in1);

            InputStream in2 = context.getAssets().open(DATA_AMERICA);
            String text2 = FileUtil.readStringFromInputStream(in2);

            InputStream in3 = context.getAssets().open(DATA_ASIA);
            String text3 = FileUtil.readStringFromInputStream(in3);

            InputStream in4 = context.getAssets().open(DATA_OCEANIA);
            String text4 = FileUtil.readStringFromInputStream(in4);


            if (!TextUtils.isEmpty(text0)) {
                JSONArray jsonArray = JSONArray.parseArray(text0);

                for (int i = 0;i<jsonArray.size();i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    CountryModel model = new CountryModel();
                    model.decode(object);
                    model.setState("europe");
                    countryModelList.add(model);

                }
            }


            if (!TextUtils.isEmpty(text1)) {
                JSONArray jsonArray1 = JSONArray.parseArray(text1);

                for (int i = 0;i<jsonArray1.size();i++){
                    JSONObject object = jsonArray1.getJSONObject(i);
                    CountryModel model = new CountryModel();
                    model.decode(object);
                    model.setState("africa");
                    countryModelList.add(model);
                }
            }

            if (!TextUtils.isEmpty(text2)) {
                JSONArray jsonArray = JSONArray.parseArray(text2);

                for (int i = 0;i<jsonArray.size();i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    CountryModel model = new CountryModel();
                    model.decode(object);
                    model.setState("america");
                    countryModelList.add(model);
                }
            }

            if (!TextUtils.isEmpty(text3)) {
                JSONArray jsonArray = JSONArray.parseArray(text3);

                for (int i = 0;i<jsonArray.size();i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    CountryModel model = new CountryModel();
                    model.decode(object);
                    model.setState("asia");
                    countryModelList.add(model);
                }
            }

            if (!TextUtils.isEmpty(text4)) {
                JSONArray jsonArray = JSONArray.parseArray(text4);

                for (int i = 0;i<jsonArray.size();i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    CountryModel model = new CountryModel();
                    model.decode(object);
                    model.setState("oceania");
                    countryModelList.add(model);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<CountryModel> getCountryList(){
        return countryModelList;
    }

    public void uploadCountry(CountryModel model, final OnDataFinishListener listener) {

        final IRequest request = (IRequest) MyClient.getMyClient().getService(MyClient.SERVICE_HTTP_REQUEST);
        if (request == null) {
            return;
        }

        HashMap<String,String> map = new HashMap<>();

        map.put("cityNameE",String.valueOf(model.getId()));
        map.put("cityName",model.getCityName());
        map.put("nationName",model.getNationName());
        map.put("diffTime",String.valueOf(model.getDiffTime()));
        map.put("logo",model.getLogo());
        map.put("state",model.getState());

        request.sendRequestForPostWithJson(URL_UPLOAD, map, new IRequestCallback() {
            @Override
            public void onResponseSuccess(JSONObject jsonObject) {
                if (listener == null) {
                    return;
                }

                if (jsonObject == null) {
                    listener.onHandleDataFinish();
                    return;
                }

                listener.onHandleDataFinish();

            }

            @Override
            public void onResponseSuccess(String str) {

            }

            @Override
            public void onResponseError(int code) {
                if (listener != null) {
                    listener.onHandleDataFinish();
                }
            }
        });


    }

    public void updateDiffTime(CountryModel model, final OnDataFinishListener listener) {

        final IRequest request = (IRequest) MyClient.getMyClient().getService(MyClient.SERVICE_HTTP_REQUEST);
        if (request == null) {
            return;
        }

        HashMap<String,String> map = new HashMap<>();
        map.put("name",model.getCityName());
        map.put("saveTimeAround",model.getExtra());


        request.sendRequestForPostWithJson(URL_UPLOAD, map, new IRequestCallback() {
            @Override
            public void onResponseSuccess(JSONObject jsonObject) {
                if (listener == null) {
                    return;
                }

                if (jsonObject == null) {
                    listener.onHandleDataFinish();
                    return;
                }

                listener.onHandleDataFinish();

            }

            @Override
            public void onResponseSuccess(String str) {

            }

            @Override
            public void onResponseError(int code) {
                if (listener != null) {
                    listener.onHandleDataFinish();
                }
            }
        });


    }

}
