package com.timediffproject.module.home.homepage;

/**
 * Created by melon on 2017/2/6.
 */

public interface OnMoveAndSwipedListener {

    boolean onItemMove(int fromPosition , int toPosition);
    void onItemDismiss(int position);
    void onItemChange(int position);

}
