package com.timediffproject.module.set;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.timediffproject.R;

/**
 * Created by melon on 2017/2/13.
 */

public class SetRecycleView extends RecyclerView {

    /**
     * The RecyclerView is not currently scrolling.
     * 当前的recycleView不滑动(滑动已经停止时)
     */
    public static final int SCROLL_STATE_IDLE = 0;

    /**
     * The RecyclerView is currently being dragged by outside input such as user touch input.
     * 当前的recycleView被拖动滑动
     */
    public static final int SCROLL_STATE_DRAGGING = 1;

    /**
     * The RecyclerView is currently animating to a final position while not under
     * outside control.
     * 当前的recycleView在滚动到某个位置的动画过程,但没有被触摸滚动.调用 scrollToPosition(int) 应该会触发这个状态
     */
    public static final int SCROLL_STATE_SETTLING = 2;

    private OnSetCityChangeListener listener;

    private int dx;

    public SetRecycleView(Context context) {
        super(context);
    }

    public SetRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setListener(OnSetCityChangeListener listener){
        this.listener = listener;
    }

    @Override
    public void onScrollStateChanged(int state) {
        if (getLayoutManager()!=null && getAdapter() != null && state == SCROLL_STATE_IDLE){

            int width = getContext().getResources().getDimensionPixelOffset(R.dimen.set_item_width);

            int firstPosition = ((LinearLayoutManager)getLayoutManager()).findFirstVisibleItemPosition();
            int firstPositionX = (int) (0.5 * width);

            View childView=this.getChildAt(0);

            //获取第一个子view的左部坐标
            int left=Math.abs(childView.getLeft());

            if (left>firstPositionX){
                firstPosition += 1;
            }
            this.scrollToPosition(firstPosition);
            listener.onChangeCity(firstPosition);
        }
        super.onScrollStateChanged(state);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        this.dx = dx;
//        Log.e("dx",String.valueOf(dx));
        super.onScrolled(dx, dy);
    }

    private void moveAnimation(int x){

    }

}
