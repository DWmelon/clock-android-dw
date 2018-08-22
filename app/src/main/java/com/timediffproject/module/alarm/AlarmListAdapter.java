package com.timediffproject.module.alarm;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.suke.widget.SwitchButton;
import com.timediffproject.R;
import com.timediffproject.application.BaseActivity;
import com.timediffproject.application.GlobalPreferenceManager;
import com.timediffproject.application.MyClient;
import com.timediffproject.constants.Constants;
import com.timediffproject.database.AlarmModel;
import com.timediffproject.model.CountryModel;
import com.timediffproject.module.set.SetAlarmUtil;
import com.timediffproject.module.set.SettingTimeActivity;
import com.timediffproject.util.CommonUtil;
import com.timediffproject.util.DateUtil;
import com.timediffproject.util.RandomUtil;
import com.timediffproject.util.SpanUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by melon on 2017/1/8.
 */

public class AlarmListAdapter extends RecyclerView.Adapter<AlarmListAdapter.ViewHolder> {

    private Context mContext;

    private MyAlarmManager manager;

    private List<AlarmModel> alarmModelList = new ArrayList<>();

    String language;

    public AlarmListAdapter(Context context, MyAlarmManager manager){
        this.mContext = context;
        this.manager = manager;
        alarmModelList = manager.getAlarmModelList();
        language = GlobalPreferenceManager.getString(mContext,GlobalPreferenceManager.KEY_LANGUAGE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_alarm_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final AlarmModel model = alarmModelList.get(position);
        CountryModel countryModel = MyClient.getMyClient().getSelectManager().getNationById(model.getCityId());

        holder.mSbAlarmSet.setChecked(model.getUsing());
        holder.mSbAlarmSet.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){

                    int index = alarmModelList.indexOf(model);

                    //关闭以前的闹钟 以防万一
                    manager.cancelAlarm(mContext,model);

                    //计算新闹钟
                    SetAlarmUtil.culRestartAlarm(model);

                    //重新设置id
                    model.setRequestCode((long) RandomUtil.getRandomInt());

                    //添加新闹钟
                    manager.addOnceAlarm(mContext,model,-1);

                }else{
                    manager.cancelAlarm(mContext,model);
                }
            }
        });

        Date date = SetAlarmUtil.getModifyTime(countryModel.getDiffTime(),model.getAlarmTime());
        holder.mTvAlarmTime.setText(DateUtil.getHourFormat(mContext,date));
        if (GlobalPreferenceManager.isUse24Hours(mContext)){
            holder.mTvAlarmTimeAP.setVisibility(View.GONE);
        }else{
            holder.mTvAlarmTimeAP.setText(DateUtil.getTimeAP(date));
            holder.mTvAlarmTimeAP.setVisibility(View.VISIBLE);
        }

        CountryModel tempModel = new CountryModel();
        tempModel.setCityName(model.getCity());
        tempModel.setCityNameE(CommonUtil.getCityEByCityC(model.getCity()));
        holder.mTvAlarmCity.setText(CommonUtil.getCityNameByLanguage(language,tempModel));

        Date localTimeDate = culLocalTime(date,model.getCityId());
        SpannableString localTime = handleLocalTime(date,localTimeDate);
        if (localTime == null || isLocalCity(model.getCityId())){
            holder.mLlExchange.setVisibility(View.GONE);
        }else{
            holder.mLlExchange.setVisibility(View.VISIBLE);
            holder.mTvAlarmLocal.setText(localTime);
            if (GlobalPreferenceManager.isUse24Hours(mContext)){
                holder.mTvAlarmLocalAP.setVisibility(View.GONE);
            }else{
                holder.mTvAlarmLocalAP.setText(DateUtil.getTimeAP(localTimeDate));
                holder.mTvAlarmLocalAP.setVisibility(View.VISIBLE);
            }

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SettingTimeActivity.class);
                intent.putExtra(Constants.INTENT_KEY_ALARM_ID,model.getRequestCode());
                intent.putExtra(SettingTimeActivity.KEY_PAGE_TYPE,SettingTimeActivity.PAGE_TYPE_MODIFY);
                intent.putExtra(Constants.INTENT_KEY_CITY_ID,model.getCityId());
                mContext.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ((BaseActivity)mContext).showCommonAlert(R.string.dialog_msg_clock, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    MyClient.getMyClient().getMyAlarmManager().cancelAlarm(mContext,model);
                    MyClient.getMyClient().getMyAlarmManager().removeAlarm(mContext,model);

                    notifyItemRemoved(position);
                    Toast.makeText(mContext,R.string.delete_success,Toast.LENGTH_LONG).show();

                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((BaseActivity)mContext).hideDialog();
                    }
                });
                return true;
            }
        });
    }

    private Date culLocalTime(Date ringDate,Long cityId){
        List<CountryModel> countryModelList = MyClient.getMyClient().getSelectManager().getUserCountry();
        if (countryModelList == null || countryModelList.isEmpty()){
            return null;
        }


        CountryModel myCountry = countryModelList.get(0);
        CountryModel alarmCountry = MyClient.getMyClient().getSelectManager().getNationById(cityId);

        if (myCountry.getId().equals(alarmCountry.getId())){
            return null;
        }

        float diffHour = myCountry.getDiffTime() - alarmCountry.getDiffTime();

//        Calendar calendarNow = Calendar.getInstance();
//        calendarNow.setTime(new Date(SetAlarmUtil.geiBeijingAlarmTime(alarmModel.getAlarmTime())));

//        int hour = (int)diffHour;
        int millis = (int) (diffHour*60*60*1000);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ringDate.getTime()+millis);

        return calendar.getTime();

    }

    private SpannableString handleLocalTime(Date AlarmDate,Date targetDate){
        if (targetDate == null){
            return null;
        }
        String city = mContext.getString(R.string.beijing);
        List<CountryModel> countryModelList = MyClient.getMyClient().getSelectManager().getUserCountry();
        if (countryModelList != null && !countryModelList.isEmpty()){
            city = CommonUtil.getCityNameByLanguage(language,countryModelList.get(0));
        }

        Calendar calendarT = Calendar.getInstance();
        calendarT.setTime(targetDate);
        Calendar calendarS = Calendar.getInstance();
        calendarS.setTime(AlarmDate);
        String dayTip = "";
        if (calendarT.compareTo(calendarS)<0){
            if (calendarT.get(Calendar.DAY_OF_MONTH)!=calendarS.get(Calendar.DAY_OF_MONTH)){
                dayTip = "(昨)";
            }
        }else{
            if (calendarT.get(Calendar.DAY_OF_MONTH)!=calendarS.get(Calendar.DAY_OF_MONTH)){
                dayTip = "(明)";
            }
        }

        String time = DateUtil.getHourFormat(mContext,targetDate);
        String localTime = mContext.getString(R.string.alarm_item_tip,city,time,dayTip);
        int index = localTime.indexOf("：");
        SpannableString span = SpanUtil.getSpannableString(localTime,
                new ForegroundColorSpan(mContext.getResources().getColor(R.color.white)),index+1,index+6);
        span = SpanUtil.getSpannableString(span,new RelativeSizeSpan(1.2f),index+1,index+6);
        return span;
    }

    private boolean isLocalCity(Long cityId){
        List<CountryModel> countryModelList = MyClient.getMyClient().getSelectManager().getUserCountry();
        if (countryModelList == null || countryModelList.isEmpty()){
            return true;
        }
        CountryModel model = countryModelList.get(0);
        return cityId.equals(model.getId());
    }

    @Override
    public int getItemCount() {
        return alarmModelList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTvAlarmTime;
        TextView mTvAlarmTimeAP;
        TextView mTvAlarmCity;

        LinearLayout mLlExchange;
        TextView mTvAlarmLocal;
        TextView mTvAlarmLocalAP;
        SwitchButton mSbAlarmSet;


        public ViewHolder(View itemView) {
            super(itemView);
            mTvAlarmTime = (TextView)itemView.findViewById(R.id.tv_alarm_time);
            mTvAlarmTimeAP = (TextView)itemView.findViewById(R.id.tv_alarm_time_ap);
            mTvAlarmCity = (TextView)itemView.findViewById(R.id.tv_alarm_city);

            mLlExchange = (LinearLayout)itemView.findViewById(R.id.ll_exchange_time);
            mTvAlarmLocal = (TextView)itemView.findViewById(R.id.tv_alarm_local_time);
            mTvAlarmLocalAP = (TextView)itemView.findViewById(R.id.tv_alarm_local_time_ap);
            mSbAlarmSet = (SwitchButton)itemView.findViewById(R.id.sb_alarm_btn);
        }

    }

}
