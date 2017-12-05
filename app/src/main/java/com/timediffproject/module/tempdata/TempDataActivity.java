package com.timediffproject.module.tempdata;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.timediffproject.R;
import com.timediffproject.application.BaseActivity;
import com.timediffproject.application.MyClient;
import com.timediffproject.model.CountryModel;
import com.timediffproject.module.select.SelectManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by melon on 2017/6/26.
 */

public class TempDataActivity extends BaseActivity implements OnDataFinishListener {

    private int index;
    private int max;
    private List<CountryModel> list = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);


        findViewById(R.id.tv_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload();
            }
        });

        findViewById(R.id.tv_update_difftime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });

    }

    private void update(){
        list = MyClient.getMyClient().getDataManager().getCountryList();
        if (list.isEmpty()){
            return;
        }

        index = 0;
        max = list.size();

        MyClient.getMyClient().getDataManager().updateDiffTime(list.get(index),this);
    }

    private void upload(){
        List<CountryModel> list = MyClient.getMyClient().getSelectManager().getCountryModelList(SelectManager.STATE_ALL);
        if (list.isEmpty()){
            return;
        }

        index = 0;
        max = list.size();

        MyClient.getMyClient().getDataManager().uploadCountry(list.get(index),this);
    }

    @Override
    public void onHandleDataFinish() {
        index ++;
        if (index == max){
            return;
        }

        MyClient.getMyClient().getDataManager().updateDiffTime(list.get(index),this);
//        MyClient.getMyClient().getDataManager().uploadCountry(MyClient.getMyClient().getSelectManager().getCountryModelList(SelectManager.STATE_ALL).get(index),this);
    }
}
