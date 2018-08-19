package com.timediffproject.module.setting;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Toast;

import com.timediffproject.R;
import com.timediffproject.application.BaseActivity;
import com.timediffproject.application.GlobalPreferenceManager;
import com.timediffproject.constants.Constant;
import com.timediffproject.util.CommonUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WidgetStyleSelectActivity extends BaseActivity {

    @BindView(R.id.my_toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_style_select);
        ButterKnife.bind(this);
        initToolbar();
    }

    private void initToolbar(){
        mToolbar.setTitle(R.string.setting_widget_style);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.icon_back_white);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick(R.id.ll_widget_style_black)
    void handleSelectBlack(){
        GlobalPreferenceManager.setInt(this,GlobalPreferenceManager.KEY_WIDGET_STYLE_INT, Constant.WIDGET_BLACK);
        String str = getString(R.string.setting_widget_style_toast,getString(R.string.setting_widget_style_black));
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.ll_widget_style_tran)
    void handleSelectTran(){
        GlobalPreferenceManager.setInt(this,GlobalPreferenceManager.KEY_WIDGET_STYLE_INT, Constant.WIDGET_TRAN);
        String str = getString(R.string.setting_widget_style_toast,getString(R.string.setting_widget_style_empty));
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.ll_widget_style_blue)
    void handleSelectBlue(){
        GlobalPreferenceManager.setInt(this,GlobalPreferenceManager.KEY_WIDGET_STYLE_INT, Constant.WIDGET_BLUE);
        String str = getString(R.string.setting_widget_style_toast,getString(R.string.setting_widget_style_blue));
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.ll_widget_style_yellow)
    void handleSelectYellow(){
        GlobalPreferenceManager.setInt(this,GlobalPreferenceManager.KEY_WIDGET_STYLE_INT, Constant.WIDGET_YELLOW);
        String str = getString(R.string.setting_widget_style_toast,getString(R.string.setting_widget_style_yellow));
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.ll_widget_style_green)
    void handleSelectGreen(){
        GlobalPreferenceManager.setInt(this,GlobalPreferenceManager.KEY_WIDGET_STYLE_INT, Constant.WIDGET_GREEN);
        String str = getString(R.string.setting_widget_style_toast,getString(R.string.setting_widget_style_green));
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.ll_widget_style_pink)
    void handleSelectPink(){
        GlobalPreferenceManager.setInt(this,GlobalPreferenceManager.KEY_WIDGET_STYLE_INT, Constant.WIDGET_PINK);
        String str = getString(R.string.setting_widget_style_toast,getString(R.string.setting_widget_style_pink));
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }

}
