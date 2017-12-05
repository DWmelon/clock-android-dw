package com.timediffproject.application;

import android.content.Context;

/**
 * Created by melon on 2017/1/8.
 */

public class GlobalPreferenceManager {

    private static final String PREFE_NAME = "global_prefe";

    private static final String KEY_USER_SELECT_CITY = "user_select_city";

    private static final String KEY_VERSION_NAME = "app_version_name";

    private static final String KEY_VERSION_CODE = "app_version_code";

    private static final String KEY_VERSION_INFO = "app_version_info";

    private static final String KEY_IS_REFRESH_ALARM = "is_refresh_alarm";

    private static final String KEY_APP_WIDGET_INDEXS = "app_widget_index";

    public static void saveUserSelect(Context context, String id) {
        context.getSharedPreferences(PREFE_NAME, Context.MODE_PRIVATE).edit().putString(KEY_USER_SELECT_CITY, id).apply();
    }

    public static String getUserSelect(Context context) {
        return context.getSharedPreferences(PREFE_NAME, Context.MODE_PRIVATE).getString(KEY_USER_SELECT_CITY,"");
    }

    public static void saveVersionCode(Context context, int versionCode) {
        context.getSharedPreferences(PREFE_NAME, Context.MODE_PRIVATE).edit().putInt(KEY_VERSION_CODE, versionCode).apply();
    }

    public static int getVersionCode(Context context) {
        return context.getSharedPreferences(PREFE_NAME, Context.MODE_PRIVATE).getInt(KEY_VERSION_CODE,0);
    }

    public static void saveVersionName(Context context, String versionName) {
        context.getSharedPreferences(PREFE_NAME, Context.MODE_PRIVATE).edit().putString(KEY_VERSION_NAME, versionName).apply();
    }

    public static String getVersionName(Context context) {
        return context.getSharedPreferences(PREFE_NAME, Context.MODE_PRIVATE).getString(KEY_VERSION_NAME,"");
    }

    public static void saveVersionInfo(Context context, String versionInfo) {
        context.getSharedPreferences(PREFE_NAME, Context.MODE_PRIVATE).edit().putString(KEY_VERSION_INFO, versionInfo).apply();
    }

    public static String getVersionInfo(Context context) {
        return context.getSharedPreferences(PREFE_NAME, Context.MODE_PRIVATE).getString(KEY_VERSION_INFO,"");
    }

    public static void setRefreshAlarm(Context context, boolean isRefresh) {
        context.getSharedPreferences(PREFE_NAME, Context.MODE_PRIVATE).edit().putBoolean(KEY_IS_REFRESH_ALARM, isRefresh).apply();
    }

    public static boolean isRefreshAlarm(Context context) {
        return context.getSharedPreferences(PREFE_NAME, Context.MODE_PRIVATE).getBoolean(KEY_IS_REFRESH_ALARM,false);
    }

    public static void saveAppWidgetIndexs(Context context, String versionCode) {
        context.getSharedPreferences(PREFE_NAME, Context.MODE_PRIVATE).edit().putString(KEY_APP_WIDGET_INDEXS, versionCode).apply();
    }

    public static String getAppWidgetIndexs(Context context) {
        return context.getSharedPreferences(PREFE_NAME, Context.MODE_PRIVATE).getString(KEY_APP_WIDGET_INDEXS,"");
    }

}
