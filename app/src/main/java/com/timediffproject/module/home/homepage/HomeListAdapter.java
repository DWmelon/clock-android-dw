package com.timediffproject.module.home.homepage;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.timediffproject.R;
import com.timediffproject.application.BaseActivity;
import com.timediffproject.application.MyClient;
import com.timediffproject.constants.Constants;
import com.timediffproject.model.CountryModel;
import com.timediffproject.module.money.EMoneyMapModel;
import com.timediffproject.module.select.SelectActivity;
import com.timediffproject.module.select.SelectManager;
import com.timediffproject.module.set.SettingTimeActivity;
import com.timediffproject.network.UrlConstantV2;
import com.timediffproject.util.ImageUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by melon on 2017/1/8.
 */

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> implements OnMoveAndSwipedListener {

    private Context mContext;

    private SelectManager manager;

    private int width;

    String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    private HashMap<String,EMoneyMapModel> mEMoneyMap = new HashMap<>();
    private String mSCoin = "";

    public HomeListAdapter(Context context, SelectManager manager){
        this.mContext = context;
        this.manager = manager;
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;
    }

    public void updateEMoneyMap(){
        mSCoin = MyClient.getMyClient().getMoneyManager().getEMoneySourceCoinName();
        mEMoneyMap = MyClient.getMyClient().getMoneyManager().getEMoneyMap();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_home_country_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CountryModel model = manager.getUserCountry().get(position);


        holder.countryIcon.setImageResource(ImageUtil.getResource(mContext,model.getLogo()));

        holder.cityName.setText(model.getCityName());
        holder.nationName.setText(model.getNationName());
        Date date = MyClient.getMyClient().getTimeManager().getTime(model.getDiffTime());
        model.setNowDate(date);
        SimpleDateFormat dff = new SimpleDateFormat("HH: mm");
        String str = dff.format(date);
        holder.smallTime.setText(str);

        SimpleDateFormat myFmt2=new SimpleDateFormat("yyyy-MM-dd");
        String strTime1 = myFmt2.format(date);
        String month = strTime1.split("-")[1];
        String day = strTime1.split("-")[2];

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        holder.bigTime.setText(month+"月"+day+"日"+"-"+weekDays[calendar.get(Calendar.DAY_OF_WEEK)-1]);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SettingTimeActivity.class);
                intent.putExtra(Constants.INTENT_KEY_CITY_ID,model.getId());
                mContext.startActivity(intent);
            }
        });

        if (position == 0){
            holder.mLlSubContent.setVisibility(View.GONE);
            holder.locIcon.setVisibility(View.VISIBLE);
        }else{
            if (mEMoneyMap.containsKey(model.getNationName()) && !TextUtils.isEmpty(mSCoin)){
                holder.mLlSubContent.setVisibility(View.VISIBLE);

                EMoneyMapModel mapModel = mEMoneyMap.get(model.getNationName());

                float sourceValue = 1f;
                float targetValue = mapModel.getValue();
                String sourceValueStr;
                String targetValueStr;
                if (targetValue < 1f){
                    sourceValue = 1f/targetValue;
                    sourceValue = Math.round(sourceValue*100)/100f;

                    sourceValueStr = String.valueOf(sourceValue);
                    targetValueStr = "1";
                }else{
                    sourceValueStr = "1";
                    targetValueStr = String.valueOf(Math.round(targetValue*100)/100f);
                }

                holder.mTvSCoinName.setText(mSCoin);
                holder.mTvSCoinValue.setText(sourceValueStr);

                holder.mTvTCoinValue.setText(targetValueStr);
                holder.mTvTCoinName.setText(mapModel.getCoinName());
            }else {
                holder.mLlSubContent.setVisibility(View.GONE);
                holder.locIcon.setVisibility(View.VISIBLE);
            }
        }



    }

    private void changeFirstTip(int action){
        Toast.makeText(mContext,action == 1?R.string.home_city_action_tip_move:R.string.home_city_action_tip_remove,Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return manager.getUserCountry().size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition == 0 || toPosition == 0){
            notifyDataSetChanged();
            changeFirstTip(1);
            return true;
        }
        manager.exchangeUserSelect(fromPosition,toPosition);
        //交换RecyclerView列表中item的位置
        notifyItemMoved(fromPosition,toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(final int position) {
        if (position == 0){
            notifyDataSetChanged();
            changeFirstTip(2);
            return;
        }
        ((BaseActivity)mContext).showCommonAlert(R.string.dialog_msg_home,new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //删除mItems数据
                manager.removeUserSelect(manager.getUserCountry().get(position));
                //删除RecyclerView列表对应item
                notifyItemRemoved(position);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onItemChange(int position) {
        Intent intent = new Intent(mContext, SelectActivity.class);
        intent.putExtra("type","change");
        intent.putExtra("index",position);
        ((BaseActivity)mContext).startActivityForResult(intent, UrlConstantV2.REQUEST.SELECT_COUNTRY);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nationName;
        TextView cityName;
        ImageView locIcon;
        TextView bigTime;
        TextView smallTime;
        ImageView countryIcon;

        LinearLayout mLlSubContent;
        TextView mTvSCoinName;
        TextView mTvSCoinValue;

        TextView mTvTCoinValue;
        TextView mTvTCoinName;
//        LinearLayout mLeft;
//        RelativeLayout mCenter;
//        LinearLayout mRight;

        public ViewHolder(View itemView) {
            super(itemView);
            countryIcon = (ImageView)itemView.findViewById(R.id.iv_home_icon);
            nationName = (TextView)itemView.findViewById(R.id.iv_home_list_country_nation);
            cityName = (TextView)itemView.findViewById(R.id.iv_home_list_country_city);
            locIcon = (ImageView)itemView.findViewById(R.id.iv_home_list_country_flag);
            bigTime = (TextView)itemView.findViewById(R.id.tv_home_list_country_time_big);
            smallTime = (TextView)itemView.findViewById(R.id.tv_home_list_country_time_small);

            mLlSubContent = (LinearLayout)itemView.findViewById(R.id.ll_home_list_item_sub);

            mTvSCoinName = (TextView)itemView.findViewById(R.id.tv_home_list_item_coin_name_s);
            mTvSCoinValue = (TextView)itemView.findViewById(R.id.tv_home_list_item_coin_value_s);

            mTvTCoinName = (TextView) itemView.findViewById(R.id.tv_home_list_item_coin_name2);
            mTvTCoinValue = (TextView)itemView.findViewById(R.id.tv_home_list_item_coin_ratio);
//            mLeft = (LinearLayout)itemView.findViewById(R.id.ll_left);
//            mCenter = (RelativeLayout)itemView.findViewById(R.id.ll_center);
//            mRight = (LinearLayout)itemView.findViewById(R.id.ll_right);
        }

    }

}
