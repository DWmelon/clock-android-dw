package com.timediffproject.application;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by melon on 2017/2/16.
 */

public class BaseFragment extends Fragment {

    protected String UMENG_STR = "";

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(UMENG_STR)){
            MobclickAgent.onPageStart(UMENG_STR);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!TextUtils.isEmpty(UMENG_STR)){
            MobclickAgent.onPageEnd(UMENG_STR);
        }
    }
}
