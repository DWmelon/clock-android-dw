package com.timediffproject.module.money;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.timediffproject.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by melon on 2017/12/13.
 */

public class EMoneyResultModel extends BaseModel {

    private final String KEY_MONEY_LIST = "moneyRatioList";
    private final String KEY_SOURCE_NAME = "sourceNation";
    private final String KEY_SOURCE_COIN = "sourceCoin";

    private List<EMoneyMapModel> modelList = new ArrayList<>();
    private String sourceNation;
    private String sourceCoin;

    public void decode(JSONObject object){
        super.decode(object);
        if (object == null){
            return;
        }

        object = object.getJSONObject(KEY_DATA);
        if (object == null){
            return;
        }

        sourceNation = object.getString(KEY_SOURCE_NAME);
        sourceCoin = object.getString(KEY_SOURCE_COIN);

        JSONArray array = object.getJSONArray(KEY_MONEY_LIST);
        if (array != null){
            modelList.clear();
            for (int i = 0;i < array.size();i++){
                EMoneyMapModel model = new EMoneyMapModel();
                model.decode(array.getJSONObject(i));
                modelList.add(model);
            }
        }

    }

    public String getSourceCoin() {
        return sourceCoin;
    }

    public void setSourceCoin(String sourceCoin) {
        this.sourceCoin = sourceCoin;
    }

    public List<EMoneyMapModel> getModelList() {
        return modelList;
    }

    public void setModelList(List<EMoneyMapModel> modelList) {
        this.modelList = modelList;
    }

    public String getSourceNation() {
        return sourceNation;
    }

    public void setSourceNation(String sourceNation) {
        this.sourceNation = sourceNation;
    }
}
