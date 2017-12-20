package com.timediffproject.module.advice;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.timediffproject.R;
import com.timediffproject.application.BaseActivity;
import com.timediffproject.util.DeviceInfoManager;
import com.timediffproject.util.EmailUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by melon on 2017/2/26.
 */

public class AdviceActivity extends BaseActivity implements View.OnClickListener{

    private Toolbar mToolbar;

    private EditText mEtContent;
    private EditText mEtContact;
    private TextView mTvSubmit;

    private EmailUtil m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        umengPage = "建议页";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice);
        initView();
        initData();
        initListener();
    }

    private void initView(){
        mEtContent = (EditText)findViewById(R.id.et_advice_1);
        mEtContact= (EditText)findViewById(R.id.et_advice_2);
        mTvSubmit = (TextView)findViewById(R.id.tv_advice_btn);
    }

    private void initData(){
        mToolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.icon_back_white);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.setTitle(R.string.advice_title);
    }

    private void initListener(){
        mTvSubmit.setOnClickListener(this);
    }




    @Override
    public void onClick(View view) {
        checkAndSend();
    }

    private void checkAndSend(){
        if(mEtContent.getText().toString().trim().isEmpty()){
            Toast.makeText(this,getString(R.string.advice_toast),Toast.LENGTH_LONG).show();
        }else{
            sendEmail();
        }
    }

    private void sendEmail(){
//        MobclickAgent.onEvent(this, StatConstant.BtnSendMail);

        m = new EmailUtil("3303847677@qq.com", "uudpnhbbmiqgcjhj");

        String[] toArr = {"3303847677@qq.com"};
        m.setTo(toArr);
        m.setFrom("3303847677@qq.com");
        String contact = mEtContact.getText().toString().trim();
        if (TextUtils.isEmpty(contact)){
            DeviceInfoManager.getInstance().init(this);
            contact = DeviceInfoManager.mDeviceUid+"_"+DeviceInfoManager.MODEL;
        }

        m.setSubject(contact+"的反馈");
        m.setBody(mEtContent.getText().toString().trim());
        try {
            if(m.send()) {
                Toast.makeText(AdviceActivity.this, "发送成功啦~", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(AdviceActivity.this, "发送失败>.<", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
