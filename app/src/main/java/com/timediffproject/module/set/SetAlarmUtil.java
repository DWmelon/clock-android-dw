package com.timediffproject.module.set;

import android.util.Log;
import android.widget.Toast;

import com.timediffproject.application.MyClient;
import com.timediffproject.database.AlarmModel;
import com.timediffproject.origin.MainApplication;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by melon on 2017/2/20.
 */

public class SetAlarmUtil {

    public static AlarmModel culAlarmTime(AlarmModel model,float diffTime,int hour,int min){
        AlarmModel result;
        if (!model.getRepeatAlarm()){
            result = culAlarmTimeOnce(model,diffTime,hour,min);
        }else{
            result = culAlarmTimeRecycle(model,diffTime,hour,min);
        }
        toastRingTip(result.getAlarmTime()-result.getSettingTime());
        return result;
    }

    private static AlarmModel culAlarmTimeOnce(AlarmModel model,float diffTime,int hour,int min){
        Date targetDate = getRealTime(diffTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(targetDate);
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,min);
        long targetTime = calendar.getTimeInMillis();

        //真实的闹钟响铃时间
        Date date = getAlarmTime(diffTime,targetTime);

        //获取当前时间
        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTime(new Date());
        nowCalendar.set(Calendar.SECOND,0);
        nowCalendar.set(Calendar.MILLISECOND,0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log.i("time",simpleDateFormat.format(nowCalendar.getTime()));
        //获取目标时间
        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.setTime(date);
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log.i("time",simpleDateFormat1.format(targetCalendar.getTime()));
        //对比 看是否要日期加一天
        if (targetCalendar.compareTo(nowCalendar)<=0){
            targetCalendar.add(Calendar.DAY_OF_MONTH,1);
        }

        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log.i("time",simpleDateFormat2.format(targetCalendar.getTime()));
        model.setSettingTime(nowCalendar.getTimeInMillis());
        model.setAlarmTime(targetCalendar.getTimeInMillis());
        return model;
    }

    private static AlarmModel culAlarmTimeRecycle(AlarmModel model,float diffTime,int hour,int min){

        culAlarmTimeOnce(model,diffTime,hour,min);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(model.getAlarmTime()));
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        //如果闹钟时间当天有被设置，则当天闹，不然就取最近的一天
        if (model.getRepeatDays().contains(day)){
            int index = model.getRepeatDays().indexOf(day);
            model.setRepeatIndex(index);
        }else{
            culNextAlarmTime(model);
        }

        return model;
    }

    public static AlarmModel culNextAlarmTime(AlarmModel model){
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log.e("time1",simpleDateFormat1.format(model.getAlarmTime()));

        Calendar originCalendar = Calendar.getInstance();
        originCalendar.setTime(new Date(model.getAlarmTime()));

        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTime(new Date());

        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.setTime(nowCalendar.getTime());
        targetCalendar.set(Calendar.HOUR,originCalendar.get(Calendar.HOUR));
        targetCalendar.set(Calendar.MINUTE,originCalendar.get(Calendar.MINUTE));
        targetCalendar.set(Calendar.MILLISECOND,0);

        //获取当前是星期几-day、获取闹钟响铃是星期几.
        int day = targetCalendar.get(Calendar.DAY_OF_WEEK);
        int i = 0;
        for (;i<model.getRepeatDays().size();i++){
            if (model.getRepeatDays().get(i)==day){
                if (nowCalendar.getTime().compareTo(targetCalendar.getTime())>0){
                    break;
                }

            }
            if (day < model.getRepeatDays().get(i)){
                break;
            }
        }
        if (i == model.getRepeatDays().size()){
            i = 0;
        }


        model.setRepeatIndex(i);
        //获取目标时间
        targetCalendar.set(Calendar.DAY_OF_WEEK,model.getRepeatDays().get(model.getRepeatIndex()));




        //对比 看是否要日期加一个星期
        if (targetCalendar.compareTo(nowCalendar)<=0){
            targetCalendar.add(Calendar.WEEK_OF_YEAR,1);
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log.e("time",simpleDateFormat.format(targetCalendar.getTime()));
        model.setAlarmTime(targetCalendar.getTimeInMillis());
        return model;

    }

    /**
     * 重启时钟
     * @param model
     * @return
     */
    public static AlarmModel culRestartAlarm(AlarmModel model){

        // 这时候model.getAlarmTime()已经是目标地区的闹钟时间，不用再计算，只需要判断需不需要加一天

        //当前时间
//        CountryModel countryModel = MyClient.getMyClient().getSelectManager().getNationById(model.getCityId());
//        Date realDate = getRealTime(countryModel.getDiffTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        //原本的目标时间
        Calendar alarmCalendar = Calendar.getInstance();
        alarmCalendar.setTime(new Date(model.getAlarmTime()));

        //今日目标时间
        Calendar targetCalender = Calendar.getInstance();
        targetCalender.setTime(new Date());
        targetCalender.set(Calendar.HOUR_OF_DAY,alarmCalendar.get(Calendar.HOUR_OF_DAY));
        targetCalender.set(Calendar.MINUTE,alarmCalendar.get(Calendar.MINUTE));
        targetCalender.set(Calendar.SECOND,0);
        targetCalender.set(Calendar.MILLISECOND,0);

        //根据是否是重复闹钟进行不同的设置
        if (model.getRepeatAlarm()){
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            boolean flag = false;
            int index = 0;
            int targetDay = 0;
            for (;index < model.getRepeatDays().size();index++){
                targetDay = model.getRepeatDays().get(index);
                if (day<=targetDay){
                    //跳过的判断
                    if (day == targetDay){
                        int targetHour = targetCalender.get(Calendar.HOUR_OF_DAY);
                        int targetMin = targetCalender.get(Calendar.MINUTE);
                        int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
                        int nowMin = calendar.get(Calendar.MINUTE);
                        if (targetHour<nowHour||targetHour==nowHour&&targetMin<=nowMin){
                            continue;
                        }
                    }
                    flag = true;
                    break;
                }
            }
            //没有找到则取第一个，加一个星期
            if (!flag){
                index = 0;
                targetDay = model.getRepeatDays().get(0);
                targetCalender.add(Calendar.WEEK_OF_YEAR,1);
            }
            model.setRepeatIndex(index);
            targetCalender.set(Calendar.DAY_OF_WEEK,targetDay);



        }else{
            //看目标时间是否要加1天
            if (calendar.compareTo(targetCalender)>0){
                targetCalender.add(Calendar.DAY_OF_YEAR,1);
            }
        }


        long diffMillis = targetCalender.getTimeInMillis() - calendar.getTimeInMillis();
        toastRingTip(diffMillis);
        model.setAlarmTime(targetCalender.getTimeInMillis());
        model.setSettingTime(calendar.getTimeInMillis());
        return model;
    }

    /**
     * 响铃提示Toast
     * @param diffMillis
     */
    public static void toastRingTip(long diffMillis){
        int diffHour = (int) (diffMillis/1000/60/60);
        int diffMin = (int) (diffMillis/1000/60)-diffHour*60;
        Toast.makeText(MainApplication.getContext(),"闹钟将在"+diffHour+"小时"+diffMin+"分钟后响铃",Toast.LENGTH_LONG).show();
    }

    /**
     * 计算真实目的地时间时间，修正手机时间误差
     * @param diffTime
     * @return
     */
    public static Date getRealTime(float diffTime){
        long fixTime = new Date().getTime() - MyClient.getMyClient().getTimeManager().getBeijingTime().getTime();
        Date nowDate = MyClient.getMyClient().getTimeManager().getTime(diffTime);
        return new Date(nowDate.getTime()+fixTime);
    }

    /**
     * 计算真实响铃的本地时间，修正手机时间误差
     * @param diffTime
     * @return
     */
    public static Date getAlarmTime(float diffTime,long alarmTime){
        long fixTime = new Date().getTime() - MyClient.getMyClient().getTimeManager().getBeijingTime().getTime();
        alarmTime = alarmTime - (long) diffTime *60 *60*1000 + fixTime;
        return new Date(alarmTime);
    }

    /**
     * 根据传入的时间和时差重算时间
     * @param diffTime 目的地与北京的时差
     * @param alarmTime 本机响铃的时间
     * @return 目的地的响铃时间
     */
    public static Date getModifyTime(float diffTime,long alarmTime){
//        long fixTime = new Date().getTime() - MyClient.getMyClient().getTimeManager().getBeijingTime().getTime();
//        //alarmTime + fixTime = 北京时间
//        //北京时间 + (long) diffTime *60 *60*1000 = 目标地时间
//        alarmTime = alarmTime + fixTime + (long) diffTime *60 *60*1000;
//        return new Date(alarmTime);


        alarmTime = alarmTime + (long) diffTime *60 *60*1000;
        return new Date(alarmTime);
    }

    public static long geiBeijingAlarmTime(long alarmTime){
        return alarmTime + MyClient.getMyClient().getTimeManager().getDiffTime();
    }

}
