package com.timediffproject.module.alarm;

import com.timediffproject.application.MyClient;
import com.timediffproject.model.CountryModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by melon on 2017/1/11.
 */

public class AlarmStructModel implements Serializable {

    private static final long serialVersionUID = -2028633573981320746L;

    List<AlarmModel> alarmModelList = new ArrayList<>();

    public List<AlarmModel> getAlarmModelList() {
        if (alarmModelList.isEmpty()){
            return alarmModelList;
        }

        List<CountryModel> countryModelList = MyClient.getMyClient().getSelectManager().getUserCountry();
        if (countryModelList == null || countryModelList.isEmpty()){
            alarmModelList.clear();
            MyClient.getMyClient().getMyAlarmManager().saveAlarmDataToFile();
            return alarmModelList;
        }

        boolean isChange = false;
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
                isChange = true;
            }
        }

        if (isChange){
            MyClient.getMyClient().getMyAlarmManager().saveAlarmDataToFile();
        }

        return alarmModelList;
    }

    public void setAlarmModelList(List<AlarmModel> alarmModelList) {
        this.alarmModelList = alarmModelList;
    }

    @Override
    public String toString() {
        return "AlarmStructModel{" +
                "alarmModelList=" + alarmModelList +
                '}';
    }
}
