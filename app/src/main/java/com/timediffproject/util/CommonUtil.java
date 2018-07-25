package com.timediffproject.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.timediffproject.application.GlobalPreferenceManager;
import com.timediffproject.constants.Constant;
import com.timediffproject.model.CountryModel;
import com.timediffproject.module.splash.SplashActivity;
import com.timediffproject.origin.MainApplication;

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

}
