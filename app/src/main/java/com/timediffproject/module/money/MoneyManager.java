package com.timediffproject.module.money;

import com.alibaba.fastjson.JSONObject;
import com.timediffproject.application.MyClient;
import com.timediffproject.constants.Constant;
import com.timediffproject.model.CountryModel;
import com.timediffproject.network.IRequest;
import com.timediffproject.network.IRequestCallback;
import com.timediffproject.util.V2ArrayUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by melon on 2017/12/13.
 */

public class MoneyManager {

    private static final String URL_GET_EMONEY_BY_CITYS = Constant.MAIN_URL + "/exchange_rate/list/nation";

    private EMoneyResultModel mEMoneyResultModel;

    private OnGetEMoneyListener onGetEMoneyListener;

    public void setOnGetEMoneyListener(OnGetEMoneyListener listener){
        onGetEMoneyListener = listener;
    }

    public void requestEMoney(){
        List<CountryModel> countryModelList = MyClient.getMyClient().getSelectManager().getUserCountry();
        if (countryModelList == null || countryModelList.size()<=1){
            return;
        }
        List<String> tList = new ArrayList<>();
        for (int i = 1;i < countryModelList.size();i++){
            tList.add(countryModelList.get(i).getNationName());
        }
        requestEMoney(countryModelList.get(0).getNationName(),tList);
    }

    public void requestEMoney(String sourceNation, List<String> targetNationList) {

        final IRequest request = (IRequest) MyClient.getMyClient().getService(MyClient.SERVICE_HTTP_REQUEST);
        if (request == null) {
            return;
        }

        HashMap<String,String> map = new HashMap<>();

        map.put("sourceNation",sourceNation);
        map.put("targetNationList", V2ArrayUtil.getJsonArrData(targetNationList));

        request.sendRequestForPostWithJson(URL_GET_EMONEY_BY_CITYS, map, new IRequestCallback() {
            @Override
            public void onResponseSuccess(JSONObject jsonObject) {
                if (onGetEMoneyListener == null) {
                    return;
                }

                if (jsonObject == null) {
                    onGetEMoneyListener.onGetEMoneyFinish(false);
                    return;
                }

                EMoneyResultModel model = new EMoneyResultModel();
                model.decode(jsonObject);
                if (model.getCode() == 0){
                    mEMoneyResultModel = model;
                    onGetEMoneyListener.onGetEMoneyFinish(true);
                }else{
                    onGetEMoneyListener.onGetEMoneyFinish(false);
                }



            }

            @Override
            public void onResponseSuccess(String str) {

            }

            @Override
            public void onResponseError(int code) {
                if (onGetEMoneyListener != null) {
                    onGetEMoneyListener.onGetEMoneyFinish(false);
                }
            }
        });


    }

    public boolean checkIsDataFit(String sourceNation){
        return !(mEMoneyResultModel == null || !mEMoneyResultModel.getSourceNation().equals(sourceNation));
    }

    public HashMap<String,EMoneyMapModel> getEMoneyMap(){
        HashMap<String,EMoneyMapModel> map = new HashMap<>();
        if (mEMoneyResultModel == null){
            return map;
        }

        for (EMoneyMapModel model : mEMoneyResultModel.getModelList()){
            map.put(model.getCoinNation(),model);
        }
        return map;
    }

    public String getEMoneySourceCoinName(){
        if (mEMoneyResultModel == null){
            return "";
        }

        return mEMoneyResultModel.getSourceCoin();
    }

}
