package com.timediffproject.model;

import com.alibaba.fastjson.JSONObject;
import com.timediffproject.util.CharUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by melon on 2017/1/5.
 */

public class CountryModel implements Serializable {

    private static final long serialVersionUID = 7362593594884248993L;

    private static final String ID = "id";

    private static final String CITY_NAME = "cityName";

    private static final String CITY_NAME_E = "cityNameE";

    private static final String NATION_NAME = "nationName";

    private static final String FIRST_CHAR = "firstChar";

    private static final String DIFF_TIME = "diffTime";

    private static final String LOGO = "logo";

    private static final String STATE = "state";

    private static final String SAVE_TIME_AROUND = "saveTimeAround";

    private static final String ZONE_NUM = "zoneNum";

    private static final String COIN_NAME = "coinName";

    private Long id;

    private String cityName;

    private String cityNameE;

    private String nationName;

    private String firstChar;

    private String nationNamePy;

    private boolean isFirstData;

    private float diffTime;

    private String logo;

    private Date nowDate;

    private String state;

    private String extra;

    private String saveTimeAround;

    private String zoneNum;

    private String coinName;

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getZoneNum() {
        return zoneNum;
    }

    public void setZoneNum(String zoneNum) {
        this.zoneNum = zoneNum;
    }

    public String getCityNameE() {
        return cityNameE;
    }

    public void setCityNameE(String cityNameE) {
        this.cityNameE = cityNameE;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getNationName() {
        return nationName;
    }

    public void setNationName(String nationName) {
        this.nationName = nationName;
    }

    public float getDiffTime() {
        return diffTime;
    }

    public void setDiffTime(float diffTime) {
        this.diffTime = diffTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getFirstChar() {
        return firstChar;
    }

    public void setFirstChar(String firstChar) {
        this.firstChar = firstChar;
    }

    public String getNationNamePy() {
        return nationNamePy;
    }

    public void setNationNamePy(String nationNamePy) {
        this.nationNamePy = nationNamePy;
        if (nationNamePy!=null && !nationNamePy.isEmpty()){
            this.firstChar = nationNamePy.substring(0,1).toUpperCase();
        }
    }

    public boolean isFirstData() {
        return isFirstData;
    }

    public void setFirstData(boolean firstData) {
        isFirstData = firstData;
    }

    public Date getNowDate() {
        return nowDate;
    }

    public void setNowDate(Date nowDate) {
        this.nowDate = nowDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getSaveTimeAround() {
        return saveTimeAround;
    }

    public void setSaveTimeAround(String saveTimeAround) {
        this.saveTimeAround = saveTimeAround;
    }

    public void decode(JSONObject object){
        id = object.getLongValue(ID);
        cityName = object.getString(CITY_NAME);
        cityNameE = object.getString(CITY_NAME_E);
        nationName = object.getString(NATION_NAME);
        setNationNamePy(CharUtil.cn2py(nationName));
        diffTime = object.getFloat(DIFF_TIME);
        logo = object.getString(LOGO);
        state = object.getString(STATE);
        extra = object.getString(SAVE_TIME_AROUND);
        zoneNum = object.getString(ZONE_NUM);
        coinName = object.getString(COIN_NAME);
    }

    @Override
    public String toString() {
        return "CountryModel{" +
                "id=" + id +
                ", cityName='" + cityName + '\'' +
                ", cityNameE='" + cityNameE + '\'' +
                ", nationName='" + nationName + '\'' +
                ", firstChar='" + firstChar + '\'' +
                ", nationNamePy='" + nationNamePy + '\'' +
                ", isFirstData=" + isFirstData +
                ", diffTime=" + diffTime +
                ", logo='" + logo + '\'' +
                ", nowDate=" + nowDate +
                ", state='" + state + '\'' +
                ", extra='" + extra + '\'' +
                ", saveTimeAround='" + saveTimeAround + '\'' +
                ", zoneNum='" + zoneNum + '\'' +
                ", coinName='" + coinName + '\'' +
                '}';
    }

    @Override
    public Object clone(){
        CountryModel model = new CountryModel();
        model.setId(id);
        model.setCityName(cityName);
        model.setCityNameE(cityNameE);
        model.setNationName(nationName);
        model.setFirstChar(firstChar);
        model.setNationNamePy(nationNamePy);
        model.setFirstData(isFirstData);
        model.setDiffTime(diffTime);
        model.setLogo(logo);
        model.setNowDate(nowDate);
        model.setState(state);
        model.setZoneNum(zoneNum);
        model.setCoinName(coinName);
        return model;
    }
}
