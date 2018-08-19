package com.timediffproject.module.emoney;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.timediffproject.R;
import com.timediffproject.application.BaseActivity;
import com.timediffproject.application.GlobalPreferenceManager;
import com.timediffproject.application.MyClient;
import com.timediffproject.model.CountryModel;
import com.timediffproject.module.money.EMoneyMapModel;
import com.timediffproject.module.money.EMoneyResultModel;
import com.timediffproject.module.set.OnSetCityChangeListener;
import com.timediffproject.module.set.SetCityDialogAdapter;
import com.timediffproject.util.CommonUtil;
import com.timediffproject.widgets.SlidingUpDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by melon on 2017/12/15.
 */

public class ExchangeMoneyActivity extends BaseActivity implements OnSetCityChangeListener,OnGetExchangeMoneyListener {

    //顶部
    @BindView(R.id.tv_exchange_nation_s)
    TextView mTvNationS;

    @BindView(R.id.tv_exchange_coin_s)
    TextView mTvCoinS;

    @BindView(R.id.tv_exchange_nation_t)
    TextView mTvNationT;

    @BindView(R.id.tv_exchange_coin_t)
    TextView mTvCoinT;

    //信息
    @BindView(R.id.tv_exchange_ratio_value)
    TextView mTvCurrRatio;

    @BindView(R.id.tv_exchange_time)
    TextView mTvCurrTime;

    //转换
    @BindView(R.id.tv_exchange_tip_s)
    TextView mTvExchangeTipS;

    @BindView(R.id.tv_exchange_tip_t)
    TextView mTvExchangeTipT;

    @BindView(R.id.et_exchange_ratio_source)
    EditText mEtRatio;

    @BindView(R.id.tv_exchange_value_target)
    TextView mTvValue;

    private final int VALUE_TARGET_S = 1;
    private final int VALUE_TARGET_T = 2;

    private EMoneyResultModel mResultModel;

    private SlidingUpDialog mNationDialog;
    private SetCityDialogAdapter mCityAdapter;

    private CountryModel mNationS;
    private CountryModel mNationT;

    private int mClickTarget = VALUE_TARGET_S;

    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_money);
        ButterKnife.bind(this);
        initData();
        initCityDialog();
    }

    private void initData(){
        Toolbar mToolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationIcon(R.drawable.icon_back_white);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.setTitle(R.string.exchange_ratio_title);

        language = GlobalPreferenceManager.getString(this,GlobalPreferenceManager.KEY_LANGUAGE);

        if (MyClient.getMyClient().getSelectManager().getUserCountry().size()>0){
            mNationS = MyClient.getMyClient().getSelectManager().getUserCountry().get(0);
            mTvNationS.setText(CommonUtil.getNationNameByLanguage(language,mNationS));
            mTvCoinS.setVisibility(View.VISIBLE);
            mTvCoinS.setText(mNationS.getCoinName());
        }else{
            mTvNationS.setText(R.string.exchange_ratio_choose);
        }

        mEtRatio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                culMoneyValue(editable.toString().trim());
            }
        });
    }

    private void initCityDialog(){
        mNationDialog = new SlidingUpDialog(this);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_set_city, null);
        mNationDialog.setContentView(contentView);
        RecyclerView mRvCity = (RecyclerView)contentView.findViewById(R.id.rv_dialog_city);
        mRvCity.setLayoutManager(new LinearLayoutManager(this));
        mCityAdapter = new SetCityDialogAdapter(this,getDialogData(mTvNationS.getText().toString().trim()));
        mCityAdapter.setListener(this);
        mRvCity.setAdapter(mCityAdapter);
    }

    private void refreshDialogData(){
        if (mClickTarget == VALUE_TARGET_S){
            mCityAdapter.setData(getDialogData(mTvNationT.getText().toString()));
        }else{
            mCityAdapter.setData(getDialogData(mTvNationS.getText().toString()));
        }
        mCityAdapter.notifyDataSetChanged();
    }

    private List<CountryModel> getDialogData(String alreadySelectNation){
        List<CountryModel> modelList = new ArrayList<>();
        for (CountryModel model : MyClient.getMyClient().getSelectManager().getUserCountry()){
            if (!CommonUtil.getNationNameByLanguage(language,model).equals(alreadySelectNation)){
                modelList.add(model);
            }
        }
        return modelList;
    }

    private void refreshView(){
        mTvCurrRatio.setText("--");
        mTvCurrTime.setText("--");
        mEtRatio.setText("");
        mTvValue.setText("0");
    }

    private void requestRatio(){
        refreshView();
        showProgress();
        MyClient.getMyClient().getMoneyManager().requestEMoney(mNationS.getNationName(),mNationT.getNationName(),this);
    }

    private void culMoneyValue(String valueS){
        if (mResultModel == null || TextUtils.isEmpty(valueS)){
            mTvValue.setText("");
            return;
        }
        float valueF = Float.parseFloat(valueS);
        mTvValue.setText(String.valueOf(valueF*mResultModel.getModelList().get(0).getValue()));
    }

    @OnClick(R.id.ll_exchange_source)
    void showSourceNationDialog(){
        showNationDialog(VALUE_TARGET_S);
    }

    @OnClick(R.id.ll_exchange_target)
    void showTargetNationDialog(){
        showNationDialog(VALUE_TARGET_T);
    }

    private void showNationDialog(int type){
        mClickTarget = type;
        refreshDialogData();
        if (mNationDialog != null){
            mNationDialog.show();
        }
    }

    @Override
    public void onChangeCity(CountryModel model) {
        if (mClickTarget == VALUE_TARGET_S){
            mNationS = model;
            mTvNationS.setText(CommonUtil.getNationNameByLanguage(language,mNationS));
            mTvCoinS.setVisibility(View.VISIBLE);
            mTvCoinS.setText(mNationS.getCoinName());
        }else{
            mNationT = model;
            mTvNationT.setText(CommonUtil.getNationNameByLanguage(language,mNationT));
            mTvCoinT.setVisibility(View.VISIBLE);
            mTvCoinT.setText(mNationT.getCoinName());
        }

        if (mNationT != null && mNationS != null){
            requestRatio();
        }

        if (mNationDialog != null){
            mNationDialog.dismiss();
        }
    }

    @Override
    public void onGetExchangeMoneyFinish(boolean isSuccess, EMoneyResultModel model) {
        hideProgress();
        if (!isSuccess || model == null){
            Toast.makeText(this,R.string.exchange_ratio_request_fail,Toast.LENGTH_LONG).show();
            return;
        }

        if (model.getModelList().isEmpty()){
            return;
        }

        EMoneyMapModel targetModel = model.getModelList().get(0);
        if (!mNationS.getNationName().equals(model.getSourceNation()) || !mNationT.getNationName().equals(targetModel.getCoinNation())){
            return;
        }

        mResultModel = model;

        mTvCurrRatio.setText(String.valueOf(targetModel.getValue()));
        mTvCurrTime.setText(model.getUpdateTime());
        mTvExchangeTipS.setText(getString(R.string.exchange_ratio_tip_source,model.getSourceCoin()));
        mTvExchangeTipT.setText(getString(R.string.exchange_ratio_tip_target,targetModel.getCoinName()));

    }
}
