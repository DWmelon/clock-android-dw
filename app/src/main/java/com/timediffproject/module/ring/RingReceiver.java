package com.timediffproject.module.ring;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.timediffproject.application.MyClient;
import com.timediffproject.constants.ParamConstants;
import com.timediffproject.module.ring.RingActivity;
import com.timediffproject.origin.MainApplication;

/**
 * Created by melon on 2017/1/10.
 */

public class RingReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("id",0);
        String type;
        if (id == 0){
            type = ParamConstants.VALUE_ALARM_TYPE_PAUSE;
        }else{
            type = ParamConstants.VALUE_ALARM_TYPE_RING;
        }

        Intent intent1 = new Intent(context, RingActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent1.putExtra(ParamConstants.KEY_ALARM_TYPE_PAUSE,type);
        intent1.putExtra("id",id);
        context.startActivity(intent1);
//        AlarmModel model = MyClient.getMyClient().getMyAlarmManager().getAlarmModelById(id);

    }

}
