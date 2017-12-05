package com.timediffproject.module.select;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.timediffproject.R;
import com.timediffproject.model.CountryModel;

/**
 * Created by melon on 2017/1/8.
 */

public class SelectOwnAdapter  extends RecyclerView.Adapter<SelectOwnAdapter.ViewHolder> {

    private Context mContext;

    private SelectManager manager;

    public SelectOwnAdapter(Context context,SelectManager manager){
        this.mContext = context;
        this.manager = manager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_public_country_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        CountryModel model = manager.getUserCountry().get(position);
        holder.cityName.setText(model.getCityName());
        holder.nationName.setText(model.getNationName());
    }

    @Override
    public int getItemCount() {
        return manager.getUserCountry().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nationName;
        TextView cityName;
        ImageView flagIcon;
        TextView firstChar;
        View selectIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            nationName = (TextView)itemView.findViewById(R.id.tv_public_list_country_name);
            cityName = (TextView)itemView.findViewById(R.id.tv_public_list_city_name);
            flagIcon = (ImageView)itemView.findViewById(R.id.iv_public_list_country_flag);
            firstChar = (TextView)itemView.findViewById(R.id.iv_public_list_country_char);
            selectIcon = itemView.findViewById(R.id.iv_public_list_country_select);
        }

    }

}
