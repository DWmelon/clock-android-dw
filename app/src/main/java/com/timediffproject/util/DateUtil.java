package com.timediffproject.util;

import android.content.Context;
import android.view.View;

import com.timediffproject.application.GlobalPreferenceManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by melon on 2017/12/28.
 */

public class DateUtil {

    public static String getHourFormat(Context context, Date date){
        boolean isUse24 = GlobalPreferenceManager.isUse24Hours(context);
        SimpleDateFormat dff;
        String str;
        if (isUse24){
            dff = new SimpleDateFormat("HH:mm");
        }else{
            dff = new SimpleDateFormat("hh:mm");
        }
        str = dff.format(date);
        return str;
    }

    public static String getTimeAP(Date date){
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a",Locale.US);
        String str = format.format(date);
        return str.contains("AM")?"AM":"PM";
    }

}
