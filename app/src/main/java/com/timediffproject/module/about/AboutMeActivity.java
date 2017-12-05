package com.timediffproject.module.about;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.timediffproject.R;
import com.timediffproject.application.BaseActivity;
import com.timediffproject.application.GlobalPreferenceManager;

/**
 * Created by melon on 2017/10/27.
 */

public class AboutMeActivity extends BaseActivity {

    private TextView mTvNow;
    private TextView mTvNew;

    private TextView mTvInfo;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);


        mTvNow = (TextView)findViewById(R.id.tv_about_version_now);
        mTvNew = (TextView)findViewById(R.id.tv_about_version_new);
        mTvInfo = (TextView)findViewById(R.id.tv_about_version_info);


        initToolbar();
        initData();
    }

    private void initData(){

        String pkName = this.getPackageName();
        try {
            String versionName = getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
            mTvNow.setText(getString(R.string.about_version_name_now, versionName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        mTvNew.setText(getString(R.string.about_version_name_new, GlobalPreferenceManager.getVersionName(this)));

        mTvInfo.setText(GlobalPreferenceManager.getVersionInfo(this));

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
