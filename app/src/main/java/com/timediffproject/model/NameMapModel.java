package com.timediffproject.model;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NameMapModel {

    private List<NameItem> nameList = new ArrayList<>();

    public void decode(String jsonStr){
        JSONArray array = JSONArray.parseArray(jsonStr);
        if (array != null){
            for (int i = 0;i < array.size();i++){
                JSONObject obj = array.getJSONObject(i);
                NameItem nameItem = new NameItem();
                nameItem.decode(obj);
                nameList.add(nameItem);
            }
        }
    }

    public static class NameItem{
        private final String KEY_CITY_NAME = "city_name";//"阿比让"
        private final String KEY_NATION_NAME = "nation_name";//"科特迪瓦"
        private final String KEY_CITY_NAME_E = "city_name_e";//"Abidjan"
        private final String KEY_STATE = "state";//"africa"
        private final String KEY_NATION_NAME_E = "nation_name_e";//"Ivory Coast"

        private String cityName;
        private String cityNameE;
        private String nationName;
        private String nationNameE;
        private String state;

        public void decode(JSONObject obj){
            cityName = obj.getString(KEY_CITY_NAME);
            cityNameE = obj.getString(KEY_CITY_NAME_E);
            nationName = obj.getString(KEY_NATION_NAME);
            nationNameE = obj.getString(KEY_NATION_NAME_E);
            state = obj.getString(KEY_STATE);
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getCityNameE() {
            return cityNameE;
        }

        public void setCityNameE(String cityNameE) {
            this.cityNameE = cityNameE;
        }

        public String getNationName() {
            return nationName;
        }

        public void setNationName(String nationName) {
            this.nationName = nationName;
        }

        public String getNationNameE() {
            return nationNameE;
        }

        public void setNationNameE(String nationNameE) {
            this.nationNameE = nationNameE;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }

    public List<NameItem> getNameList() {
        return nameList;
    }

    public void setNameList(List<NameItem> nameList) {
        this.nameList = nameList;
    }
}
