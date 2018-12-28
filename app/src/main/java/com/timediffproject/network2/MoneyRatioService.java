package com.timediffproject.network2;


import com.timediffproject.constants.Constant;
import com.timediffproject.module.money.EMoneyResultModel;
import com.timediffproject.module.money.EMoneyResultModel2;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MoneyRatioService {

//    @FormUrlEncoded
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("exchange_rate/list/nation")
    Observable<EMoneyResultModel2> getNationMoneyRatio(@Body RequestBody body);

}
