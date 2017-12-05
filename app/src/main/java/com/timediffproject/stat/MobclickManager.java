package com.timediffproject.stat;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * Created by melon on 2017/2/15.
 */

public class MobclickManager {

    public static void onEvent(Context context, List keyPath, int value, String label){
        MobclickAgent.onEvent(context, keyPath, value, label);
    }

}
