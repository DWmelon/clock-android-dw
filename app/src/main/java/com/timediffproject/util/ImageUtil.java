package com.timediffproject.util;

import android.content.Context;

/**
 * Created by melon on 2017/2/24.
 */

public class ImageUtil {

    public static int  getResource(Context context,String imageName) {

        int resId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        return resId;
    }
}
