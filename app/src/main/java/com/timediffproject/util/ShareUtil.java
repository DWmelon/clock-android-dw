package com.timediffproject.util;

import android.app.Activity;
import android.content.Context;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by melon on 2017/3/3.
 */

public class ShareUtil {

    public static void getShareInstant(Activity activity, UMShareListener listener){
        new ShareAction(activity).withText("hello")
                .setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.QQ,SHARE_MEDIA.WEIXIN)
                .setCallback(listener).open();
    }

}
