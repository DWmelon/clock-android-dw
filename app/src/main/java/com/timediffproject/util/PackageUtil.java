package com.timediffproject.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;
import java.security.MessageDigest;
import java.util.List;
import java.util.Map;

/**
 */
public class PackageUtil {
    /**
     * App installation location flags of android system
     */
    public static final int APP_INSTALL_AUTO = 0;
    public static final int APP_INSTALL_INTERNAL = 1;
    public static final int APP_INSTALL_EXTERNAL = 2;

    /**
     * App signature
     */
    private static final String PACKAGE_MD5 ="B4:C6:9E:DD:75:B3:B9:7A:47:37:66:15:26:2B:F1:3B";
    private static final String PACKAGE_SHA1 ="08:5B:57:51:92:0C:59:86:38:A9:F9:C0:E2:42:6D:65:EF:C1:95:5C";
    private static final String PACKAGE_SHA256 ="0A:7C:15:BA:25:1E:5E:BA:D1:49:0C:B8:47:3C:1A:28:11:33:7F:BE:69:0A:74:17:19:15:3C:88:39:8F:AC:AA";

    /**
     * 调用系统安装应用
     */
    public static boolean install(Context context, File file) {
        if (file == null || !file.exists() || !file.isFile()) {
            return false;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        return true;
    }

    /**
     * 调用系统卸载应用
     */
    public static void uninstallApk(Context context, String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        Uri packageURI = Uri.parse("package:" + packageName);
        intent.setData(packageURI);
        context.startActivity(intent);
    }

    /**
     * 打开已安装应用的详情
     */
    public static void goToInstalledAppDetails(Context context, String packageName) {
        Intent intent = new Intent();
        int sdkVersion = Build.VERSION.SDK_INT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.fromParts("package", packageName, null));
        } else {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra((sdkVersion == Build.VERSION_CODES.FROYO ? "pkg"
                    : "com.android.settings.ApplicationPkgName"), packageName);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 获取指定程序信息
     */
    public static ActivityManager.RunningTaskInfo getTopRunningTask(Context context) {
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            // 得到当前正在运行的任务栈
            List<ActivityManager.RunningTaskInfo> runningTasks = am.getRunningTasks(1);
            // 得到前台显示的任务栈
            ActivityManager.RunningTaskInfo runningTaskInfo = runningTasks.get(0);
            return runningTaskInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static int getAppVersionCode(Context context) {
        if (context != null) {
            PackageManager pm = context.getPackageManager();
            if (pm != null) {
                PackageInfo pi;
                try {
                    pi = pm.getPackageInfo(context.getPackageName(), 0);
                    if (pi != null) {
                        return pi.versionCode;
                    }
                } catch (NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }


    /**
     * get app package info
     */
    public static PackageInfo getAppPackageInfo(Context context) {
        if (context != null) {
            PackageManager pm = context.getPackageManager();
            if (pm != null) {
                PackageInfo pi;
                try {
                    return pm.getPackageInfo(context.getPackageName(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * whether context is system application
     */
    public static boolean isSystemApplication(Context context) {
        if (context == null) {
            return false;
        }
        return isSystemApplication(context, context.getPackageName());
    }

    /**
     * whether packageName is system application
     */
    public static boolean isSystemApplication(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null || packageName == null || packageName.length() == 0) {
            return false;
        }
        try {
            ApplicationInfo app = packageManager.getApplicationInfo(packageName, 0);
            return (app != null && (app.flags & ApplicationInfo.FLAG_SYSTEM) > 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取已安装的全部应用信息
     */
    public static List<PackageInfo> getInsatalledPackages(Context context) {
        return context.getPackageManager().getInstalledPackages(0);
    }

    /**
     * 获取指定程序信息
     */
    public static ApplicationInfo getApplicationInfo(Context context, String pkg) {
        try {
            return context.getPackageManager().getApplicationInfo(pkg, 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 启动应用
     */
    public static boolean startAppByPackageName(Context context, String packageName) {
        return startAppByPackageName(context, packageName, null);
    }

    /**
     * 启动应用
     */
    public static boolean startAppByPackageName(Context context, String packageName, Map<String, String> param) {
        PackageInfo pi = null;
        try {
            pi = context.getPackageManager().getPackageInfo(packageName, 0);
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
                resolveIntent.setPackage(pi.packageName);
            }

            List<ResolveInfo> apps = context.getPackageManager().queryIntentActivities(resolveIntent, 0);

            ResolveInfo ri = apps.iterator().next();
            if (ri != null) {
                String packageName1 = ri.activityInfo.packageName;
                String className = ri.activityInfo.name;

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                   ComponentName cn = new ComponentName(packageName1, className);

                intent.setComponent(cn);
                if (param != null) {
                    for (Map.Entry<String, String> en : param.entrySet()) {
                        intent.putExtra(en.getKey(), en.getValue());
                    }
                }
                context.startActivity(intent);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context.getApplicationContext(), "启动失败",
                    Toast.LENGTH_LONG).show();
        }
        return false;
    }


    public static byte[] getPackageSignature(Context context, String packageName) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(
                    packageName, PackageManager.GET_SIGNATURES);
            return packageInfo.signatures[0].toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }


    public static ApplicationInfo getAppInfo(Context context, String pkg) {
        if (context != null && !TextUtils.isEmpty(pkg)) {
            PackageManager mPm = context.getPackageManager();
            ApplicationInfo info = null;
            try {
                info = mPm.getApplicationInfo(pkg, 0);
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
            return info;
        }
        return null;
    }



    public static String getPackageVersionName(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return pi.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "unknown";
    }

    public static String getMetaDataStringValue(Context context,String name) {
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(),PackageManager.GET_META_DATA);
            if(info == null){
                return null;
            }

            return info.metaData.getString(name);

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static boolean isPackageInstalled(Context context, String pkgName) {
        return getPackageInfo(context, pkgName) != null;
    }

    public static PackageInfo getPackageInfo(Context context, String pkgName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (NameNotFoundException e) {
            packageInfo = null;
        }
        return packageInfo;
    }

    private static String getMD5String(byte[] paramArrayOfByte) {
        char[] asciiTable = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102 }; // ascii表对应的数字和字符的编码
        try {
            MessageDigest md5MessageDigest = MessageDigest.getInstance("MD5");
            md5MessageDigest.update(paramArrayOfByte);//
            byte[] tempByte = md5MessageDigest.digest();
            int i = tempByte.length;
            char[] tempChar = new char[i * 2];
            int j = 0;
            int k = 0;
            while (true) { // 将二进制数组转换成字符串
                if (j >= i) {
                    return new String(tempChar);
                }
                int m  = tempByte[j];
                int n  = k + 1;
                tempChar[k] = asciiTable[(0xF & m >>> 4)];
                k = n + 1;
                tempChar[n] = asciiTable[(m & 0xF)];
                j++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isPackageReal(Context context) {

        byte[] bytes = getPackageSignature(context, context.getPackageName());
        String currentMD5 = getMD5String(bytes).toUpperCase();

        String realMD5 = PACKAGE_MD5.replace(":","");
//        Log.v("msg", currentMD5);
//        Log.v("msg", realMD5);
        if(currentMD5.equals(realMD5))return true;
        else return false;
    }
}
