package com.timediffproject.database;

import android.text.TextUtils;

import com.timediffproject.util.V2ArrayUtil;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by melon on 2017/1/11.
 */
@Entity
public class AlarmModel implements Serializable {
    @Transient
    private static final long serialVersionUID = 5152490762332477309L;

    //作用等同于id
    @Id
    private Long requestCode;

    private Long settingTime;

    private Long cityId;

    private String city;

    private Long alarmTime;

    private Boolean isRepeatAlarm = false;

    private String repeatDaysStr;

    private Integer repeatIndex;

    private Boolean isUsing = true;

    private Float noiseLevel = 0.5f;

    @Generated(hash = 377260657)
    public AlarmModel(Long requestCode, Long settingTime, Long cityId, String city,
            Long alarmTime, Boolean isRepeatAlarm, String repeatDaysStr,
            Integer repeatIndex, Boolean isUsing, Float noiseLevel) {
        this.requestCode = requestCode;
        this.settingTime = settingTime;
        this.cityId = cityId;
        this.city = city;
        this.alarmTime = alarmTime;
        this.isRepeatAlarm = isRepeatAlarm;
        this.repeatDaysStr = repeatDaysStr;
        this.repeatIndex = repeatIndex;
        this.isUsing = isUsing;
        this.noiseLevel = noiseLevel;
    }

    @Generated(hash = 2112667735)
    public AlarmModel() {
    }

    public Long getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(Long requestCode) {
        this.requestCode = requestCode;
    }

    public Long getSettingTime() {
        return settingTime;
    }

    public void setSettingTime(Long settingTime) {
        this.settingTime = settingTime;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(Long alarmTime) {
        this.alarmTime = alarmTime;
    }

    public Boolean getRepeatAlarm() {
        return isRepeatAlarm;
    }

    public void setRepeatAlarm(Boolean repeatAlarm) {
        isRepeatAlarm = repeatAlarm;
    }

    public String getRepeatDaysStr() {
        return repeatDaysStr;
    }

    public void setRepeatDaysStr(String repeatDaysStr) {
        this.repeatDaysStr = repeatDaysStr;
    }

    public List<Integer> getRepeatDays() {
        List<String> strList = V2ArrayUtil.getListByJson(repeatDaysStr);
        List<Integer> intList = new ArrayList<>();
        for (String str : strList){
            intList.add(Integer.parseInt(str));
        }
        return intList;
    }

    public void setRepeatDays(List<Integer> repeatDays) {
        this.repeatDaysStr = V2ArrayUtil.getJsonArrData(repeatDays);
    }

    public Integer getRepeatIndex() {
        return repeatIndex;
    }

    public void setRepeatIndex(Integer repeatIndex) {
        this.repeatIndex = repeatIndex;
    }

    public Boolean getUsing() {
        return isUsing;
    }

    public void setUsing(Boolean using) {
        isUsing = using;
    }

    public Float getNoiseLevel() {
        return noiseLevel;
    }

    public void setNoiseLevel(Float noiseLevel) {
        this.noiseLevel = noiseLevel;
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
                ", repeatDaysStr='" + repeatDaysStr + '\'' +
                ", repeatIndex=" + repeatIndex +
                ", isUsing=" + isUsing +
                ", noiseLevel=" + noiseLevel +
                '}';
    }

    public Boolean getIsRepeatAlarm() {
        return this.isRepeatAlarm;
    }

    public void setIsRepeatAlarm(Boolean isRepeatAlarm) {
        this.isRepeatAlarm = isRepeatAlarm;
    }

    public Boolean getIsUsing() {
        return this.isUsing;
    }

    public void setIsUsing(Boolean isUsing) {
        this.isUsing = isUsing;
    }
}
