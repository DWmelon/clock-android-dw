package com.timediffproject.stat;

import android.content.Context;

import com.timediffproject.application.DebugConfig;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by eddy on 2015/4/20.
 */
public class StatManager {

    public static void mobclickAgentStart(String name){

        if(DebugConfig.isDebug){
            return;
        }

        MobclickAgent.onPageStart(name);
    }

    public static void mobclickAgentEnd(String name){

        if(DebugConfig.isDebug){
            return;
        }

        MobclickAgent.onPageEnd(name);
    }

    public static void statEventNum(Context context, String eventId){
        if(DebugConfig.isDebug){
            return;
        }
        if (context == null){
            return;
        }

        MobclickAgent.onEvent(context,eventId);
    }

    public static void statEventNum(Context context, String eventId, String value){

        if(DebugConfig.isDebug){
            return;
        }

        List<String> list = new ArrayList<>();
        list.add(value);
        statEventNum(context,eventId,list);
    }

    public static void statEventNum(Context context, String eventId, List<String> list){

        if(DebugConfig.isDebug){
            return;
        }

        if (list == null || list.isEmpty()){
            return;
        }
        Map<String,String> map;
        for (String str : list){
            map = new HashMap<>();
            map.put("event",str);
            MobclickAgent.onEvent(context,eventId,map);
        }
    }

}
