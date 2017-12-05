package com.timediffproject.module.home.homepage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by melon on 2017/2/7.
 */

public class RecycleViewItem extends LinearLayout {

    private int width;

    public RecycleViewItem(Context context) {
        super(context);
    }

    public RecycleViewItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    float x=0;
    float y=0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN){
            x = event.getX();
            y = event.getY();

        }
        if (event.getAction() == MotionEvent.ACTION_UP){

        }
        if (event.getAction() == MotionEvent.ACTION_SCROLL){
            setX(event.getX());
        }
        return super.onTouchEvent(event);
    }
}
