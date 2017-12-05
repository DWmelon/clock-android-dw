package com.timediffproject.application;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.timediffproject.R;
import com.timediffproject.util.ProgressBarUtil;
import com.timediffproject.widgets.MaterialDialog;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import anet.channel.util.StringUtils;

/**
 * Created by melon on 2017/1/3.
 */

public class BaseActivity extends AppCompatActivity {

    private MaterialDialog mMaterialDialog;

    protected String umengPage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PushAgent.getInstance(this).onAppStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }

    }


    public void showCommonAlert(int msgResId,final View.OnClickListener l,final View.OnClickListener r){
        showCommonAlert(R.string.dialog_title,msgResId,R.string.dialog_ok,R.string.dialog_cancel,l,r);
    }

    public void showCommonAlert(int titleResId, int msgResId, int posTxtResId, int nagTxtResId, final View.OnClickListener l, final View.OnClickListener listener) {

        if (mMaterialDialog != null) {
            if (mMaterialDialog.isShowing()) {
                mMaterialDialog.dismiss();
            }
            mMaterialDialog = null;
        }
        mMaterialDialog = new MaterialDialog(this);
        mMaterialDialog.setTitle(titleResId);
        mMaterialDialog.setMessage(msgResId);
        mMaterialDialog.setCancelable(true);
        mMaterialDialog.setCanceledOnTouchOutside(true);
        mMaterialDialog.setPositiveButton(posTxtResId, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
                if (l != null) {
                    l.onClick(v);
                }
            }
        });
        mMaterialDialog.setNegativeButton(nagTxtResId, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
                mMaterialDialog = null;
                if (listener != null) {
                    listener.onClick(v);
                }
            }
        });
        mMaterialDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                listener.onClick(new View(BaseActivity.this));
            }
        });
        mMaterialDialog.show();
    }

    public void showCommonAlert(int titleResId, int msgResId){
        showCommonAlert(getString(titleResId),getString(msgResId));
    }

    public void showCommonAlert(String titleResId, String msgResId) {

        if (mMaterialDialog != null) {
            if (mMaterialDialog.isShowing()) {
                mMaterialDialog.dismiss();
            }
            mMaterialDialog = null;
        }
        mMaterialDialog = new MaterialDialog(this);
        mMaterialDialog.setTitle(titleResId);
        mMaterialDialog.setMessage(msgResId);
        mMaterialDialog.setCancelable(true);
        mMaterialDialog.setCanceledOnTouchOutside(true);
        mMaterialDialog.setPositiveButton(R.string.dialog_ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
            }
        });

        mMaterialDialog.show();
    }

    public void hideDialog(){
        if (mMaterialDialog != null && mMaterialDialog.isShowing()){
            mMaterialDialog.dismiss();
        }
    }

    public void showProgress(){
        showProgress(R.string.progress_tip,false);
    }

    public void showProgress(int msgResId, boolean cancelable) {

        if (mMaterialDialog != null) {
            if (mMaterialDialog.isShowing()) {
                mMaterialDialog.dismiss();
            }
            mMaterialDialog = null;
        }

        mMaterialDialog = new MaterialDialog(this);
        if (mMaterialDialog != null) {

            View view = LayoutInflater.from(this).inflate(R.layout.layout_progressbar, null);
            TextView msgTv = (TextView) view.findViewById(R.id.content);
            msgTv.setText(msgResId);
            ProgressBar progressBar = (ProgressBar) view.findViewById(android.R.id.progress);
            final int materialBlue = getResources().getColor(R.color.progress_color);
            ProgressBarUtil.setupProgressDialog(progressBar, materialBlue);
            mMaterialDialog.setView(view);
        }

//        mMaterialDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                mMaterialDialog = null;
//            }
//        });
        mMaterialDialog.setCancelable(cancelable);
        mMaterialDialog.setMessage(getText(msgResId));
        mMaterialDialog.show();
    }

    public void hideProgress() {
        if (mMaterialDialog != null) {
            if (mMaterialDialog.isShowing()) {
                mMaterialDialog.dismiss();
            }
            mMaterialDialog = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!StringUtils.isBlank(umengPage)){
            MobclickAgent.onPageStart(umengPage);
        }
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!StringUtils.isBlank(umengPage)){
            MobclickAgent.onPageEnd(umengPage);
        }
        MobclickAgent.onPause(this);
    }


}