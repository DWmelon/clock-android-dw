package com.timediffproject.util;

import android.content.res.Resources;

/**
 * Created by melon on 2017/4/23.
 */

public class Utils {

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }


    public static float spToPt(int sp) {
        return sp * Resources.getSystem().getDisplayMetrics().density;
    }

    public static String md5(String str) {
        if (str == null) {
            return null;
        }
        return md5(str.getBytes());
    }

    public static String md5(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        try {
            java.security.MessageDigest md5 = java.security.MessageDigest
                    .getInstance("MD5");
            byte[] digest = md5.digest(bytes);
            sb.append(bytesToHexString(digest));
        } catch (Exception e) {
        }
        return sb.toString();
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            int val = b & 0xff;
            if (val < 0x10) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(val));
        }
        return sb.toString();
    }

}
