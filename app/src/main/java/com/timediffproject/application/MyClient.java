package com.timediffproject.application;

import android.content.Context;
import android.text.TextUtils;

import com.timediffproject.database.DatabaseManager;
import com.timediffproject.module.alarm.MyAlarmManager;
import com.timediffproject.module.misc.MiscManager;
import com.timediffproject.module.money.MoneyManager;
import com.timediffproject.module.search.SearchManager;
import com.timediffproject.module.select.SelectManager;
import com.timediffproject.module.tempdata.DataManager;
import com.timediffproject.module.widget.WidgetManager;
import com.timediffproject.network.HttpRequestManager;
import com.timediffproject.network.IInterface;
import com.timediffproject.network.IRequest;
import com.timediffproject.storage.StorageManager;

import java.util.HashMap;

/**
 * Created by melon on 2017/1/3.
 */

public class MyClient {

    private static MyClient myClient;


    public static final String SERVICE_HTTP_REQUEST = "httpRequest";

    private HashMap<String, IInterface> mService = new HashMap<String, IInterface>();


    private Context context;

    public static synchronized MyClient getMyClient(){
        if (myClient == null){
            myClient = new MyClient();
        }
        return myClient;
    }

    public void init(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("IpinClient#init,the param context is null,please check again");
        }
        this.context = context;

        StorageManager.getInstance().init(context);

        getSelectManager().init(context);
        getTimeManager().init(context);
        getMyAlarmManager().init(context);
        getDataManager().init(context);
        getWidgetManager().init();
        initModule();
    }

    private void initModule() {
        IRequest request = new HttpRequestManager(context);
        mService.put(MyClient.SERVICE_HTTP_REQUEST, request);
    }

    public IInterface getService(String serviceName) {

        if (TextUtils.isEmpty(serviceName)) {
            return null;
        }

        return mService.get(serviceName);

    }



    private SelectManager selectManager;
    private TimeManager timeManager;
    private MyAlarmManager myAlarmManager;
    private DataManager dataManager;
    private SearchManager searchManager;
    private WidgetManager widgetManager;
    private MiscManager miscManager;
    private MoneyManager moneyManager;
    private DatabaseManager databaseManager;

    public synchronized SelectManager getSelectManager(){
        if (selectManager == null){
            selectManager = new SelectManager();
        }
        return selectManager;
    }

    public synchronized TimeManager getTimeManager(){
        if (timeManager == null){
            timeManager = new TimeManager();
        }
        return timeManager;
    }

    public synchronized MyAlarmManager getMyAlarmManager(){
        if (myAlarmManager == null){
            myAlarmManager = new MyAlarmManager();
        }
        return myAlarmManager;
    }

    public synchronized DataManager getDataManager(){
        if (dataManager == null){
            dataManager = new DataManager();
        }
        return dataManager;
    }

    public synchronized SearchManager getSearchManager(){
        if (searchManager == null){
            searchManager = new SearchManager();
        }
        return searchManager;
    }

    public synchronized WidgetManager getWidgetManager(){
        if (widgetManager == null){
            widgetManager = new WidgetManager(context);
        }
        return widgetManager;
    }

    public synchronized MiscManager getMiscManager(){
        if (miscManager == null){
            miscManager = new MiscManager();
        }
        return miscManager;
    }

    public synchronized MoneyManager getMoneyManager(){
        if (moneyManager == null){
            moneyManager = new MoneyManager();
        }
        return moneyManager;
    }

    public synchronized DatabaseManager getDatabaseManager(){
        if (databaseManager == null){
            databaseManager = new DatabaseManager(context);
        }
        return databaseManager;
    }

}
