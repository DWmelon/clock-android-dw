package com.timediffproject.module.widget;

import android.content.Context;
import android.text.TextUtils;

import com.timediffproject.application.GlobalPreferenceManager;

import java.util.HashMap;

/**
 * Created by melon on 2017/10/11.
 */

public class WidgetManager {

    private Context context;

    private HashMap<Integer,Integer> appIdMapIndex = new HashMap<>();

    public WidgetManager(Context context){
        this.context = context;
    }

    public void init(){
        loadData();
    }

    private void loadData(){
        //"12-0|13-2"
        String indexStr = GlobalPreferenceManager.getAppWidgetIndexs(context);
        if (TextUtils.isEmpty(indexStr)){
            return;
        }
        appIdMapIndex.clear();
        String[] indexArr = indexStr.split("\\|");
        for (String str : indexArr){
            if (TextUtils.isEmpty(str)){
                continue;
            }
            int appId = Integer.parseInt(str.split("-")[0]);
            int index = Integer.parseInt(str.split("-")[1]);
            appIdMapIndex.put(appId,index);
        }
    }

    public void saveData(){
        if (appIdMapIndex.isEmpty()){
            GlobalPreferenceManager.saveAppWidgetIndexs(context,"");
            return;
        }
        String str = "";
        for (Integer appId : appIdMapIndex.keySet()){
            str += appId + "-" + appIdMapIndex.get(appId);
            str += "|";
        }
        GlobalPreferenceManager.saveAppWidgetIndexs(context,str);
    }

    public HashMap<Integer, Integer> getAppIdMapIndex() {
        return appIdMapIndex;
    }

    public void setAppIdMapIndex(HashMap<Integer, Integer> appIdMapIndex) {
        this.appIdMapIndex = appIdMapIndex;
    }
}
