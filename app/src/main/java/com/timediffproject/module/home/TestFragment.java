package com.timediffproject.module.home;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timediffproject.R;
import com.timediffproject.application.BaseFragment;
import com.timediffproject.application.MyClient;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by melon on 2017/2/16.
 */

public class TestFragment extends BaseFragment {



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        UMENG_STR = "测试页";
        return inflater.inflate(R.layout.fragment_test,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.i("test","pause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("test","resume");
    }
}
