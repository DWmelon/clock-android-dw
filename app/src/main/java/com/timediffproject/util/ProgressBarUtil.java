package com.timediffproject.util;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.widget.ProgressBar;

/**
 * Created by eddy on 2015/5/6.
 */
public class ProgressBarUtil {


    public static void setupProgressDialog(ProgressBar progressBar, int color) {

        if (progressBar == null) return;
        ColorStateList sl = ColorStateList.valueOf(color);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progressBar.setProgressTintList(sl);
            progressBar.setSecondaryProgressTintList(sl);
            progressBar.setIndeterminateTintList(sl);
        } else {
            PorterDuff.Mode mode = PorterDuff.Mode.SRC_IN;
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
                mode = PorterDuff.Mode.MULTIPLY;
            }
            if (progressBar.getIndeterminateDrawable() != null)
                progressBar.getIndeterminateDrawable().setColorFilter(color, mode);
            if (progressBar.getProgressDrawable() != null)
                progressBar.getProgressDrawable().setColorFilter(color, mode);
        }

    }
}
