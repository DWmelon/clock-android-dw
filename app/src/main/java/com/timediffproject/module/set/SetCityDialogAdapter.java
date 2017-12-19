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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by melon on 2017/2/28.
 */

public class SetCityDialogAdapter extends RecyclerView.Adapter<SetCityDialogAdapter.ViewHolder> {

    private Context mContext;

    private SelectManager manager;

    private OnSetCityChangeListener listener;

    private List<CountryModel> dataList = new ArrayList<>();

    public SetCityDialogAdapter(Context context,List<CountryModel> modelList){
        this.mContext = context;
        manager = MyClient.getMyClient().getSelectManager();
        dataList = modelList;
    }

    public void setData(List<CountryModel> modelList){
        dataList = modelList;
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        CountryModel model = dataList.get(position);
        holder.mTvCity.setText(model.getCityName());
        holder.mTvNation.setText(mContext.getString(R.string.city_dialog_nation,model.getNationName()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null){
                    listener.onChangeCity(dataList.get(holder.getAdapterPosition()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView mTvCity;
        TextView mTvNation;

        ViewHolder(View itemView) {
            super(itemView);
            mTvCity = (TextView)itemView.findViewById(R.id.tv_dialog_city);
            mTvNation = (TextView)itemView.findViewById(R.id.tv_dialog_nation);
        }
    }

}
