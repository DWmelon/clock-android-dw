package com.timediffproject.module.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.timediffproject.application.GlobalPreferenceManager;
import com.timediffproject.application.MyClient;
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
import java.util.List;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by melon on 2017/1/10.
 */

public class MyAlarmManager {

    private AlarmStructModel alarmStructModel = new AlarmStructModel();

    private HashMap<Integer,AlarmModel> alarmModelHashMap = new HashMap<>();

    private List<OnLoadAlarmFinishListener> listenerList = new ArrayList<>();

    private boolean isLoadSuccess;

    private Context context;

    public void init(Context context){
        this.context = context;
        isLoadSuccess = false;

        if (GlobalPreferenceManager.isRefreshAlarm(context)){
            GlobalPreferenceManager.setRefreshAlarm(context,false);
            saveAlarmDataToFile();
        }else{
            getAlarmDataFromFile();
        }
    }

    /**
     * 保存闹钟设置信息到文件
     */
    public synchronized void saveAlarmDataToFile(){
        TaskExecutor.getInstance().post(new Runnable() {
            @Override
            public void run() {
                if (alarmStructModel != null) {
                    String path = StorageManager.getInstance().getPackageFiles() + "alarm";
                    FileUtil.writeObjectToPath(alarmStructModel, path);
                }
            }
        });
    }

    /**
     * 从文件中读取闹钟设置信息
     */
    public void getAlarmDataFromFile(){
        TaskExecutor.getInstance().post(new Runnable() {
            @Override
            public void run() {
                synchronized (MyAlarmManager.class) {

                    String path = StorageManager.getInstance().getPackageFiles() + "alarm";
                        Object object = FileUtil.readObjectFromPath(path);

                        if (object != null && object instanceof AlarmStructModel) {
                            try{
                                alarmStructModel = (AlarmStructModel) object;
                                for (AlarmModel model : alarmStructModel.getAlarmModelList()){
                                    alarmModelHashMap.put(model.getRequestCode(),model);
                                }
                            }catch (Exception e){

                            }
                        }
                    isLoadSuccess = true;
                    dispatLoadAlarmListener();
                }
            }
        });
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

    public void addAlarm(AlarmModel model,int index){
        if (model != null){
            model.setUsing(true);
            if (index >= 0){
                alarmStructModel.getAlarmModelList().add(index,model);
            }
            alarmModelHashMap.put(model.getRequestCode(),model);
            saveAlarmDataToFile();
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
            List<AlarmModel> alarmModelList = alarmStructModel.getAlarmModelList();
            for (AlarmModel mo : alarmModelList){
                if (mo.getRequestCode() == model.getRequestCode()){
                    flag = true;
                    alarmModelHashMap.remove(mo.getRequestCode());
                    mo.setUsing(false);
                }
            }
            //删除了闹钟 保存信息
            if (flag){
                saveAlarmDataToFile();
            }
        }
        return flag;
    }

    public boolean removeAlarm(Context context,AlarmModel model){
        return removeAlarm(context,model,true);
    }

    public boolean removeAlarm(Context context,AlarmModel model,boolean isSaveData){
        cancelAlarm(context,model);
        boolean flag = false;
        if (model != null){
            List<AlarmModel> alarmModelList = alarmStructModel.getAlarmModelList();
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
                if (isSaveData){
                    saveAlarmDataToFile();
                }
            }
        }
        return flag;
    }

    /**
     * 一次性闹钟
     * @param context
     */

    public void addOnceAlarm(Context context, AlarmModel model){
        int index = getAlarmStructModel().getAlarmModelList().size();
        addOnceAlarm(context,model,index);
    }

    public void addOnceAlarm(Context context, AlarmModel model,int index){
        Log.i("model",model.toString());
        cancelAlarm(context,model);

        Intent intent = new Intent(context, RingReceiver.class);
        intent.putExtra("id",model.getRequestCode());
        PendingIntent sender = PendingIntent.getBroadcast(
                context, model.getRequestCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

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
                context, model.getRequestCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // We want the alarm to go off 10 seconds from now.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 10);
        // Schedule the alarm!
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP,
                5 * 1000, 60 * 1000, sender);

        addAlarm(model,getAlarmStructModel().getAlarmModelList().size());
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
                context, model==null?0:model.getRequestCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // And cancel the alarm.
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.cancel(sender);

        return closeAlarm(model);

    }


    /**
     * 开机重新设置全部闹钟
     */
    public void recoverAlarm(){
        AlarmStructModel modelM = getAlarmStructModel();
        if (modelM == null || modelM.getAlarmModelList().isEmpty()){
            return;
        }
        List<AlarmModel> alarmModelList = modelM.getAlarmModelList();
        boolean flag = false;
        long nowTime = new Date().getTime();
        for (AlarmModel model : alarmModelList){
            //正在使用中的闹钟重新设置
            if (model.isUsing()){
                //如果闹钟时间已经超过设置时间
                //1.重复闹钟则计算下一次响铃时间，设置闹钟
                //2.单次闹钟则更改使用状态，不设置闹钟
                if (model.getAlarmTime()<=nowTime){
                    flag = true;
                    if (model.isRepeatAlarm()){
                        SetAlarmUtil.culNextAlarmTime(model);
                    }else{
                        model.setUsing(false);
                        continue;
                    }
                }
                addOnceAlarm(MainApplication.getContext(),model,-1);
            }
        }
        if (flag){
            saveAlarmDataToFile();
        }
    }

    public void checkAlarmWithUserCountry(){
        List<CountryModel> countryModelList = MyClient.getMyClient().getSelectManager().getUserCountry();
        List<Long> ids = new ArrayList<>();
        for (CountryModel country : countryModelList){
            ids.add(country.getId());
        }

        HashSet<AlarmModel> removeList = new HashSet<>();
        for (AlarmModel model : alarmStructModel.getAlarmModelList()){
            if (!ids.contains(model.getCityId())){
                removeList.add(model);
            }
        }

        if (!removeList.isEmpty()){
            for (AlarmModel model : removeList){
                removeAlarm(context,model,false);
            }
            saveAlarmDataToFile();
        }

    }

    public AlarmModel getAlarmModelById(Integer id){
        return alarmModelHashMap.get(id);
    }

    public AlarmStructModel getAlarmStructModel() {
        if (alarmStructModel == null){
            getAlarmDataFromFile();
            return alarmStructModel;
        }
        return alarmStructModel;
    }

    public void setAlarmStructModel(AlarmStructModel alarmStructModel) {
        this.alarmStructModel = alarmStructModel;
    }

    public boolean isLoadSuccess() {
        return isLoadSuccess;
    }

    public void setLoadSuccess(boolean loadSuccess) {
        isLoadSuccess = loadSuccess;
    }
}
