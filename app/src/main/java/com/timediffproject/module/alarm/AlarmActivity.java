package com.timediffproject.module.alarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.timediffproject.R;
import com.timediffproject.application.BaseActivity;
import com.timediffproject.application.MyClient;
import com.timediffproject.module.ring.RingActivity;
import com.timediffproject.module.set.SettingTimeActivity;
import com.timediffproject.module.set.time.RadialPickerLayout;
import com.timediffproject.module.set.time.TimePickerDialog;
import com.umeng.analytics.MobclickAgent;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by melon on 2017/1/20.
 */

public class AlarmActivity extends BaseActivity implements View.OnClickListener,OnLoadAlarmFinishListener{

    private Toolbar mToolbar;

    private RecyclerView mRvAlarmList;
    private AlarmListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        umengPage = "时钟设置页";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        initView();
        initData();
        initListener();
    }

    private void initView(){
        mRvAlarmList = (RecyclerView)findViewById(R.id.rv_alarm_list);
        mRvAlarmList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AlarmListAdapter(this,MyClient.getMyClient().getMyAlarmManager());
        mRvAlarmList.setAdapter(adapter);
    }

    private void initData(){
        mToolbar = (Toolbar)findViewById(R.id.my_toolbar);
        mToolbar.setTitle("闹钟列表");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.icon_back_white);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initListener(){
        findViewById(R.id.fab_alarm_add).setOnClickListener(this);
        MyClient.getMyClient().getMyAlarmManager().registerLoadAlarmListener(this);
    }

    private void addAlarm(){
        Intent intent = new Intent(this,SettingTimeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_alarm_add:{
                addAlarm();
                break;
            }
        }
    }

    @Override
    public void loadDataFinish() {
        if (adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyClient.getMyClient().getMyAlarmManager().unregisterLoadAlarmListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }
}
