package com.timediffproject.application;

import android.support.v4.app.Fragment;

import com.umeng.analytics.MobclickAgent;

import anet.channel.util.StringUtils;

/**
 * Created by melon on 2017/2/16.
 */

public class BaseFragment extends Fragment {

    protected String UMENG_STR = "";

    @Override
    public void onResume() {
        super.onResume();
        if (!StringUtils.isBlank(UMENG_STR)){
            MobclickAgent.onPageStart(UMENG_STR);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!StringUtils.isBlank(UMENG_STR)){
            MobclickAgent.onPageEnd(UMENG_STR);
        }
    }
}
