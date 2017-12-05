package com.timediffproject.module.alarm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by melon on 2017/1/11.
 */

public class AlarmModel implements Serializable {

    private static final long serialVersionUID = 5152490762332477309L;

    //作用等同于id
    private int requestCode;

    private long settingTime;

    private Long cityId;

    private String city;

    private long alarmTime;

    private boolean isRepeatAlarm = false;

    private List<Integer> repeatDays = new ArrayList<>();

    private int repeatIndex;

    private boolean isUsing = true;

    private float noiseLevel = 0.5f;

    public float getNoiseLevel() {
        return noiseLevel;
    }

    public void setNoiseLevel(float noiseLevel) {
        this.noiseLevel = noiseLevel;
    }

    public long getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(long alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public long getSettingTime() {
        return settingTime;
    }

    public void setSettingTime(long settingTime) {
        this.settingTime = settingTime;
    }

    public boolean isRepeatAlarm() {
        return isRepeatAlarm;
    }

    public void setRepeatAlarm(boolean repeatAlarm) {
        isRepeatAlarm = repeatAlarm;
    }

    public List<Integer> getRepeatDays() {
        return repeatDays;
    }

    public void setRepeatDays(List<Integer> repeatDays) {
        this.repeatDays = repeatDays;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public int getRepeatIndex() {
        return repeatIndex;
    }

    public void setRepeatIndex(int repeatIndex) {
        this.repeatIndex = repeatIndex;
    }

    public boolean isUsing() {
        return isUsing;
    }

    public void setUsing(boolean using) {
        isUsing = using;
    }

    @Override
    public String toString() {
        return "AlarmModel{" +
                "requestCode=" + requestCode +
                ", settingTime=" + settingTime +
                ", cityId=" + cityId +
                ", city='" + city + '\'' +
                ", alarmTime=" + alarmTime +
                ", isRepeatAlarm=" + isRepeatAlarm +
                ", repeatDays=" + repeatDays +
                ", repeatIndex=" + repeatIndex +
                ", isUsing=" + isUsing +
                ", noiseLevel=" + noiseLevel +
                '}';
    }
}
