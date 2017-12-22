package com.timediffproject.module.ring;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.timediffproject.R;
import com.timediffproject.application.BaseActivity;
import com.timediffproject.application.MyClient;
import com.timediffproject.constants.Constant;
import com.timediffproject.constants.Constants;
import com.timediffproject.constants.ParamConstants;
import com.timediffproject.model.CountryModel;
import com.timediffproject.module.alarm.AlarmModel;
import com.timediffproject.module.set.SetAlarmUtil;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by melon on 2017/2/3.
 */

public class RingActivity extends BaseActivity implements RingCancelListener {

    private RingService ringService;

    private AlarmModel model;

    private TextView mTvClock;
    private TextView mTvDate;
    private TextView mTvWeek;
    private TextView mTvCity;

    private PowerManager.WakeLock wakeLock;

    //闹钟类型（正常，暂缓）
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        umengPage = "响钟页";
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring);
        handleIntent();
        culNextAlarm();
        initView();
        initListener();
        ringService = new RingService();
        bindServiceConnection();


        if (ringService == null || model == null){
            finish();
        }
        ringService.play(model.getNoiseLevel());

    }

    private void handleIntent(){
        type = getIntent().getStringExtra(ParamConstants.KEY_ALARM_TYPE_PAUSE);
        if (type.equals(ParamConstants.VALUE_ALARM_TYPE_RING)){
            int id = getIntent().getIntExtra("id",0);
            if (id != 0){
                model = MyClient.getMyClient().getMyAlarmManager().getAlarmModelById(id);
            }
        }
        if (model == null){
            finish();
        }
    }

    private void culNextAlarm(){
        MyClient.getMyClient().getMyAlarmManager().cancelAlarm(this,model);
        if (type.equals(ParamConstants.VALUE_ALARM_TYPE_RING)){
            if (model!=null){
                if (model.isRepeatAlarm()){
                    model = SetAlarmUtil.culNextAlarmTime(model);
                    MyClient.getMyClient().getMyAlarmManager().addOnceAlarm(this,model);
                }
            }
        }
    }

    private void initView(){
        mTvClock = (TextView) findViewById(R.id.tv_ring_clock);
        mTvDate = (TextView) findViewById(R.id.tv_ring_date);
        mTvWeek = (TextView) findViewById(R.id.tv_ring_week);
        mTvCity = (TextView) findViewById(R.id.tv_ring_city);

        Date nowDate;
        CountryModel countryModel = null;
        if (model == null){
            nowDate = new Date();
        }else{
            countryModel = MyClient.getMyClient().getSelectManager().getNationById(model.getCityId());
            nowDate = MyClient.getMyClient().getTimeManager().getTime(countryModel.getDiffTime());
            mTvCity.setVisibility(View.VISIBLE);
            mTvCity.setText(countryModel.getCityName());
        }
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm");
        mTvClock.setText(simpleDateFormat1.format(nowDate));

        SimpleDateFormat myFmt2=new SimpleDateFormat("yyyy-MM-dd");
        String strTime1 = myFmt2.format(nowDate);
        String month = strTime1.split("-")[1];
        String day = strTime1.split("-")[2];
        //2004-12-16 17:24:27
        String strTime2 = nowDate.toString();
        String monthE = strTime2.split(" ")[1];
        //16 Dec 2004 09:24:27 GMT
        mTvDate.setText(month+"月"+day+"日");

        String[] dayOfWeeks = getResources().getStringArray(R.array.day_of_week);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        mTvWeek.setText(dayOfWeeks[dayOfWeek-1]);

    }

    private void initListener(){
        ((RingRelativeLayout)findViewById(R.id.rl_ring_content)).setListener(this);
        findViewById(R.id.tv_ring_pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyClient.getMyClient().getMyAlarmManager().addPauseAlarm(RingActivity.this);
                cancelClock();
            }
        });
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ringService = ((RingService.MyBinder)iBinder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            ringService = null;
        }
    };
    private void bindServiceConnection() {

        Intent intent = new Intent(RingActivity.this, RingService.class);
        bindService(intent, serviceConnection, this.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        if (ringService != null){
            ringService.stop();
        }
        unbindService(serviceConnection);
        super.onDestroy();
//        if (wakeLock != null){
//            wakeLock.release();
//            wakeLock=null;
//        }
    }

    @Override
    public void cancelClock() {
        finish();
    }
}
