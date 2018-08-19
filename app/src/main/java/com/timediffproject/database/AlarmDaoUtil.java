package com.timediffproject.database;

import com.timediffproject.application.MyClient;

import java.util.List;

import database.com.timediffproject.AlarmModelDao;

/**
 * Created by melon on 2018/1/2.
 */

public class AlarmDaoUtil {

    private static AlarmModelDao dao;

    private static void checkDao(){
        if (dao == null){
            dao = MyClient.getMyClient().getDatabaseManager().getDaoSession().getAlarmModelDao();
        }
    }

    public static List<AlarmModel> loadAllAlarm(){
        checkDao();
        return dao.loadAll();
    }

    public static AlarmModel loadAlarm(Long id){
        checkDao();
        return dao.load(id);
    }

    public static void insertAlarm(AlarmModel model){
        checkDao();
        Long id = dao.insert(model);
    }

    public static void removeAlarm(Long id){
        checkDao();
        dao.deleteByKey(id);
    }

    public static void removeAllAlarm(){
        checkDao();
        dao.deleteAll();
    }

    public static void updateAlarm(AlarmModel model){
        checkDao();
        dao.update(model);
    }

}
