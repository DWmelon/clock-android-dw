package com.timediffproject.module.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.timediffproject.R;
import com.timediffproject.application.BaseActivity;
import com.timediffproject.application.MyClient;
import com.timediffproject.listener.OnGetTimeCallback;
import com.timediffproject.module.home.MyMainActivity;
import com.timediffproject.widgets.flowtext.Typefaces;

/**
 * Created by melon on 2017/1/3.
 */

public class SplashActivity extends BaseActivity implements OnGetTimeCallback {

    private TextView tv;

    private boolean isReady = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        umengPage = "欢迎页";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tv = (TextView) findViewById(R.id.tv_splash);

        tv.setTypeface(Typefaces.get(this, "Satisfy-Regular.ttf"));

        MyClient.getMyClient().getTimeManager().registerGetTimeCallBack(this);
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                isReady = true;
                if (MyClient.getMyClient().getTimeManager().isLoadFinish()){
                    judgeResult();
                }

            }
        },2000);
    }

    @Override
    public void onGetTimeFinish() {
        if (isReady){
            judgeResult();
        }
    }

    private void judgeResult(){
        if (!MyClient.getMyClient().getTimeManager().getDateStr().isEmpty()){
            alarmWidget();

            Intent intent = new Intent(SplashActivity.this,MyMainActivity.class);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(SplashActivity.this,"不能获得时间",Toast.LENGTH_LONG).show();
        }
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
