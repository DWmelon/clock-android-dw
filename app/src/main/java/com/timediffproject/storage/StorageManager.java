package com.timediffproject.storage;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by eddy on 2015/4/20.
 */
public final class StorageManager {

    //    public static final String DIR_ROOT_DIR_NAME = "iPIN";
    public static final String DIR_APP_ROOT_NAME = "zhiyuan";
    public static final String DIR_NEW_VERSION_NAME = "apk";
    public static final String DIR_IMAGE_CACHE_NAME = "imgCache";
    public static final String DIR_STRING_CACHE_NAME = "strCache";
    public static final String DIR_FAV_CACHE_NAME = "favCache";
    public static final String DIR_LOG_CACHE_NAME = "log";
    private static final String PATCH_NAME = "iPIN.apatch";

    public static final String FILE_NEW_VERSION_NAME = "wanmeizhiyuan_";
    public static final String SUFFIX_NEW_VERSION = ".apk";

    public static final String PATH_NEW_VERSION = new StringBuilder(DIR_APP_ROOT_NAME).append(File.separator)
            .append(DIR_NEW_VERSION_NAME).append(File.separator).toString();
    public static final String PATH_IMG_CACHE = new StringBuilder(DIR_APP_ROOT_NAME).append(File.separator).
            append(DIR_IMAGE_CACHE_NAME).append(File.separator).toString();
    public static final String PATH_STRING_CACHE = new StringBuilder(DIR_APP_ROOT_NAME).append(File.separator).
            append(DIR_STRING_CACHE_NAME).append(File.separator).toString();
    public static final String PATH_FAV_CACHE = new StringBuilder(DIR_APP_ROOT_NAME).append(File.separator).
            append(DIR_FAV_CACHE_NAME).append(File.separator).toString();
    public static final String PATH_LOG_CACHE = new StringBuilder(DIR_APP_ROOT_NAME).append(File.separator).
            append(DIR_LOG_CACHE_NAME).append(File.separator).toString();


    private static String mExternalRootDirPath;
    private static String mRootDirPath;
    private static String mPrivateCacheDir;
    private static String mFilesDir;
    private static String mLogFilePath;

    private static StorageManager mInstance;
    private Context mContext;

    private StorageManager() {

    }

    public static synchronized StorageManager getInstance() {
        if (mInstance == null) {
            mInstance = new StorageManager();
        }

        return mInstance;
    }

    public void init(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("the param context is null,please check again");
        }

        mContext = context.getApplicationContext();
        mPrivateCacheDir = mContext.getCacheDir().getAbsolutePath() + File.separator;
        mFilesDir = mContext.getFilesDir() + File.separator;

        if (isExternalStorageAvailable()) {
            File sdRootFile = Environment.getExternalStorageDirectory();
            if (sdRootFile != null && sdRootFile.exists()) {
                mExternalRootDirPath = sdRootFile.getAbsolutePath() + File.separator;
            }

            if (mContext.getExternalCacheDir() != null) {
                mRootDirPath = mContext.getExternalCacheDir().getPath() + File.separator;
            } else {
                if (!TextUtils.isEmpty(mExternalRootDirPath)) {
                    mRootDirPath = mExternalRootDirPath;
                } else {
                    mRootDirPath = mPrivateCacheDir;
                }
            }

        } else {
            mRootDirPath = mPrivateCacheDir;//mContext.getCacheDir().getAbsolutePath();
        }

//        File data = Environment.getDataDirectory();

        initAllDir();

    }

    private void initAllDir() {

        //跟目录
//        File rootDir = new File(new StringBuilder(mRootDirPath).append(File.separator).append(DIR_ROOT_DIR_NAME).append(File.separator).toString());
//        if(!rootDir.exists()){
//         rootDir.mkdirs();
//        }

        //app目录
        File appDir = new File(new StringBuilder(mRootDirPath).append(DIR_APP_ROOT_NAME).append(File.separator).toString());
        if (!appDir.exists()) {
            appDir.mkdirs();
        }

        if (!TextUtils.isEmpty(mExternalRootDirPath)) {
            //sd卡可用
            File externalAppDir = new File(new StringBuilder(mExternalRootDirPath).append(DIR_APP_ROOT_NAME).append(File.separator).toString());
            if (!externalAppDir.exists()) {
                externalAppDir.mkdirs();
            }

            //新版apk目录
            File dirFile = new File(externalAppDir, DIR_NEW_VERSION_NAME + File.separator);
            if (!dirFile.exists()) {
                dirFile.mkdir();
            }

        } else {
            //sd卡不可用
//            新版apk目录
            File dirFile = new File(appDir, DIR_NEW_VERSION_NAME);
            if (!dirFile.exists()) {
                dirFile.mkdir();
            }
        }

//


        //图片缓存目录
        File imgDir = new File(appDir, DIR_IMAGE_CACHE_NAME);
        if (!imgDir.exists()) {
            imgDir.mkdir();
        }

        //json缓存
        File strDir = new File(appDir, DIR_STRING_CACHE_NAME);
        if (!strDir.exists()) {
            strDir.mkdir();
        }

        //收藏数据缓存
        File favDir = new File(appDir, DIR_FAV_CACHE_NAME);
        if (!favDir.exists()) {
            favDir.mkdir();
        }

        File logDir = new File(appDir, DIR_LOG_CACHE_NAME);
        if (!logDir.exists()) {
            logDir.mkdirs();
        }


    }

    public void release() {

    }

    public static String getRootDirPath() {
        return mRootDirPath;
    }

    public static boolean isExternalStorageAvailable() {
        if (!Environment.isExternalStorageRemovable()) {
            return true;
        } else {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                return true;
            } else {
                return false;
            }
        }
    }


    public static String getPackageCacheRoot() {
        return mPrivateCacheDir;
    }

    public static String getPackageFiles() {
        return mFilesDir;
    }

    public static String getImgCachePath() {
        return getImgCacheDirPath();
    }

    public static String getStrCachePath() {
        return getStrCacheDirPath();
    }

    public static String getFavCachePahth() {
        return getFavCacheDirPath();
    }

    public static String getLogCachePath() {
        return getLogFileDirPath();
    }

    public static String getNewVerisonPath(String version) {

        if (TextUtils.isEmpty(version)) {
            version = "";
        }

        StringBuilder sb = new StringBuilder(getNewVersionApkDirPath()).append(FILE_NEW_VERSION_NAME).append(version).append(SUFFIX_NEW_VERSION);

        return sb.toString();

    }


    private static String getNewVersionApkDirPath() {
        return TextUtils.isEmpty(mExternalRootDirPath) ? new StringBuilder(mRootDirPath).append(PATH_NEW_VERSION).toString() : new StringBuilder(mExternalRootDirPath).append(PATH_NEW_VERSION).toString();
    }

    public static String getPatchPath() {
        return TextUtils.isEmpty(mExternalRootDirPath) ? new StringBuilder(mRootDirPath).append(PATCH_NAME).toString() : new StringBuilder(mExternalRootDirPath).append(PATCH_NAME).toString();
    }

    private static String getImgCacheDirPath() {
        return new StringBuilder(mRootDirPath).append(PATH_IMG_CACHE).toString();
    }

    private static String getStrCacheDirPath() {
        return new StringBuilder(mRootDirPath).append(PATH_STRING_CACHE).toString();
    }

    private static String getFavCacheDirPath() {
        return new StringBuilder(mRootDirPath).append(PATH_FAV_CACHE).toString();
    }

    private static String getLogFileDirPath() {
        return new StringBuilder(mRootDirPath).append(PATH_LOG_CACHE).toString();
    }


}
