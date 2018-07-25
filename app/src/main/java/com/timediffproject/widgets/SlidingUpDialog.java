package com.timediffproject.widgets;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import com.timediffproject.R;
import com.timediffproject.widgets.BaseDialog;


/**
 * 向上滑出的对话框
 * Created by tziyao on 16/3/14.
 */
public class SlidingUpDialog extends BaseDialog {
    public SlidingUpDialog(Context context) {
        this(context, 0);
    }

    public SlidingUpDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    private void init() {
        //设置动画
        getWindow().setWindowAnimations(R.style.CustomDialog);
    }

    @Override
    public void setContentView(View contentView) {
        setGravity(Gravity.BOTTOM);
        super.setContentView(contentView);
    }
}
