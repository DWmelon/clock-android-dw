package com.timediffproject.module.ring;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import com.timediffproject.application.BaseActivity;
import com.timediffproject.origin.MainApplication;

/**
 * Created by melon on 2017/2/18.
 */

public class RingRelativeLayout extends RelativeLayout {

    int width;
    int height;

    private RingCancelListener listener;

    public void setListener(RingCancelListener listener){
        this.listener = listener;
    }

    public RingRelativeLayout(Context context) {
        super(context);
        init();
    }

    public RingRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){

        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();

        width = metrics.widthPixels;
        height = metrics.heightPixels;
    }


    private float moveY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN){
            moveY = event.getRawY();
        }

        if (event.getAction() == MotionEvent.ACTION_UP){
            float diff = Math.abs(getTop())/(float)height;
            if (diff<0.6){
                showMoveAnimation();
            }else{
                showCancelAnimation();
            }
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE){
            int diffY = (int) (event.getRawY() - moveY);
            diffY += getTop();
            if (diffY>0){
                diffY = 0;
            }

            Log.i("diff",String.valueOf(diffY));
                layout(0,diffY,width,diffY+height);
            moveY = event.getRawY();
        }

        return true;
    }

    private void showMoveAnimation(){
        final int top = getTop();

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(Math.abs(top));
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float y = (float)valueAnimator.getAnimatedValue();
                layout(0,(int)(top+y),width,(int)(top+y)+height);
            }
        });

        valueAnimator.start();

    }

    private void showCancelAnimation(){
        final int top = getTop();

        ValueAnimator valueAnimator = ValueAnimator.ofInt(height-Math.abs(top));
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int y = (int)valueAnimator.getAnimatedValue();
                layout(0,(int)(top-y),width,(int)(top-y)+height);
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (listener!=null){
                    listener.cancelClock();
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        valueAnimator.start();

    }


}
