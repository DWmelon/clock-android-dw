package com.timediffproject.module.about;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.timediffproject.R;
import com.timediffproject.application.BaseActivity;
import com.timediffproject.application.GlobalPreferenceManager;
import com.timediffproject.application.UpdateActivity;

/**
 * Created by melon on 2017/10/27.
 */

public class AboutMeActivity extends UpdateActivity {

    private TextView mTvNow;
    private TextView mTvNew;

    private TextView mTvInfo;

    private Toolbar mToolbar;

    private TextView mTvUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);


        mTvNow = (TextView)findViewById(R.id.tv_about_version_now);
        mTvNew = (TextView)findViewById(R.id.tv_about_version_new);
        mTvInfo = (TextView)findViewById(R.id.tv_about_version_info);

        mTvUpdate = (TextView)findViewById(R.id.tv_update_btn);

        initToolbar();
        initData();
    }

    private void initData(){

        int newlyCode = GlobalPreferenceManager.getVersionCode(this);
        int nowCode = 0;

        String pkName = this.getPackageName();
        try {
            String versionName = getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
            nowCode = getPackageManager().getPackageInfo(
                    pkName, 0).versionCode;
            mTvNow.setText(getString(R.string.about_version_name_now, versionName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        mTvNew.setText(getString(R.string.about_version_name_new, GlobalPreferenceManager.getVersionName(this)));

        mTvInfo.setText(GlobalPreferenceManager.getVersionInfo(this));

        if (newlyCode > nowCode && nowCode != 0){
            mTvUpdate.setText(R.string.update_now);
            mTvUpdate.setSelected(true);
            mTvUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showProgress();
                    setShow(true);
                    checkUpdate();
                }
            });
        }else{
            mTvUpdate.setText(R.string.update_already);
            mTvUpdate.setSelected(false);
        }


    }

    private void initToolbar(){
        mToolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.icon_back_white);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.setTitle(R.string.about_me);
    }

}
