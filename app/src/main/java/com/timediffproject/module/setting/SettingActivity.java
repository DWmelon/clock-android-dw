package com.timediffproject.module.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.suke.widget.SwitchButton;
import com.timediffproject.R;
import com.timediffproject.application.BaseActivity;
import com.timediffproject.application.GlobalPreferenceManager;
import com.timediffproject.constants.Constant;
import com.timediffproject.eventbus.HomeEB;
import com.timediffproject.network.UrlConstantV2;
import com.timediffproject.stat.StatCMConstant;
import com.timediffproject.stat.StatManager;
import com.timediffproject.util.CommonUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @BindView(R.id.cb_language_china)
    CheckBox mCbChina;
    boolean isUseChina = true;

    @BindView(R.id.cb_language_english)
    CheckBox mCbEnglish;
    boolean isUseEnglish;

    @BindView(R.id.tv_setting_widget_style)
    TextView mTvWidgetStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initToolbar();
        initData();
    }

    private void initToolbar(){
        mToolbar.setTitle(R.string.setting_title);
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

        mCbChina.setChecked(CommonUtil.judgeLanguage(Constant.LANGUAGE_CHINA));
        mCbEnglish.setChecked(CommonUtil.judgeLanguage(Constant.LANGUAGE_ENGLISH));

        mTvWidgetStyle.setText(CommonUtil.getWidgetBackStyleStr());
    }

    @Override
    protected void onDestroy() {
        if (mSbUse24.isChecked() != isUse24 || mSbUseRatio.isChecked() != isUseRatio){
            GlobalPreferenceManager.setUse24Hours(this,mSbUse24.isChecked());
            GlobalPreferenceManager.setUseRatio(this,mSbUseRatio.isChecked());
            EventBus.getDefault().post(new HomeEB());

            if (mSbUse24.isChecked() != isUse24){
                List<String> list = new ArrayList<>();
                list.add(String.valueOf(mSbUse24.isChecked()));
                StatManager.statEventNum(this, StatCMConstant.SET_USE_24,list);
            }

            if (mSbUseRatio.isChecked() != isUseRatio){
                List<String> list = new ArrayList<>();
                list.add(String.valueOf(mSbUseRatio.isChecked()));
                StatManager.statEventNum(this, StatCMConstant.SET_USE_RATIO,list);
            }

        }
        super.onDestroy();
    }

    @OnClick(R.id.cb_language_china)
    void selectLanguageChina(){
        mCbChina.setChecked(true);
        mCbEnglish.setChecked(false);
        Toast.makeText(this,R.string.setting_restart,Toast.LENGTH_SHORT).show();
        GlobalPreferenceManager.setString(this,GlobalPreferenceManager.KEY_LANGUAGE,Constant.LANGUAGE_CHINA);
        CommonUtil.changeAppLanguage(this, Locale.SIMPLIFIED_CHINESE);
    }

    @OnClick(R.id.cb_language_english)
    void selectLanguageEnglish(){
        mCbEnglish.setChecked(true);
        mCbChina.setChecked(false);
        Toast.makeText(this,R.string.setting_restart,Toast.LENGTH_SHORT).show();
        GlobalPreferenceManager.setString(this,GlobalPreferenceManager.KEY_LANGUAGE,Constant.LANGUAGE_ENGLISH);
        CommonUtil.changeAppLanguage(this, Locale.US);
    }

    @OnClick(R.id.ll_setting_widget_style)
    void handleIntentWidgetStyle(){
        Intent intent = new Intent(this,WidgetStyleSelectActivity.class);
        startActivityForResult(intent, UrlConstantV2.REQUEST.WIDGET_STYLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UrlConstantV2.REQUEST.WIDGET_STYLE){
            mTvWidgetStyle.setText(CommonUtil.getWidgetBackStyleStr());
        }
    }
}
