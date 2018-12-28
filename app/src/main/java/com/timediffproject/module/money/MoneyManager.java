package com.timediffproject.module.money;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.timediffproject.application.MyClient;
import com.timediffproject.constants.Constant;
import com.timediffproject.model.CountryModel;
import com.timediffproject.module.emoney.OnGetExchangeMoneyListener;
import com.timediffproject.network.IRequest;
import com.timediffproject.network.IRequestCallback;
import com.timediffproject.network2.MoneyRatioService;
import com.timediffproject.network2.MoneyRatioServiceImpl;
import com.timediffproject.network2.RetrofitManager;
import com.timediffproject.util.V2ArrayUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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
        HashMap<String,String> map = new HashMap<>();
        map.put("sourceNation",sourceNation);
        map.put("targetNationList", V2ArrayUtil.getJsonArrData(targetNationList));
        Disposable subscribe = MoneyRatioServiceImpl.getNationMoneyRatio(map).subscribe(new Consumer<EMoneyResultModel2>() {

            @Override
            public void accept(EMoneyResultModel2 eMoneyResultModel) throws Exception {
                if (onGetEMoneyListener == null && listener == null) {
                    return;
                }

                boolean isSuccess = true;
                if (eMoneyResultModel == null) {
                    isSuccess = false;
                }

                if (listener == null){
                    onGetEMoneyListener.onGetEMoneyFinish(isSuccess);
                }else{
                    listener.onGetExchangeMoneyFinish(isSuccess,eMoneyResultModel);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if (listener == null){
                    onGetEMoneyListener.onGetEMoneyFinish(false);
                }else{
                    listener.onGetExchangeMoneyFinish(false,null);
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
