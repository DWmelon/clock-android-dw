package com.timediffproject.module.set;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.timediffproject.R;
import com.timediffproject.application.MyClient;
import com.timediffproject.model.CountryModel;
import com.timediffproject.module.select.SelectManager;

/**
 * Created by melon on 2017/2/28.
 */

public class SetCityDialogAdapter extends RecyclerView.Adapter<SetCityDialogAdapter.ViewHolder> {

    private Context mContext;

    private SelectManager manager;

    private OnSetCityChangeListener listener;

    public SetCityDialogAdapter(Context context){
        this.mContext = context;
        manager = MyClient.getMyClient().getSelectManager();
    }

    public void setListener(OnSetCityChangeListener listener){
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_set_city,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        CountryModel model = manager.getUserCountry().get(position);
        holder.mTvCity.setText(model.getCityName());
//        if (position == 0){
//            holder.mIvLocal.setVisibility(View.VISIBLE);
//        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null){
                    listener.onChangeCity(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return manager.getUserCountry().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mTvCity;
        private ImageView mIvLocal;
        private ImageView mIvRight;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvCity = (TextView)itemView.findViewById(R.id.tv_dialog_city);
            mIvLocal = (ImageView)itemView.findViewById(R.id.iv_local_icon);
            mIvRight = (ImageView)itemView.findViewById(R.id.iv_right);
        }
    }

}
