package com.timediffproject.module.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.timediffproject.R;
import com.timediffproject.application.BaseActivity;
import com.timediffproject.application.MyClient;
import com.timediffproject.application.PermissionActivity;
import com.timediffproject.listener.OnGetTimeCallback;
import com.timediffproject.module.home.MyMainActivity;
import com.timediffproject.widgets.flowtext.Typefaces;

/**
 * Created by melon on 2017/1/3.
 */

public class SplashActivity extends PermissionActivity implements OnGetTimeCallback {

    //闪屏展示时间
    private final int SPLASH_SHOW_TIME = 2000;

    private boolean isCountDownFinish;
    private boolean isTimeCheckFinish;

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        umengPage = "欢迎页";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tv = (TextView) findViewById(R.id.tv_splash);

        tv.setTypeface(Typefaces.get(this, "Satisfy-Regular.ttf"));


        boolean isLoadFinish = MyClient.getMyClient().getTimeManager().isLoadFinish();
        if (isLoadFinish){
            isTimeCheckFinish = true;
        }else{
            MyClient.getMyClient().getTimeManager().registerGetTimeCallBack(this);
        }

        startSplash();
    }

    private void startSplash(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isCountDownFinish = true;
                handleIntentNext();
            }
        },SPLASH_SHOW_TIME);
    }


    protected void handleIntentNext(){
        if (!isCountDownFinish || !isTimeCheckFinish || !isPermissionCheck){
            return;
        }
        if (!MyClient.getMyClient().getTimeManager().getDateStr().isEmpty()){
            alarmWidget();

            Intent intent = new Intent(SplashActivity.this,MyMainActivity.class);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(SplashActivity.this,"不能获得时间",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onGetTimeFinish() {
        isTimeCheckFinish = true;
        handleIntentNext();
    }

    private void alarmWidget(){
        Intent i = new Intent();
        i.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        sendBroadcast(i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyClient.getMyClient().getTimeManager().unregisterGetTimeCallBack(this);
    }

}
