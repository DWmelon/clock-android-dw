package com.timediffproject.module.ring;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.timediffproject.R;
import com.timediffproject.module.home.MyMainActivity;
import com.timediffproject.origin.MainApplication;
import com.timediffproject.storage.StorageManager;

import java.net.URL;

/**
 * Created by melon on 2017/2/15.
 */

public class RingService extends Service {

    /**
     * 除了播放应用的资源文件(res/raw/)使用create(Context,id)外，其他都是使用setDataSource();
     * 原始资源文件(assets): setDataSource(FileDescriptor fd, long offset, long length);
     * 存储上的音频资源文件(sdcard):setDataSource(String);
     * 播放网络上的音频文件:setDataSource(Context, uri);
     */

    private static MediaPlayer mp;

    private static int originVolume;
    private static int maxVolume;


    public RingService(){

    }

    public final IBinder binder = new MyBinder();
    public class MyBinder extends Binder {
        RingService getService() {
            return RingService.this;
        }

        void play(){
            RingService.this.play();
        }

        void stop(){

            RingService.this.stop();
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("service","onBind");
        return binder;
    }

    @Override
    public void onCreate() {
        Log.i("service","onCreate");
        super.onCreate();
    }

    @Override
    public void onRebind(Intent intent) {
        Log.i("service","onRebind");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("service","onUnbind");
        return super.onUnbind(intent);
    }

    public void play() {
//        try {

            AudioManager audioManager = (AudioManager) MainApplication.getContext().getSystemService(Context.AUDIO_SERVICE);
            //最大音量

            originVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            maxVolume = maxVolume/2;
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume,
                    AudioManager.FLAG_PLAY_SOUND);

        try {
            mp = MediaPlayer.create(MainApplication.getContext(), R.raw.samsumg);
            if (mp != null) {
                mp.stop();
            }
            mp.setLooping(true);
            mp.prepare();
        }catch (Exception e){
            Log.i("service","fail");
        }
        mp.start();
    }

    public void stop() {
        mp.stop();
        mp.release();
        AudioManager audioManager = (AudioManager) MainApplication.getContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originVolume,
                AudioManager.FLAG_PLAY_SOUND);
    }


}
