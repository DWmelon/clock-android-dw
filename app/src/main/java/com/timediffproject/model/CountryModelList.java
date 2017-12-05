package com.timediffproject.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by melon on 2017/7/2.
 */

public class CountryModelList extends BaseModel implements Serializable{

    private static final String KEY_COUNTRY_LIST = "countryList";

    private static final long serialVersionUID = 2800161555697996451L;

    private List<CountryModel> countryModelList = new ArrayList<>();


    public void decode(JSONObject object){
        super.decode(object);
        if (object == null){
            return;
        }

        JSONObject data = object.getJSONObject(KEY_DATA);
        if (data == null){
            return;
        }

        JSONArray arr = data.getJSONArray(KEY_COUNTRY_LIST);
        if (arr == null){
            return;
        }

        countryModelList.clear();
        CountryModel model;
        for (int i = 0;i<arr.size();i++){
            JSONObject obj = arr.getJSONObject(i);
            model = new CountryModel();
            model.decode(obj);
            countryModelList.add(model);
        }

    }

    public List<CountryModel> getCountryModelList() {
        return countryModelList;
    }

    public void setCountryModelList(List<CountryModel> countryModelList) {
        this.countryModelList = countryModelList;
    }

    @Override
    public String toString() {
        return "CountryModelList{" +
                "countryModelList=" + countryModelList +
                "} " + super.toString();
    }
}
