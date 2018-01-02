package com.timediffproject.application;

import android.content.Context;
import android.content.Intent;

import com.timediffproject.TimeService;
import com.timediffproject.listener.OnGetTimeCallback;
import com.timediffproject.listener.OnUpdateTimeCallback;
import com.timediffproject.model.CountryModel;
import com.timediffproject.database.AlarmModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by melon on 2017/1/8.
 */

public class TimeManager {


    //北京为准 东八区
    private long diffTime = 0;

    private String dateStr = "";

    private Context context;

    private boolean isLoadFinish = false;

    public void init(Context context){
        this.context = context;
        initTime();
    }

    private void initTime(){
        Date date = getBeijingTime();
        isLoadFinish = true;
        Date nowDate = new Date();
        diffTime = date.getTime() - nowDate.getTime();
        startTimeService(date);
        dispathGetTimeCallBack();


    }

    private void startTimeService(Date date){
        Intent intent = new Intent(context,TimeService.class);
        intent.putExtra("second",date.getSeconds());
        context.startService(intent);
    }

    private void stopTimeService(){
        Intent intent = new Intent(context,TimeService.class);
        context.stopService(intent);
    }

    public Date getBeijingTime(){
        Date date = null;
        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String ee = dff.format(new Date());

        dateStr = ee;

        String[] dateS1 = ee.split(" ");
        String[] dateL1 = dateS1[0].split("-");
        String[] dateL2 = dateS1[1].split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,Integer.valueOf(dateL1[0]));
        calendar.set(Calendar.MONTH,Integer.valueOf(dateL1[1])-1);
        calendar.set(Calendar.DAY_OF_MONTH,Integer.valueOf(dateL1[2]));

        calendar.set(Calendar.HOUR_OF_DAY,Integer.valueOf(dateL2[0]));
        calendar.set(Calendar.MINUTE,Integer.valueOf(dateL2[1]));
        calendar.set(Calendar.SECOND,Integer.valueOf(dateL2[2]));

        date = calendar.getTime();

        if (date == null){
            date = new Date();
        }
        return date;

//        long ld = 0;
//
//        try {
//            URL url=new URL("http://www.bjtime.cn");//取得资源对象
//            URLConnection uc=url.openConnection();//生成连接对象
//            uc.connect(); //发出连接
//            ld =uc.getDate(); //取得网站日期时间
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (ld == 0){
//            return new Date();
//        }else{
//            Date date = new Date(ld);
//            SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            dateStr = dff.format(date);
//            return date;
//        }

    }

    private Date culLocalTime(AlarmModel alarmModel,CountryModel localCountry){
        List<CountryModel> countryModelList = MyClient.getMyClient().getSelectManager().getUserCountry();
        if (countryModelList == null || countryModelList.isEmpty()){
            return null;
        }


        CountryModel alarmCountry = MyClient.getMyClient().getSelectManager().getNationById(alarmModel.getCityId());
        float diffHour = alarmCountry.getDiffTime() - localCountry.getDiffTime();

        Calendar calendarNow = Calendar.getInstance();
        calendarNow.setTime(new Date(alarmModel.getAlarmTime()));
        int hour = (int)diffHour;
        int min = (int) ((diffHour - hour)*60);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(calendarNow.getTime());
        if (min != 0){
            calendar.add(Calendar.MINUTE,min);
        }
        calendar.add(Calendar.HOUR_OF_DAY,hour);

        return calendar.getTime();

    }

    private List<OnGetTimeCallback> onGetTimeCallBacks = new ArrayList<>();

    private List<OnUpdateTimeCallback> onUpdateTimeCallBacks = new ArrayList<>();
    
    public void registerGetTimeCallBack(OnGetTimeCallback callBack){
        onGetTimeCallBacks.add(callBack);
    }

    public void unregisterGetTimeCallBack(OnGetTimeCallback callBack){
        onGetTimeCallBacks.remove(callBack);
    }

    public void dispathGetTimeCallBack(){
        for (OnGetTimeCallback callBack : onGetTimeCallBacks){
            callBack.onGetTimeFinish();
        }
    }

    public void registerUpdateTimeCallBack(OnUpdateTimeCallback callBack){
        onUpdateTimeCallBacks.add(callBack);
    }

    public void unregisterUpdateTimeCallBack(OnUpdateTimeCallback callBack){
        onUpdateTimeCallBacks.remove(callBack);
    }

    public void dispathUpdateTimeCallBack(){
        for (OnUpdateTimeCallback callBack : onUpdateTimeCallBacks){
            callBack.onUpdateTime();
        }
    }

    public Date getTime(float diffHour){
        Date date = getBeijingTime();
        long realTime = date.getTime();
        long diffMs = (long) (diffHour*60*60*1000);
        realTime +=diffMs;
        return new Date(realTime);
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public boolean isLoadFinish() {
        return isLoadFinish;
    }

    public void setLoadFinish(boolean loadFinish) {
        isLoadFinish = loadFinish;
    }

    public long getDiffTime() {
        return diffTime;
    }

    public void setDiffTime(long diffTime) {
        this.diffTime = diffTime;
    }
}
