package com.timediffproject.module.set;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timediffproject.R;
import com.timediffproject.application.MyClient;
import com.timediffproject.model.CountryModel;
import com.timediffproject.module.select.SelectManager;

/**
 * Created by melon on 2017/2/10.
 */

public class RecycleViewSetCityAdapter extends RecyclerView.Adapter<RecycleViewSetCityAdapter.ViewHolder> {

    private Context mContext;

    private SelectManager selectManager;

    public RecycleViewSetCityAdapter(Context context){
        this.mContext = context;
        this.selectManager = MyClient.getMyClient().getSelectManager();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_set_city_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CountryModel model = selectManager.getUserCountry().get(position);
        holder.mTvCity.setText(model.getCityName());
    }

    @Override
    public int getItemCount() {
        return selectManager.getUserCountry().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mTvCity;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvCity = (TextView)itemView.findViewById(R.id.tv_set_city);
        }
    }

}
