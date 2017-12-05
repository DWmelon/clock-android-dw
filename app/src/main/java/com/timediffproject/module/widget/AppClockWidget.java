package com.timediffproject.module.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.timediffproject.R;
import com.timediffproject.application.MyClient;
import com.timediffproject.model.CountryModel;
import com.timediffproject.module.select.SelectManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by melon on 2017/10/4.
 */

public class AppClockWidget extends AppWidgetProvider {

//    public static final String UPDATE_STATUS_FROM_WIDGET_START = "update_widget_change_country";
    public static final String UPDATE_STATUS_FROM_WIDGET_START = "update_widget_change_country2";


    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)) {
            // 改变widget外观

            ComponentName componentName = new ComponentName(context,AppClockWidget.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            onUpdate(context,manager,manager.getAppWidgetIds(componentName));
//            Intent intent2 = new Intent(context,AppClockWidget.class);
//            Bundle bundle = new Bundle();
//            bundle.putIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS,AppWidgetManager.getInstance(context).getAppWidgetIds(componentName));
//            intent2.putExtras(bundle);
//            intent2.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//            context.sendBroadcast(intent2);
//            updateWidgets(context);

        } else {
            super.onReceive(context, intent);// 这里一定要添加，eles部分，不然，onReceive不会去调用其它的方法。但是如果把这条语句放在外面，就会每次运行onUpdate,onDeleted等方法，就会运行两次，因为UPDATE_ACTION.equals(action)配置成功会运行一次，super.onReceive(context,
            // intent)配置成功又会运行一次，后都是系统自定义的。
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        //widget被从屏幕移除
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        //最后一个widget被从屏幕移除
//        Intent intent = new Intent(context,WidgetTimerService.class);
//        context.startService(intent);


        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            ComponentName componentName = new ComponentName(context,AppClockWidget.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
    }


    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        //widget添加到屏幕上执行
        context.stopService(new Intent(context,WidgetTimerService.class));
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        Intent intent=new Intent(context ,WidgetTimerService.class);
//        PendingIntent refreshIntent=PendingIntent.getService(context, 0, intent, 0);
//        AlarmManager alarm=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//        alarm.setRepeating(AlarmManager.RTC, 5000, 60000, refreshIntent);//每分1次
        context.startService(intent);

    }



}
