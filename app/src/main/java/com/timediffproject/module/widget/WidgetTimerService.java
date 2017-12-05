package com.timediffproject.module.widget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.timediffproject.R;
import com.timediffproject.application.MyClient;
import com.timediffproject.model.CountryModel;
import com.timediffproject.module.select.SelectManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by melon on 2017/10/4.
 */

public class WidgetTimerService extends Service {

    private Timer mTimer;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private int index = 0;

    private HashMap<Integer,Integer> widgetIndexs;

    String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(AppClockWidget.UPDATE_STATUS_FROM_WIDGET_START.equals(intent.getAction())){

                SelectManager selectManager = MyClient.getMyClient().getSelectManager();
                int appId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,0);
                int index = widgetIndexs.get(appId);
                if (!selectManager.getUserCountry().isEmpty()){
                    index = (index + 1)%selectManager.getUserCountry().size();
                }else{
                    index = 0;
                }
                widgetIndexs.put(appId,index);
                MyClient.getMyClient().getWidgetManager().saveData();

                handleUpdate(getApplicationContext(),appId);
            }
        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppClockWidget.UPDATE_STATUS_FROM_WIDGET_START);
        registerReceiver(receiver, filter);

        // TODO: 2017/10/11 读取位置
        widgetIndexs = MyClient.getMyClient().getWidgetManager().getAppIdMapIndex();

        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handleUpdate(getApplicationContext(),-1);
            }
        }, 0, 5000);
    }


    private void handleUpdate(Context context, int appId){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        if (appId == -1){
            ComponentName componentName = new ComponentName(context,AppClockWidget.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);

            for (int appWidgetId : appWidgetIds) {
                updateProcess(context, appWidgetId);
            }
        }else{
            updateProcess(context,appId);
        }


    }

    private void updateProcess(Context context,int appWidgetId){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_clock_widget);
        onUpdate(context,appWidgetId,remoteViews);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    private void onUpdate(Context context,int appWidgetId,RemoteViews rv){

        SelectManager selectManager = MyClient.getMyClient().getSelectManager();

        int index;
        if (!widgetIndexs.containsKey(appWidgetId)){
            widgetIndexs.put(appWidgetId,0);
            index = 0;
        }else{
            index = widgetIndexs.get(appWidgetId);
        }


        if (selectManager.getUserCountry().isEmpty() || selectManager.getUserCountry().size()<=index){
            updateEmpty(rv);
        }else{
            CountryModel model = selectManager.getUserCountry().get(index);
            updateContent(context,appWidgetId,rv,model);
        }

    }

    private void updateContent(Context context,int appWidgetId,RemoteViews rv,CountryModel model){
        Date date = MyClient.getMyClient().getTimeManager().getTime(model.getDiffTime());
        model.setNowDate(date);
        SimpleDateFormat dff = new SimpleDateFormat("HH: mm");
        String str = dff.format(date);
        rv.setTextViewText(R.id.tv_app_widget_time_small, str);

        SimpleDateFormat myFmt2=new SimpleDateFormat("yyyy-MM-dd");
        String strTime1 = myFmt2.format(date);
        String month = strTime1.split("-")[1];
        String day = strTime1.split("-")[2];
        //2004-12-16 17:24:27
        String strTime2 = date.toString();
        String monthE = strTime2.split(" ")[1];
        //16 Dec 2004 09:24:27 GMT

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        rv.setTextViewText(R.id.tv_app_widget_time_big, month+"月"+day+"日"+"-"+weekDays[calendar.get(Calendar.DAY_OF_WEEK)-1]);


        rv.setTextViewText(R.id.tv_app_widget_country_name, model.getNationName());
        rv.setTextViewText(R.id.tv_app_widget_city_name, model.getCityName());


        Intent intent2 = new Intent();
        intent2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent2.setAction(AppClockWidget.UPDATE_STATUS_FROM_WIDGET_START);
        PendingIntent doubleClick = PendingIntent.getBroadcast(this, appWidgetId, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setOnClickPendingIntent(R.id.iv_change_country, doubleClick);
    }

    private void updateEmpty(RemoteViews rv){
        rv.setTextViewText(R.id.tv_app_widget_time_small, "");
        rv.setTextViewText(R.id.tv_app_widget_time_big, "");
        rv.setTextViewText(R.id.tv_app_widget_country_name, "");
        rv.setTextViewText(R.id.tv_app_widget_city_name, "");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        mTimer = null;
    }

}
