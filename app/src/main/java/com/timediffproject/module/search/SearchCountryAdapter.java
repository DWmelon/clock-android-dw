package com.timediffproject.module.search;

import android.content.Context;
import android.content.pm.ProviderInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.timediffproject.R;
import com.timediffproject.application.MyClient;
import com.timediffproject.model.CountryModel;
import com.timediffproject.module.select.SelectManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by melon on 2017/10/2.
 */

public class SearchCountryAdapter extends BaseAdapter {

    private Context mContext;

    private List<CountryModel> countryModelList = new ArrayList<>();

    private SelectManager selectManager;

    private OnSearchClickListener listener;

    public void setListener(OnSearchClickListener listener){
        this.listener = listener;
    }

    public SearchCountryAdapter(Context context){
        this.mContext = context;
        selectManager = MyClient.getMyClient().getSelectManager();
    }

    public void setData(List<CountryModel> countryModels){
        this.countryModelList = countryModels;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return countryModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return countryModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_public_country_item,viewGroup,false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        performView(holder,i);

        return view;
    }

    private void performView(final ViewHolder holder, int position){
        final CountryModel model = (CountryModel) getItem(position);
        if (model == null){
            return;
        }

        if (selectManager.isUserSelected(model)){
            holder.mVSelectIcon.setSelected(true);
        }else{
            holder.mVSelectIcon.setSelected(false);
        }

        holder.mTvCountryName.setText(model.getNationName());
        holder.mTvCityName.setText(model.getCityName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null){
                    listener.onSelectItem(holder.mVSelectIcon,model);
                }
            }
        });
    }

    public class ViewHolder{

        View itemView;

        TextView mTvCityName;
        TextView mTvCountryName;

        View mVSelectIcon;

        public ViewHolder(View view){
            itemView = view;
            mTvCountryName = (TextView)view.findViewById(R.id.tv_public_list_country_name);
            mTvCityName = (TextView)view.findViewById(R.id.tv_public_list_city_name);
            mVSelectIcon = view.findViewById(R.id.iv_public_list_country_select);
        }
    }

}
