package com.timediffproject.open;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.timediffproject.application.MyClient;

/**
 * Created by melon on 2017/3/6.
 */

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        MyClient.getMyClient().getMyAlarmManager().recoverAlarm();
    }

}
