package com.timediffproject.module.set;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.timediffproject.R;
import com.timediffproject.application.BaseActivity;
import com.timediffproject.network.UrlConstantV2;
import com.timediffproject.origin.MainApplication;
import com.timediffproject.util.SlidingUpDialog;

import java.util.Arrays;

/**
 * Created by melon on 2017/12/12.
 */

public class TimeVoiceActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar mToolbar;

    private TextView mTvVoice;

    private SlidingUpDialog dialogVoice;
    private WheelView mWvVoice;

    private static MediaPlayer mp;
    private int maxVolume;
    private float voiceLevel = UrlConstantV2.VALUE.DEFAULT_VOICE;

    private boolean isModify = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_voice);

        voiceLevel = getIntent().getFloatExtra(UrlConstantV2.BUNDLE.VOICE_LEVEL,UrlConstantV2.VALUE.DEFAULT_VOICE);

        initView();
        initCityDialog();
        initListener();
    }

    private void initView(){
        mToolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationIcon(R.drawable.icon_back_white);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        mToolbar.setTitle(R.string.voice_name);

        mTvVoice = (TextView)findViewById(R.id.tv_set_voice_level);
        mTvVoice.setText(String.valueOf((int)(voiceLevel*10)));
    }

    private void initCityDialog(){
        dialogVoice = new SlidingUpDialog(this);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_set_voice, null);
        dialogVoice.setContentView(contentView);
        mWvVoice = (WheelView) contentView.findViewById(R.id.wv_voice);
        mWvVoice.setOffset(1);
        mWvVoice.setItems(Arrays.asList(getResources().getStringArray(R.array.voice)));
        mWvVoice.setOnWheelViewListener(new WheelView.OnWheelViewListener(){
            public void onSelected(int selectedIndex, String item) {
                voiceLevel = selectedIndex/(float)10;
                mTvVoice.setText(item);
                isModify = true;
            }
        });
        contentView.findViewById(R.id.iv_voice_play).setOnClickListener(this);

    }

    private void initListener(){
        findViewById(R.id.ll_alarm_voice).setOnClickListener(this);
    }


    @Override
    public void onBackPressed() {
        back();
    }

    private void back(){
        if (isModify){
            Intent intent = new Intent();
            intent.putExtra(UrlConstantV2.BUNDLE.VOICE_LEVEL,voiceLevel);
            setResult(RESULT_OK,intent);
        }
        finish();
    }

    public void play() {
//        try {

        AudioManager audioManager = (AudioManager) MainApplication.getContext().getSystemService(Context.AUDIO_SERVICE);
        //最大音量

        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        maxVolume = (int) (maxVolume*voiceLevel);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume,
                AudioManager.FLAG_PLAY_SOUND);

        if (mp == null){
            try {
                mp = MediaPlayer.create(MainApplication.getContext(), R.raw.ding);
                if (mp != null) {
                    mp.stop();
                }
                mp.setLooping(false);
                mp.prepare();
            }catch (Exception e){
                Log.i("service","fail");
            }
        }

        mp.start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_alarm_voice:{
                dialogVoice.show();
                mWvVoice.setSeletion((int)(voiceLevel*10)-1);
                break;
            }
            case R.id.iv_voice_play:{
                play();
                break;
            }
        }
    }
}
