package com.timediffproject.module.home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.timediffproject.R;
import com.timediffproject.application.BaseActivity;
import com.timediffproject.module.about.AboutMeActivity;
import com.timediffproject.module.advice.AdviceActivity;
import com.timediffproject.module.tempdata.TempDataActivity;

/**
 * Created by melon on 2017/2/26.
 */

public class LeftDrawerAdapter extends RecyclerView.Adapter<LeftDrawerAdapter.ViewHolder> {

    private final static int TYPE_HEADER = 0;
    private final static int TYPE_ITEM = 1;

    private Context mContext;

    public LeftDrawerAdapter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_HEADER){
            view = LayoutInflater.from(mContext).inflate(R.layout.header_left_drawer,parent,false);
        }else{
            view = LayoutInflater.from(mContext).inflate(R.layout.adapter_left_drawer,parent,false);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == 0){
            holder.mTvContext.setText(mContext.getString(R.string.app_name));
            holder.mIvIcon.setImageResource(R.drawable.icon_app);
        } else if (position == 1){
            holder.mTvContext.setText(R.string.left_drawer_tip_advice);
            holder.mIvIcon.setImageResource(R.drawable.icon_feedback);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, AdviceActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }else if (position == 2){
            holder.mIvIcon.setImageResource(R.drawable.icon_widget_know);
            holder.mTvContext.setText(R.string.left_drawer_tip_widget);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((BaseActivity)mContext).showCommonAlert(R.string.left_drawer_tip_widget_1,R.string.left_drawer_tip_widget_2);
                }
            });
        } else if (position == 3){
            holder.mIvIcon.setImageResource(R.drawable.icon_about_me);
            holder.mTvContext.setText(R.string.about_me);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, AboutMeActivity.class);
                    mContext.startActivity(intent);
                }
            });
        } else if (position == 4){
            holder.mIvIcon.setImageResource(R.drawable.icon_share);
            holder.mTvContext.setText(R.string.left_drawer_tip_share);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, TempDataActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return TYPE_HEADER;
        }else{
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTvContext;
        public ImageView mIvIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvContext = (TextView) itemView.findViewById(R.id.tv_left_drawer_tip);
            mIvIcon = (ImageView) itemView.findViewById(R.id.iv_left_drawer_icon);

        }
    }

}
