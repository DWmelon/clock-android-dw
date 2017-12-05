package com.timediffproject.module.search;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.timediffproject.application.BaseActivity;
import com.timediffproject.application.MyClient;
import com.timediffproject.model.CountryModel;
import com.timediffproject.model.CountryModelList;
import com.timediffproject.module.select.OnGetCountryByStateListener;
import com.timediffproject.network.IRequest;
import com.timediffproject.network.IRequestCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by melon on 2017/10/2.
 */

public class SearchManager {

    private static final String URL_SEARCH_COUNTRY = "http://119.23.222.106/timediff/country/search";

    public void searchCountry(String keyword, final OnSearchCountryListener listener) {
        HashMap<String,String> map = new HashMap<>();
        map.put("keyword",keyword);

        final IRequest request = (IRequest) MyClient.getMyClient().getService(MyClient.SERVICE_HTTP_REQUEST);
        request.sendRequestForPostWithJson(URL_SEARCH_COUNTRY, map, new IRequestCallback() {
            @Override
            public void onResponseSuccess(JSONObject jsonObject) {
                if (listener == null) {
                    return;
                }

                if (jsonObject == null) {
                    listener.onSearchCountryFinish(new ArrayList<CountryModel>());
                    return;
                }

                CountryModelList model = new CountryModelList();
                model.decode(jsonObject);

                if (model.getCode() == 0){
                    sortCountry(model.getCountryModelList());
                    listener.onSearchCountryFinish(model.getCountryModelList());
                }else{
                    listener.onSearchCountryFinish(new ArrayList<CountryModel>());
                }

            }

            @Override
            public void onResponseSuccess(String str) {

            }

            @Override
            public void onResponseError(int code) {
                if (listener != null) {
                    listener.onSearchCountryFinish(new ArrayList<CountryModel>());
                }
            }
        });


    }

    private void sortCountry(List<CountryModel> modelList){
        Collections.sort(modelList, new Comparator<CountryModel>() {
            @Override
            public int compare(CountryModel lhs, CountryModel rhs) {
                return lhs.getNationNamePy().compareTo(rhs.getNationNamePy());
            }
        });

    }

}
