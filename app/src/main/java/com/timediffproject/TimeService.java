package com.timediffproject;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.timediffproject.application.MyClient;

/**
 * Created by melon on 2017/1/8.
 */

public class TimeService extends Service {

    private Handler handler;

    private long count = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent!=null){
            count = intent.getIntExtra("second",0);
        }
        handler.post(runnable);
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (handler == null){
            handler = new Handler(getMainLooper());
        }

//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(Intent.ACTION_TIME_TICK); // 时间的流逝
//        intentFilter.addAction(Intent.ACTION_TIME_CHANGED); // 时间被改变，人为设置时间
//        registerReceiver(boroadcastReceiver, intentFilter);
    }

    // 用于监听系统时间变化Intent.ACTION_TIME_TICK的BroadcastReceiver，此BroadcastReceiver须为动态注册
//    private BroadcastReceiver boroadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context acontext, Intent intent) {
            //分钟更新一次
//            MyClient.getMyClient().getTimeManager().dispathUpdateTimeCallBack();
//        }
//    };

    private void postTime(){
        handler.postDelayed(runnable,1000);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (count%60==0){
                MyClient.getMyClient().getTimeManager().dispathUpdateTimeCallBack();
                count = 0;
            }
            count++;
            postTime();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(boroadcastReceiver);
    }
}
