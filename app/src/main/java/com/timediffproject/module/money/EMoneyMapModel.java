package com.timediffproject.module.money;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by melon on 2017/12/13.
 */

public class EMoneyMapModel {

    private final String KEY_COIN_NATION = "coinNation";
    private final String KEY_COIN_NAME = "coinName";
    private final String KEY_VALUE = "value";

    private String coinNation;
    private String coinName;
    private float value;

    public void decode(JSONObject object){
        if (object == null){
            return;
        }

        coinNation = object.getString(KEY_COIN_NATION);
        coinName = object.getString(KEY_COIN_NAME);
        value = object.getFloatValue(KEY_VALUE);

    }

    public String getCoinNation() {
        return coinNation;
    }

    public void setCoinNation(String coinNation) {
        this.coinNation = coinNation;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

}
