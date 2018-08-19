package com.timediffproject.module.ring;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.timediffproject.constants.Constant;

/**
 * Created by melon on 2017/1/10.
 */

public class RingReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        long id = intent.getLongExtra("id",0);
        String type;
        if (id == 0){
            type = Constant.VALUE_ALARM_TYPE_PAUSE;
        }else{
            type = Constant.VALUE_ALARM_TYPE_RING;
        }

        Intent intent1 = new Intent(context, RingActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent1.putExtra(Constant.KEY_ALARM_TYPE,type);
        intent1.putExtra("id",id);
        context.startActivity(intent1);
//        AlarmModel model = MyClient.getMyClient().getMyAlarmManager().getAlarmModelById(id);

    }

}
