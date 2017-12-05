package com.timediffproject.module.misc;

import com.alibaba.fastjson.JSONObject;
import com.timediffproject.application.MyClient;
import com.timediffproject.constants.Constant;
import com.timediffproject.model.CountryModel;
import com.timediffproject.module.home.AppUpdateCheckModel;
import com.timediffproject.module.tempdata.OnDataFinishListener;
import com.timediffproject.network.IRequest;
import com.timediffproject.network.IRequestCallback;

import java.util.HashMap;

/**
 * Created by melon on 2017/10/27.
 */

public class MiscManager {

    private static final String URL_UPDATE_CHECK = Constant.MAIN_URL + "/update/check";

    public void updateCheck(final OnUpdateCheckListener listener) {

        final IRequest request = (IRequest) MyClient.getMyClient().getService(MyClient.SERVICE_HTTP_REQUEST);
        if (request == null) {
            return;
        }

        HashMap<String,String> map = new HashMap<>();

        request.sendRequestForPostWithJson(URL_UPDATE_CHECK, map, new IRequestCallback() {
            @Override
            public void onResponseSuccess(JSONObject jsonObject) {
                if (listener == null) {
                    return;
                }

                if (jsonObject == null) {
                    listener.onUpdateCheck(null);
                    return;
                }


                AppUpdateCheckModel model = new AppUpdateCheckModel();
                model.decode(jsonObject);
                listener.onUpdateCheck(model);

            }

            @Override
            public void onResponseSuccess(String str) {

            }

            @Override
            public void onResponseError(int code) {
                if (listener != null) {
                    listener.onUpdateCheck(null);
                }
            }
        });


    }

}
