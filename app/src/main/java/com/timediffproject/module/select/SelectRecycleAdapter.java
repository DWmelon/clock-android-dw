package com.timediffproject.module.select;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.timediffproject.R;
import com.timediffproject.application.GlobalPreferenceManager;
import com.timediffproject.model.CountryModel;
import com.timediffproject.util.CommonUtil;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by melon on 2017/1/6.
 */

public class SelectRecycleAdapter extends RecyclerView.Adapter<SelectRecycleAdapter.ViewHolder> {

    private Context mContext;

    private final int TYPE_BAR = 1;

    private final int TYPE_ITEM = 2;


    private SelectManager manager;

    private OnCountrySelectListener listener;

    private List<CountryModel> modelList = new ArrayList<>();
    private HashMap<Integer,Integer> indexMap = new HashMap<>();
    private List<Integer> barIndex = new ArrayList<>();

    private String language;

    public SelectRecycleAdapter(Context context,SelectManager manager,OnCountrySelectListener listener){
        this.mContext = context;
        this.manager = manager;
        this.listener = listener;
        language = GlobalPreferenceManager.getString(mContext,GlobalPreferenceManager.KEY_LANGUAGE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_BAR){
            view = LayoutInflater.from(mContext).inflate(R.layout.adapter_country_item_bar,parent,false);
        }else{
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_public_country_item,parent,false);
        }
        ViewHolder viewHolder = new ViewHolder(view,viewType);
        view.setTag(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final CountryModel model = modelList.get(position);

        if (barIndex.contains(position)){
            holder.firstChar.setText(model.getFirstChar());
        }else{
            holder.itemView.setTag(position);

            holder.cityName.setText(CommonUtil.getCityNameByLanguage(language,model));
            holder.nationName.setText(model.getNationName());

            if (manager.isUserSelected(model)){
                holder.selectIcon.setSelected(true);
            }else{
                holder.selectIcon.setSelected(false);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                if (listener!=null){
                    listener.onCountrySelect(view,model,position);
                }
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (barIndex.contains(position)){
            return TYPE_BAR;
        }else{
            return TYPE_ITEM;
        }
    }

    public void setState(int state){
        List<CountryModel> list = manager.getCountryModelList(state);
        if (list == null){
            if (mContext!=null){
                Toast.makeText(mContext,"网络不通畅，获取数据失败。",Toast.LENGTH_LONG).show();
            }

            list = new ArrayList<>();
        }
        modelList.clear();
        indexMap.clear();
        barIndex.clear();

        int index = 0;
        for (int i = 0;i<list.size();i++){
            CountryModel model = list.get(i);
            if (model.isFirstData()){
                modelList.add(model);
                barIndex.add(index);
                index++;
            }

            modelList.add(model);
            indexMap.put(index,i);
            index++;

        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        int type = 0;

        TextView nationName;
        TextView cityName;
        ImageView flagIcon;
        TextView firstChar;
        View selectIcon;

        public ViewHolder(View itemView,int type) {
            super(itemView);
            this.type = type;
            if (type == TYPE_BAR){
                firstChar = (TextView)itemView.findViewById(R.id.iv_public_list_country_char);
            }else{
                nationName = (TextView)itemView.findViewById(R.id.tv_public_list_country_name);
                cityName = (TextView)itemView.findViewById(R.id.tv_public_list_city_name);
                flagIcon = (ImageView)itemView.findViewById(R.id.iv_public_list_country_flag);
                selectIcon = itemView.findViewById(R.id.iv_public_list_country_select);
            }

        }

    }

}
