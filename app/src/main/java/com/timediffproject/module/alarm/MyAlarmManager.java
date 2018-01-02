package com.timediffproject.module.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.timediffproject.application.GlobalPreferenceManager;
import com.timediffproject.application.MyClient;
import com.timediffproject.database.AlarmDaoUtil;
import com.timediffproject.database.AlarmModel;
import com.timediffproject.model.CountryModel;
import com.timediffproject.module.ring.RingReceiver;
import com.timediffproject.module.set.SetAlarmUtil;
import com.timediffproject.origin.MainApplication;
import com.timediffproject.storage.StorageManager;
import com.timediffproject.storage.TaskExecutor;
import com.timediffproject.util.FileUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import database.com.timediffproject.AlarmModelDao;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by melon on 2017/1/10.
 */

public class MyAlarmManager {

    private List<AlarmModel> alarmModelList = new ArrayList<>();

    private HashMap<Long,AlarmModel> alarmModelHashMap = new HashMap<>();

    private List<OnLoadAlarmFinishListener> listenerList = new ArrayList<>();

    private boolean isLoadSuccess;

    private Context context;

    public void init(Context context){
        this.context = context;
        isLoadSuccess = false;

        if (GlobalPreferenceManager.isRefreshAlarm(context)){
            GlobalPreferenceManager.setRefreshAlarm(context,false);
            AlarmDaoUtil.removeAllAlarm();
        }else{
            getAlarmDataFromFile();
        }
    }

    public List<AlarmModel> getAlarmModelList() {
        if (alarmModelList.isEmpty()){
            return alarmModelList;
        }

        List<CountryModel> countryModelList = MyClient.getMyClient().getSelectManager().getUserCountry();
        if (countryModelList == null || countryModelList.isEmpty()){
            alarmModelList.clear();
            AlarmDaoUtil.removeAllAlarm();
            return alarmModelList;
        }

        Iterator iterator = alarmModelList.iterator();
        while (iterator.hasNext()){
            AlarmModel model = (AlarmModel) iterator.next();
            Long cityId = model.getCityId();
            boolean isHasCity = false;
            for (CountryModel countryModel : countryModelList){
                if (countryModel.getId().equals(cityId)){
                    isHasCity = true;
                }
            }
            if (!isHasCity){
                alarmModelList.remove(model);
                AlarmDaoUtil.removeAlarm(model.getRequestCode());
            }
        }

        return alarmModelList;
    }

    public void getAlarmDataFromFile(){
        alarmModelList = AlarmDaoUtil.loadAllAlarm();
        for (AlarmModel model : alarmModelList){
            alarmModelHashMap.put(model.getRequestCode(),model);
        }
        isLoadSuccess = true;
        dispatLoadAlarmListener();

    }


    public void registerLoadAlarmListener(OnLoadAlarmFinishListener listener){
        listenerList.add(listener);
    }

    public void unregisterLoadAlarmListener(OnLoadAlarmFinishListener listener){
        if (listenerList.contains(listener)){
            listenerList.remove(listener);
        }
    }

    private void dispatLoadAlarmListener(){
        for (OnLoadAlarmFinishListener listener : listenerList){
            listener.loadDataFinish();
        }
    }

    public Integer getRequestCodeInt(AlarmModel model) {
        return Integer.parseInt(model.getRequestCode()+"");
    }

    public void addAlarm(AlarmModel model,int index){
        if (model != null){
            model.setUsing(true);
            if (index >= 0){
                alarmModelList.add(index,model);
            }
            alarmModelHashMap.put(model.getRequestCode(),model);
            AlarmDaoUtil.insertAlarm(model);
        }
    }

    /**
     * 关闭闹钟，但不删除
     * @param model
     * @return
     */
    private boolean closeAlarm(AlarmModel model){
        boolean flag = false;
        if (model != null){
            for (AlarmModel mo : alarmModelList){
                if (mo.getRequestCode().equals(model.getRequestCode())){
                    flag = true;
                    alarmModelHashMap.remove(mo.getRequestCode());
                    mo.setUsing(false);
                    AlarmDaoUtil.updateAlarm(mo);
                }
            }
        }
        return flag;
    }

    public boolean removeAlarm(Context context,AlarmModel model){
        cancelAlarm(context,model);
        boolean flag = false;
        if (model != null){
            AlarmModel removeMo = null;
            for (AlarmModel mo : alarmModelList){
                if (mo.getRequestCode() == model.getRequestCode()){
                    removeMo = mo;
                    flag = true;
                    break;
                }
            }
            //删除了闹钟 保存信息
            if (flag){
                alarmModelList.remove(removeMo);
                alarmModelHashMap.remove(removeMo.getRequestCode());
                AlarmDaoUtil.removeAlarm(removeMo.getRequestCode());
            }
        }
        return flag;
    }

    /**
     * 一次性闹钟
     * @param context
     */

    public void addOnceAlarm(Context context, AlarmModel model){
        int index = alarmModelList.size();
        addOnceAlarm(context,model,index);
    }

    public void addOnceAlarm(Context context, AlarmModel model,int index){
        Log.i("model",model.toString());
        cancelAlarm(context,model);

        Intent intent = new Intent(context, RingReceiver.class);
        intent.putExtra("id",model.getRequestCode());
        PendingIntent sender = PendingIntent.getBroadcast(
                context, getRequestCodeInt(model), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(model.getAlarmTime());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm");
        Log.i("addAlarm",simpleDateFormat.format(calendar.getTime()));

        // Schedule the alarm!
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
        }else {
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
        }

        addAlarm(model,index);

    }

    /**
     * 暂缓闹钟（10分钟）
     * @param context
     */
    public void addPauseAlarm(Context context){

        Intent intent = new Intent(context, RingReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // We want the alarm to go off 10 seconds from now.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE,10);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Log.i("addpauseAlarm",simpleDateFormat.format(calendar.getTime()));

        // Schedule the alarm!
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
        }else {
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
        }

    }

    /**
     * 重复闹钟
     */
    private void addRepeatAlarm(Context context,AlarmModel model){

        Intent intent = new Intent(context,
                RingReceiver.class);
        intent.putExtra("id",model.getRequestCode());
        PendingIntent sender = PendingIntent.getBroadcast(
                context, getRequestCodeInt(model), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // We want the alarm to go off 10 seconds from now.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 10);
        // Schedule the alarm!
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP,
                5 * 1000, 60 * 1000, sender);

        addAlarm(model,alarmModelHashMap.size());
    }

    /**
     * 取消闹钟
     */
    public boolean cancelAlarm(Context context,AlarmModel model){

        if (model!=null){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            Log.i("cancelAlarm",simpleDateFormat.format(new Date(model.getAlarmTime())));
        }

        Intent intent = new Intent(context, RingReceiver.class);

        PendingIntent sender = PendingIntent.getBroadcast(
                context, model==null?0:getRequestCodeInt(model), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // And cancel the alarm.
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.cancel(sender);

        return closeAlarm(model);

    }


    /**
     * 开机重新设置全部闹钟
     */
    public void recoverAlarm(){
        if (alarmModelList == null || alarmModelList.isEmpty()){
            alarmModelList = AlarmDaoUtil.loadAllAlarm();
        }
        long nowTime = new Date().getTime();
        for (AlarmModel model : alarmModelList){
            //正在使用中的闹钟重新设置
            if (model.getUsing()){
                //如果闹钟时间已经超过设置时间
                //1.重复闹钟则计算下一次响铃时间，设置闹钟
                //2.单次闹钟则更改使用状态，不设置闹钟
                if (model.getAlarmTime()<=nowTime){
                    if (model.getRepeatAlarm()){
                        SetAlarmUtil.culNextAlarmTime(model);
                        AlarmDaoUtil.updateAlarm(model);
                    }else{
                        model.setUsing(false);
                        AlarmDaoUtil.updateAlarm(model);
                        continue;
                    }
                }
                addOnceAlarm(MainApplication.getContext(),model,-1);
            }
        }
    }

    public void checkAlarmWithUserCountry(){
        List<CountryModel> countryModelList = MyClient.getMyClient().getSelectManager().getUserCountry();
        List<Long> ids = new ArrayList<>();
        for (CountryModel country : countryModelList){
            ids.add(country.getId());
        }

        HashSet<AlarmModel> removeList = new HashSet<>();
        for (AlarmModel model : alarmModelList){
            if (!ids.contains(model.getCityId())){
                removeList.add(model);
            }
        }

        if (!removeList.isEmpty()){
            for (AlarmModel model : removeList){
                removeAlarm(context,model);
            }
        }

    }

    public AlarmModel getAlarmModelById(Integer id){
        return alarmModelHashMap.get(id);
    }

    public boolean isLoadSuccess() {
        return isLoadSuccess;
    }

    public void setLoadSuccess(boolean loadSuccess) {
        isLoadSuccess = loadSuccess;
    }
}
