package com.timediffproject.network2;


import com.alibaba.fastjson.JSONObject;
import com.timediffproject.module.money.*;
import com.timediffproject.util.V2ArrayUtil;


import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class MoneyRatioServiceImpl {

    public static Observable<EMoneyResultModel2> getNationMoneyRatio(HashMap<String,String> map){
        JSONObject object = new JSONObject();
        for (String key : map.keySet()){
            object.put(key,map.get(key));
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), object.toJSONString());
        MoneyRatioService service = RetrofitManager.getInstant().getRetrofit().create(MoneyRatioService.class);
        return service.getNationMoneyRatio(body)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
