package com.timediffproject.module.ring;

import android.content.ComponentName;
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
import com.timediffproject.application.GlobalPreferenceManager;
import com.timediffproject.application.MyClient;
import com.timediffproject.constants.Constant;
import com.timediffproject.database.AlarmDaoUtil;
import com.timediffproject.model.CountryModel;
import com.timediffproject.database.AlarmModel;
import com.timediffproject.module.set.SetAlarmUtil;
import com.timediffproject.stat.StatCMConstant;
import com.timediffproject.stat.StatManager;
import com.timediffproject.util.CommonUtil;
import com.timediffproject.util.RandomUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by melon on 2017/2/3.
 */

public class RingActivity extends BaseActivity implements RingCancelListener {

    private static final long RING_NEXT_TIME = 2 * 60 * 1000;//10分钟后响铃

    private RingService ringService;

    private AlarmModel model;

    private TextView mTvClock;
    private TextView mTvDate;
    private TextView mTvWeek;
    private TextView mTvCity;

    private PowerManager.WakeLock wakeLock;

    //闹钟类型（正常，暂缓）
    private String type;

    private String language;

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
        ringService.play(model==null?0.5f:model.getNoiseLevel());
        StatManager.statEventNum(this, StatCMConstant.PAGE_IN_RING_ALARM);
    }

    private void handleIntent(){
        type = getIntent().getStringExtra(Constant.KEY_ALARM_TYPE);
        if (type.equals(Constant.VALUE_ALARM_TYPE_RING)){
            long id = getIntent().getLongExtra("id",0);
            if (id != 0){
                model = AlarmDaoUtil.loadAlarm(id);
            }
            if (model == null){
                finish();
            }
        }else{

        }

    }

    private void culNextAlarm(){
        if (type.equals(Constant.VALUE_ALARM_TYPE_PAUSE)){
            return;
        }
        MyClient.getMyClient().getMyAlarmManager().cancelAlarm(this,model);
        if (type.equals(Constant.VALUE_ALARM_TYPE_RING)){
            if (model!=null){
                if (model.getRepeatAlarm()){
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

        language = GlobalPreferenceManager.getString(this,GlobalPreferenceManager.KEY_LANGUAGE);

        Date nowDate;
        CountryModel countryModel = null;
        if (model == null){
            nowDate = new Date();
            findViewById(R.id.tv_ring_pause).setVisibility(View.INVISIBLE);
        }else{
            countryModel = MyClient.getMyClient().getSelectManager().getNationById(model.getCityId());
            nowDate = MyClient.getMyClient().getTimeManager().getTime(countryModel.getDiffTime());

            mTvCity.setVisibility(View.VISIBLE);
            mTvCity.setText(CommonUtil.getCityNameByLanguage(language,countryModel));
        }
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm");
        mTvClock.setText(simpleDateFormat1.format(nowDate));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowDate);

        String monthStr = CommonUtil.getMonthStr(language,calendar);
        String dayOfWeekStr = CommonUtil.getDayOfWeekStr(language,calendar);
        String dayOfMonthStr = CommonUtil.getDayOfMonthStr(language,calendar);
        String devide = language.equals(Constant.LANGUAGE_CHINA)?"":" ";
        mTvDate.setText(monthStr+devide+dayOfMonthStr);

        mTvWeek.setText(dayOfWeekStr);

    }

    private void initListener(){
        ((RingRelativeLayout)findViewById(R.id.rl_ring_content)).setListener(this);
        findViewById(R.id.tv_ring_pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long time = model.getAlarmTime() + RING_NEXT_TIME;
                MyClient.getMyClient().getMyAlarmManager().addPauseAlarm(RingActivity.this,time);
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
