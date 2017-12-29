package com.timediffproject.module.setting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.suke.widget.SwitchButton;
import com.timediffproject.R;
import com.timediffproject.application.BaseActivity;
import com.timediffproject.application.GlobalPreferenceManager;
import com.timediffproject.eventbus.HomeEB;
import com.timediffproject.module.home.MyMainActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by melon on 2017/12/28.
 */

public class SettingActivity extends BaseActivity {

    @BindView(R.id.my_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.sb_use_24)
    SwitchButton mSbUse24;
    boolean isUse24;

    @BindView(R.id.sb_use_ratio)
    SwitchButton mSbUseRatio;
    boolean isUseRatio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initToolbar();
        initData();
    }

    private void initToolbar(){
        mToolbar.setTitle("设置");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.icon_back_white);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData(){
        isUse24 = GlobalPreferenceManager.isUse24Hours(this);
        mSbUse24.setChecked(isUse24);

        isUseRatio = GlobalPreferenceManager.isUseRatio(this);
        mSbUseRatio.setChecked(isUseRatio);
    }

    @Override
    protected void onDestroy() {
        if (mSbUse24.isChecked() != isUse24 || mSbUseRatio.isChecked() != isUseRatio){
            GlobalPreferenceManager.setUse24Hours(this,mSbUse24.isChecked());
            GlobalPreferenceManager.setUseRatio(this,mSbUseRatio.isChecked());
            EventBus.getDefault().post(new HomeEB());
        }
        super.onDestroy();
    }
}
