package com.timediffproject.util;

import android.text.Spannable;
import android.text.SpannableString;

/**
 * Created by melon on 2017/2/23.
 */

public class SpanUtil {

    /**
     * 获取一个带样式的字符串
     *
     * @param str
     * @param what  要设置的样式
     * @param start
     * @param end
     * @return
     */
    public static SpannableString getSpannableString(CharSequence str, Object what, int start, int end) {
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(what, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

}
