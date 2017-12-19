package com.timediffproject.module.money;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.timediffproject.application.MyClient;
import com.timediffproject.constants.Constant;
import com.timediffproject.model.CountryModel;
import com.timediffproject.module.emoney.OnGetExchangeMoneyListener;
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

    public void requestEMoney(String nationS,String nationT,OnGetExchangeMoneyListener listener){
        if (TextUtils.isEmpty(nationS) || TextUtils.isEmpty(nationT) || listener == null){
            return;
        }
        List<String> targetNationList = new ArrayList<>();
        targetNationList.add(nationT);
        requestEMoney(nationS,targetNationList,listener);
    }

    public void requestEMoney(String sourceNation, List<String> targetNationList){
        requestEMoney(sourceNation,targetNationList,null);
    }

    public void requestEMoney(String sourceNation, List<String> targetNationList, final OnGetExchangeMoneyListener listener) {

        final IRequest request = (IRequest) MyClient.getMyClient().getService(MyClient.SERVICE_HTTP_REQUEST);
        if (request == null) {
            return;
        }

        HashMap<String,String> map = new HashMap<>();

        map.put("sourceNation",sourceNation);
        map.put("targetNationList", V2ArrayUtil.getJsonArrData(targetNationList));

        request.sendRequestForPostWithJson(URL_GET_EMONEY_BY_CITYS, map, new IRequestCallback() {

            private void dispatch(boolean isSuccess,EMoneyResultModel model){
                if (listener == null && onGetEMoneyListener == null){
                    return;
                }
                if (listener == null){
                    onGetEMoneyListener.onGetEMoneyFinish(isSuccess);
                }else{
                    listener.onGetExchangeMoneyFinish(isSuccess,model);
                }
            }

            @Override
            public void onResponseSuccess(JSONObject jsonObject) {
                if (onGetEMoneyListener == null && listener == null) {
                    return;
                }

                if (jsonObject == null) {
                    dispatch(false,null);
                    return;
                }

                EMoneyResultModel model = new EMoneyResultModel();
                model.decode(jsonObject);
                if (model.getCode() == 0){
                    mEMoneyResultModel = model;
                    dispatch(true,model);
                }else{
                    dispatch(false,null);
                }

            }

            @Override
            public void onResponseSuccess(String str) {

            }

            @Override
            public void onResponseError(int code) {
                dispatch(false,null);
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
