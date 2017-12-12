package com.timediffproject.module.set;

import android.app.AlarmManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.suke.widget.SwitchButton;
import com.timediffproject.R;
import com.timediffproject.application.BaseActivity;
import com.timediffproject.application.MyClient;
import com.timediffproject.constants.Constants;
import com.timediffproject.model.CountryModel;
import com.timediffproject.module.alarm.AlarmActivity;
import com.timediffproject.module.alarm.AlarmModel;
import com.timediffproject.module.alarm.MyAlarmManager;
import com.timediffproject.module.home.MyMainActivity;
import com.timediffproject.module.select.SelectActivity;
import com.timediffproject.module.select.SelectManager;
import com.timediffproject.module.set.time.RadialPickerLayout;
import com.timediffproject.module.set.time.TimePickerDialog;
import com.timediffproject.network.UrlConstantV2;
import com.timediffproject.util.RandomUtil;
import com.timediffproject.util.SlidingUpDialog;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

/**
 * Created by melon on 2017/1/20.
 */

public class SettingTimeActivity extends BaseActivity implements View.OnClickListener,OnSetCityChangeListener {

    public static final String KEY_PAGE_TYPE = "page";
    public static final String PAGE_TYPE_MODIFY = "modify";
    public static final String PAGE_TYPE_ADD = "add";
    private String pageType = PAGE_TYPE_ADD;

    private Toolbar mToolbar;

    private TextView mTvCity;
    private SlidingUpDialog dialog;

    private SwitchButton mSbRepeat;

    private TextView mTvSet1;
    private TextView mTvSet2;
    private TextView mTvSet3;
    private TextView mTvSet4;
    private TextView mTvSet5;
    private TextView mTvSet6;
    private TextView mTvSet7;
    private HashMap<Integer,View> setMap = new HashMap<>();

    private WheelView mWvHour;
    private WheelView mWvMin;

    private TextView mTvRepeat;

    private HashSet<Integer> repeatMap = new HashSet<>();
    private int hour;
    private int min;

    private float voiceLevel = UrlConstantV2.VALUE.DEFAULT_VOICE;

    private AlarmModel alarmModel;
    private CountryModel cityModel;
    private SelectManager selectManager;
    private MyAlarmManager alarmManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        umengPage = "时钟设置页";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_time);
        selectManager = MyClient.getMyClient().getSelectManager();
        alarmManager = MyClient.getMyClient().getMyAlarmManager();
        initCityDialog();
        initIntent();
        initView();
        initListener();
        initData();
    }

    private void initIntent(){
        Intent intent = getIntent();
        if (intent == null){
            pageType = PAGE_TYPE_ADD;
            dialog.show();
            return;
        }

        cityModel = selectManager.getNationById(getIntent().getLongExtra(Constants.INTENT_KEY_CITY_ID,0));

        if (intent.getStringExtra(KEY_PAGE_TYPE)!=null){
            pageType = intent.getStringExtra(KEY_PAGE_TYPE);
        }

        if (pageType.equals(PAGE_TYPE_MODIFY)){
            int code = intent.getIntExtra(Constants.INTENT_KEY_ALARM_ID,-1);
            if (code == -1){
                pageType = PAGE_TYPE_ADD;
                dialog.show();
                return;
            }
            alarmModel = alarmManager.getAlarmModelById(code);
            voiceLevel = alarmModel.getNoiseLevel();
        }

    }

    private void initView(){
        mTvCity = (TextView)findViewById(R.id.tv_set_city);

        mSbRepeat = (SwitchButton)findViewById(R.id.sb_set_repeat);
        mTvRepeat = (TextView)findViewById(R.id.tv_is_repeat);

        mTvSet1 = (TextView)findViewById(R.id.tv_set_1);
        mTvSet2 = (TextView)findViewById(R.id.tv_set_2);
        mTvSet3 = (TextView)findViewById(R.id.tv_set_3);
        mTvSet4 = (TextView)findViewById(R.id.tv_set_4);
        mTvSet5 = (TextView)findViewById(R.id.tv_set_5);
        mTvSet6 = (TextView)findViewById(R.id.tv_set_6);
        mTvSet7 = (TextView)findViewById(R.id.tv_set_7);

        mWvHour = (WheelView)findViewById(R.id.wv_hour);
        mWvMin = (WheelView)findViewById(R.id.wv_min);

    }

    private void initData(){
        if (cityModel != null){
            mTvCity.setText(cityModel.getCityName());
        }

        mToolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationIcon(R.drawable.icon_back_white);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSbRepeat.setChecked(false);
        mSbRepeat.setShadowEffect(true);//disable shadow effect
        mSbRepeat.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                findViewById(R.id.ll_set_day).setVisibility(isChecked?View.VISIBLE:View.GONE);
                mTvRepeat.setText(isChecked?getString(R.string.set_repeat_y):getString(R.string.set_repeat_n));
            }
        });

        mWvHour.setOffset(1);
        mWvHour.setItems(Arrays.asList(getResources().getStringArray(R.array.hour)));
        mWvHour.setOnWheelViewListener(new SetWheelViewListener(SetWheelViewListener.POSITION_HOUR));

        mWvMin.setOffset(1);
        mWvMin.setItems(Arrays.asList(getResources().getStringArray(R.array.min)));
        mWvMin.setOnWheelViewListener(new SetWheelViewListener(SetWheelViewListener.POSITION_MIN));


        if (pageType.equals(PAGE_TYPE_ADD)){
            initAddData();
        }else{
            initModifyData();
        }

    }

    private void initAddData(){
        mToolbar.setTitle(R.string.set_title);

        float diffTime = 0;
        if (cityModel != null){
            diffTime = cityModel.getDiffTime();
        }
        Date date = MyClient.getMyClient().getTimeManager().getTime(diffTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        mWvHour.setSeletion(calendar.get(Calendar.HOUR_OF_DAY));
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        mWvMin.setSeletion(calendar.get(Calendar.MINUTE));
        min = calendar.get(Calendar.MINUTE);
    }

    private void initModifyData(){
        if (alarmModel == null){
            finish();
            return;
        }
        mToolbar.setTitle(R.string.modify_title);

        if (alarmModel.isRepeatAlarm()){
            findViewById(R.id.ll_set_day).setVisibility(View.VISIBLE);
            List<Integer> days = alarmModel.getRepeatDays();
            for (Integer day : days){
                setMap.get(day).callOnClick();
            }
        }
        mSbRepeat.setChecked(alarmModel.isRepeatAlarm());

        Date date = new Date(alarmModel.getAlarmTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        mWvHour.setSeletion(calendar.get(Calendar.HOUR_OF_DAY));
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        mWvMin.setSeletion(calendar.get(Calendar.MINUTE));
        min = calendar.get(Calendar.MINUTE);
    }

    private void initListener(){
        findViewById(R.id.ll_set_city).setOnClickListener(this);
        findViewById(R.id.ll_alarm_voice).setOnClickListener(this);

        findViewById(R.id.rl_set_1).setOnClickListener(new SetRepeatClickListener(1));
        setMap.put(1,findViewById(R.id.rl_set_1));
        findViewById(R.id.rl_set_2).setOnClickListener(new SetRepeatClickListener(2));
        setMap.put(2,findViewById(R.id.rl_set_2));
        findViewById(R.id.rl_set_3).setOnClickListener(new SetRepeatClickListener(3));
        setMap.put(3,findViewById(R.id.rl_set_3));
        findViewById(R.id.rl_set_4).setOnClickListener(new SetRepeatClickListener(4));
        setMap.put(4,findViewById(R.id.rl_set_4));
        findViewById(R.id.rl_set_5).setOnClickListener(new SetRepeatClickListener(5));
        setMap.put(5,findViewById(R.id.rl_set_5));
        findViewById(R.id.rl_set_6).setOnClickListener(new SetRepeatClickListener(6));
        setMap.put(6,findViewById(R.id.rl_set_6));
        findViewById(R.id.rl_set_7).setOnClickListener(new SetRepeatClickListener(7));
        setMap.put(7,findViewById(R.id.rl_set_7));
    }

    private void initCityDialog(){
        dialog = new SlidingUpDialog(this);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_set_city, null);
        dialog.setContentView(contentView);
        RecyclerView mRvCity = (RecyclerView)contentView.findViewById(R.id.rv_dialog_city);
        mRvCity.setLayoutManager(new LinearLayoutManager(this));
        SetCityDialogAdapter adapter = new SetCityDialogAdapter(this);
        adapter.setListener(this);
        mRvCity.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_right, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_right:
                if (cityModel == null){
                    Toast.makeText(this,"请选择闹钟对应国家",Toast.LENGTH_LONG).show();
                    dialog.show();
                    return true;
                }
                if (mSbRepeat.isChecked()&&repeatMap.isEmpty()){
                    Toast.makeText(this,"请选择重复日期",Toast.LENGTH_LONG).show();
                    return true;
                }
                saveAlarm();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onChangeCity(int position) {
        cityModel = MyClient.getMyClient().getSelectManager().getUserCountry().get(position);
        mTvCity.setText(cityModel.getCityName());
        initAddData();
        if (dialog != null){
            dialog.dismiss();
        }
    }

    public class SetWheelViewListener extends WheelView.OnWheelViewListener {

        public static final int POSITION_HOUR = 1;
        public static final int POSITION_MIN = 2;

        private int position;

        public SetWheelViewListener(int position){
            this.position = position;
        }

        public void onSelected(int selectedIndex, String item) {
            if (position == POSITION_HOUR){
                hour = Integer.parseInt(item);
            }else if (position == POSITION_MIN){
                min = Integer.parseInt(item);
            }
        }
    }

    public class SetRepeatClickListener implements View.OnClickListener{

        int position;

        public SetRepeatClickListener(int position){
            this.position = position;
        }

        @Override
        public void onClick(View v) {

            if (repeatMap.contains(position)){
                repeatMap.remove(position);
            }else{
                repeatMap.add(position);
            }

            switch (v.getId()){
                case R.id.rl_set_1:{
                    mTvSet1.setSelected(!mTvSet1.isSelected());
                    break;
                }
                case R.id.rl_set_2:{
                    mTvSet2.setSelected(!mTvSet2.isSelected());
                    break;
                }
                case R.id.rl_set_3:{
                    mTvSet3.setSelected(!mTvSet3.isSelected());
                    break;
                }
                case R.id.rl_set_4:{
                    mTvSet4.setSelected(!mTvSet4.isSelected());
                    break;
                }
                case R.id.rl_set_5:{
                    mTvSet5.setSelected(!mTvSet5.isSelected());
                    break;
                }
                case R.id.rl_set_6:{
                    mTvSet6.setSelected(!mTvSet6.isSelected());
                    break;
                }
                case R.id.rl_set_7:{
                    mTvSet7.setSelected(!mTvSet7.isSelected());
                    break;
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_set_city:{
                if (dialog != null) {
                    dialog.show();
                }
                break;
            }
            case R.id.ll_alarm_voice:{
                Intent intent = new Intent(this,TimeVoiceActivity.class);
                intent.putExtra(UrlConstantV2.BUNDLE.VOICE_LEVEL,voiceLevel);
                startActivityForResult(intent,UrlConstantV2.REQUEST.ALARM_VOICE);
                break;
            }
        }
    }

    private void saveAlarm(){

        AlarmModel newAlarmModel = new AlarmModel();

        newAlarmModel.setNoiseLevel(voiceLevel);
        newAlarmModel.setCity(cityModel.getCityName());
        newAlarmModel.setCityId(cityModel.getId());
        newAlarmModel.setRequestCode(RandomUtil.getRandomInt());
        if (repeatMap.isEmpty()){
            newAlarmModel.setRepeatAlarm(false);
        }else{
            newAlarmModel.setRepeatAlarm(true);
            List<Integer> repeatDays = new ArrayList<>(repeatMap);
            Collections.sort(repeatDays, new Comparator<Integer>() {
                @Override
                public int compare(Integer integer, Integer t1) {
                    return integer.compareTo(t1);
                }
            });
            newAlarmModel.setRepeatDays(repeatDays);
        }

        newAlarmModel = SetAlarmUtil.culAlarmTime(newAlarmModel,cityModel.getDiffTime(),hour,min);

        if (pageType.equals(PAGE_TYPE_ADD)){
            alarmManager.addOnceAlarm(this,newAlarmModel);
        }else{
            int index = alarmManager.getAlarmStructModel().getAlarmModelList().indexOf(alarmModel);
            alarmManager.removeAlarm(this,alarmModel);
            alarmManager.addOnceAlarm(this,newAlarmModel,index);
        }

        finish();
//        performIntentAlarm();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UrlConstantV2.REQUEST.ALARM_VOICE && resultCode == RESULT_OK){
            voiceLevel = data.getFloatExtra(UrlConstantV2.BUNDLE.VOICE_LEVEL,UrlConstantV2.VALUE.DEFAULT_VOICE);
        }
    }
}
