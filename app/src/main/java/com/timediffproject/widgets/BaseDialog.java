package com.timediffproject.widgets;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;

import com.timediffproject.R;


/**
 * 所有自定义对话框的基类
 * Created by tziyao on 16/5/16.
 */
public class BaseDialog extends Dialog {
    public BaseDialog(Context context) {
        this(context, 0);
    }

    public BaseDialog(Context context, int theme) {
        super(context, 0);
        init();
    }

    private void init() {
        getWindow().getDecorView().setBackgroundResource(R.color.transparent);
        //去除黑色边框
        getWindow().getDecorView().setBackgroundResource(R.color.transparent);
        //去除padding
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
    }

    protected BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    @Override
    public void setContentView(View contentView) {
        //设置宽高一定要在setContentView前设置,不能能在构造函数中设置
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.horizontalMargin = 0;
        lp.alpha = 1;
        lp.width = getContext().getResources().getDisplayMetrics().widthPixels;
        getWindow().setAttributes(lp);
        super.setContentView(contentView);
    }

    protected void setGravity(int gravity) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.gravity = gravity;
        getWindow().setAttributes(lp);
    }
}
