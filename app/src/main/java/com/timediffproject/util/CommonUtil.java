package com.timediffproject.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;

import com.timediffproject.R;
import com.timediffproject.application.GlobalPreferenceManager;
import com.timediffproject.application.MyClient;
import com.timediffproject.constants.Constant;
import com.timediffproject.model.CountryModel;
import com.timediffproject.module.splash.SplashActivity;
import com.timediffproject.origin.MainApplication;
import com.umeng.message.common.Const;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by melon on 2018/7/25.
 */

public class CommonUtil {

    public static boolean judgeLanguage(String language){
        if (TextUtils.isEmpty(language)){
            return false;
        }
        String currentLanguage = GlobalPreferenceManager.getString(MainApplication.getContext(),GlobalPreferenceManager.KEY_LANGUAGE);
        if (TextUtils.isEmpty(currentLanguage)){
            if (language.equals(Constant.LANGUAGE_CHINA)){
                GlobalPreferenceManager.setString(MainApplication.getContext(),GlobalPreferenceManager.KEY_LANGUAGE,Constant.LANGUAGE_CHINA);
                return true;
            }else {
                return false;
            }
        }

        return language.equals(currentLanguage);

    }

    /**
     * 更改应用语言
     *
     * @param locale
     */
    public static void changeAppLanguage(Context context,Locale locale) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        Configuration configuration = context.getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        context.getResources().updateConfiguration(configuration, metrics);
    }

    public static void resetAppLanguage(Context context) {
        String language = GlobalPreferenceManager.getString(context,GlobalPreferenceManager.KEY_LANGUAGE);
        if (TextUtils.isEmpty(language)){
            return;
        }
        switch (language){
            case Constant.LANGUAGE_CHINA:{
                changeAppLanguage(context,Locale.SIMPLIFIED_CHINESE);
                break;
            }
            case Constant.LANGUAGE_ENGLISH:{
                changeAppLanguage(context,Locale.US);
                break;
            }
        }

    }

    public static int getWidgetBackStyleRes(){
        int style = GlobalPreferenceManager.getInt(MainApplication.getContext(),GlobalPreferenceManager.KEY_WIDGET_STYLE_INT);
        switch (style){
            case Constant.WIDGET_BLACK:{
                return R.drawable.bg_corner_tran;
            }
            case Constant.WIDGET_TRAN:{
                return R.drawable.bg_empty;
            }
            case Constant.WIDGET_BLUE:{
                return R.drawable.bg_corner_blue;
            }
            case Constant.WIDGET_YELLOW:{
                return R.drawable.bg_corner_yellow;
            }
            case Constant.WIDGET_GREEN:{
                return R.drawable.bg_corner_green;
            }
            case Constant.WIDGET_PINK:{
                return R.drawable.bg_corner_pink;
            }
            default:
                GlobalPreferenceManager.setInt(MainApplication.getContext(),GlobalPreferenceManager.KEY_WIDGET_STYLE_INT, Constant.WIDGET_BLACK);
                return R.drawable.bg_corner_tran;
        }


    }

    public static String getWidgetBackStyleStr(){
        int style = GlobalPreferenceManager.getInt(MainApplication.getContext(),GlobalPreferenceManager.KEY_WIDGET_STYLE_INT);
        switch (style){
            case Constant.WIDGET_BLACK:{
                return MainApplication.getContext().getResources().getString(R.string.setting_widget_style_black);
            }
            case Constant.WIDGET_TRAN:{
                return MainApplication.getContext().getResources().getString(R.string.setting_widget_style_empty);
            }
            case Constant.WIDGET_BLUE:{
                return MainApplication.getContext().getResources().getString(R.string.setting_widget_style_blue);
            }
            case Constant.WIDGET_YELLOW:{
                return MainApplication.getContext().getResources().getString(R.string.setting_widget_style_yellow);
            }
            case Constant.WIDGET_GREEN:{
                return MainApplication.getContext().getResources().getString(R.string.setting_widget_style_green);
            }
            case Constant.WIDGET_PINK:{
                return MainApplication.getContext().getResources().getString(R.string.setting_widget_style_pink);
            }
            default:
                GlobalPreferenceManager.setInt(MainApplication.getContext(),GlobalPreferenceManager.KEY_WIDGET_STYLE_INT, Constant.WIDGET_BLACK);
                return MainApplication.getContext().getResources().getString(R.string.setting_widget_style_empty);
        }


    }

    public static String getDayOfMonthStr(String language, Calendar calendar){
        return getDayOfMonthStr(language,calendar,Calendar.SHORT);
    }

    public static String getDayOfMonthStr(String language, Calendar calendar,int type){
        if (calendar == null){
            return "";
        }
        if (TextUtils.isEmpty(language)){
            return calendar.get(Calendar.DAY_OF_MONTH) + "日";
        }

        switch (language){
            case Constant.LANGUAGE_CHINA:{
                return calendar.get(Calendar.DAY_OF_MONTH) + "日";
            }
            case Constant.LANGUAGE_ENGLISH:{
                return calendar.get(Calendar.DAY_OF_MONTH) + "";
            }
        }
        return calendar.getDisplayName(Calendar.MONTH,type,Locale.CHINA);
    }

    public static String getDayOfWeekStr(String language, Calendar calendar){
        return getDayOfWeekStr(language,calendar,Calendar.SHORT);
    }

    public static String getDayOfWeekStr(String language, Calendar calendar,int type){
        if (calendar == null){
            return "";
        }
        if (TextUtils.isEmpty(language)){
            return calendar.getDisplayName(Calendar.DAY_OF_WEEK,type,Locale.CHINA);
        }

        switch (language){
            case Constant.LANGUAGE_CHINA:{
                return calendar.getDisplayName(Calendar.DAY_OF_WEEK,type,Locale.CHINA);
            }
            case Constant.LANGUAGE_ENGLISH:{
                return calendar.getDisplayName(Calendar.DAY_OF_WEEK,type,Locale.US);
            }
        }
        return calendar.getDisplayName(Calendar.MONTH,type,Locale.CHINA);
    }

    public static String getMonthStr(String language, Calendar calendar){
        return getMonthStr(language,calendar,Calendar.SHORT);
    }

    public static String getMonthStr(String language, Calendar calendar,int type){
        if (calendar == null){
            return "";
        }
        if (TextUtils.isEmpty(language)){
            return calendar.getDisplayName(Calendar.MONTH,type,Locale.CHINA);
        }

        switch (language){
            case Constant.LANGUAGE_CHINA:{
                return calendar.getDisplayName(Calendar.MONTH,type,Locale.CHINA);
            }
            case Constant.LANGUAGE_ENGLISH:{
                return calendar.getDisplayName(Calendar.MONTH,type,Locale.US);
            }
        }
        return calendar.getDisplayName(Calendar.MONTH,type,Locale.CHINA);
    }

    public static String getCityNameByLanguage(CountryModel model){
        String language = GlobalPreferenceManager.getString(MainApplication.getContext(),GlobalPreferenceManager.KEY_LANGUAGE);
        return getCityNameByLanguage(language,model);
    }

    public static String getCityNameByLanguage(String language, CountryModel model){
        if (TextUtils.isEmpty(language)){
            return model.getCityName();
        }

        switch (language){
            case Constant.LANGUAGE_CHINA:{
                return model.getCityName();
            }
            case Constant.LANGUAGE_ENGLISH:{
                return model.getCityNameE();
            }
        }
        return model.getCityName();
    }

    public static String getNationNameByLanguage(String language, CountryModel model){
        if (TextUtils.isEmpty(language)){
            return model.getNationName();
        }

        switch (language){
            case Constant.LANGUAGE_CHINA:{
                return model.getNationName();
            }
            case Constant.LANGUAGE_ENGLISH:{
                return model.getNationNameE();
            }
        }
        return model.getNationName();
    }

    public static String getCityEByCityC(String cityName){
        return MyClient.getMyClient().getDataManager().getCityNameE(cityName);
    }

    public static SpannableString getSpannableString(CharSequence str, Object what, int start, int end) {
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(what, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

}
