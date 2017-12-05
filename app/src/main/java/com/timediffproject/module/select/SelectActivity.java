package com.timediffproject.module.select;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.timediffproject.R;
import com.timediffproject.application.BaseActivity;
import com.timediffproject.application.MyClient;
import com.timediffproject.model.CountryModel;
import com.timediffproject.module.search.SearchCountryActivity;

/**
 * Created by melon on 2017/1/6.
 */

public class SelectActivity extends BaseActivity implements View.OnClickListener,OnCountrySelectListener,OnGetCountryByStateListener{

    private static final int PAGE_STATE = 0;
    private static final int PAGE_COUNTRY = 1;



    private TextView mTvTitle;

    private ImageView mIvHome;

    //州
    private LinearLayout mLlState;

    private RelativeLayout mRlEurope;
    private SimpleDraweeView mSdvEurope;

    private RelativeLayout mRlAsia;
    private SimpleDraweeView mSdvAsia;

    private RelativeLayout mRlAfrica;
    private SimpleDraweeView mSdvAfrica;

    private RelativeLayout mRlAmerica;
    private SimpleDraweeView mSdvAmerica;

    private RelativeLayout mRlOceania;
    private SimpleDraweeView mSdvOceania;


    //国家城市
    private RecyclerView mRvSelect;
    private SelectRecycleAdapter adapterSelect;

    private View back;

    private String type;
    private int index;

    private int page = PAGE_STATE;
    private int state = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        umengPage = "选择页";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_country);
        type = getIntent().getStringExtra("type");
        index = getIntent().getIntExtra("index",-1);
        initView();
        initData();
        initListener();
    }

    private void initView(){
        back = findViewById(R.id.iv_back);


        mTvTitle    = (TextView)findViewById(R.id.select_title);
        mIvHome     = (ImageView)findViewById(R.id.iv_return_home);

        mLlState = (LinearLayout) findViewById(R.id.ll_select_state);
        mSdvEurope  = (SimpleDraweeView)findViewById(R.id.sdv_europe);
        mSdvAsia    = (SimpleDraweeView)findViewById(R.id.sdv_asia);
        mSdvAfrica  = (SimpleDraweeView)findViewById(R.id.sdv_africa);
        mSdvAmerica = (SimpleDraweeView)findViewById(R.id.sdv_america);
        mSdvOceania = (SimpleDraweeView)findViewById(R.id.sdv_oceania);

        mRlEurope   = (RelativeLayout)findViewById(R.id.rl_europe);
        mRlAsia     = (RelativeLayout)findViewById(R.id.rl_asia);
        mRlAfrica   = (RelativeLayout)findViewById(R.id.rl_africa);
        mRlAmerica  = (RelativeLayout)findViewById(R.id.rl_america);
        mRlOceania  = (RelativeLayout)findViewById(R.id.rl_oceania);

        mRvSelect = (RecyclerView)findViewById(R.id.rv_select_country);
        mRvSelect.setLayoutManager(new LinearLayoutManager(this));
        adapterSelect = new SelectRecycleAdapter(this, MyClient.getMyClient().getSelectManager(),this);
        mRvSelect.setAdapter(adapterSelect);

    }

    private void initData(){
        if (type.equals("normal")){

        }else{
            mTvTitle.setText("更换时区");
        }

        Uri uri = Uri.parse("res://"+getPackageName()+"/"+R.drawable.icon_europe);
        mSdvEurope.setImageURI(uri);

        Uri uri1 = Uri.parse("res://"+getPackageName()+"/"+R.drawable.icon_asia);
        mSdvAsia.setImageURI(uri1);

        Uri uri2 = Uri.parse("res://"+getPackageName()+"/"+R.drawable.icon_africa);
        mSdvAfrica.setImageURI(uri2);

        Uri uri3 = Uri.parse("res://"+getPackageName()+"/"+R.drawable.icon_america);
        mSdvAmerica.setImageURI(uri3);

        Uri uri4 = Uri.parse("res://"+getPackageName()+"/"+R.drawable.icon_oceania);
        mSdvOceania.setImageURI(uri4);

    }

    private void initListener(){
        back.setOnClickListener(this);
        mRlEurope.setOnClickListener(this);
        mRlAsia.setOnClickListener(this);
        mRlAfrica.setOnClickListener(this);
        mRlAmerica.setOnClickListener(this);
        mRlOceania.setOnClickListener(this);
        mIvHome.setOnClickListener(this);
        findViewById(R.id.rl_select_search).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:{
                onBackHandle();
                break;
            }
            case R.id.rl_europe:{
                MyClient.getMyClient().getSelectManager().requestCountryByState(SelectActivity.this,SelectManager.STATE_EUROPE_E,this);
                break;
            }
            case R.id.rl_asia:{
                MyClient.getMyClient().getSelectManager().requestCountryByState(SelectActivity.this,SelectManager.STATE_ASIA_E,this);
                break;
            }
            case R.id.rl_africa:{
                MyClient.getMyClient().getSelectManager().requestCountryByState(SelectActivity.this,SelectManager.STATE_AFRICA_E,this);
                break;
            }
            case R.id.rl_america:{
                MyClient.getMyClient().getSelectManager().requestCountryByState(SelectActivity.this,SelectManager.STATE_AMERICA_E,this);
                break;
            }
            case R.id.rl_oceania:{
                MyClient.getMyClient().getSelectManager().requestCountryByState(SelectActivity.this,SelectManager.STATE_OCEANIA_E,this);
                break;
            }
            case R.id.rl_select_search:{
                handleToSearch();
                break;
            }
            case R.id.iv_return_home:{
                finish();
                break;
            }
        }
    }

    private void handleStateClick(int state){
        this.state = state;
        adapterSelect.setState(state);
        changePageView(PAGE_COUNTRY);
    }

    private void handleToSearch(){
        Intent intent = new Intent(this,SearchCountryActivity.class);
        startActivityForResult(intent,0);
    }

    private void onBackHandle(){
        if (page == PAGE_STATE){
            finish();
        }else{
            changePageView(PAGE_STATE);
        }
    }

    private void changePageView(int page){
        this.page = page;
        if (page == PAGE_STATE){
            mLlState.setVisibility(View.VISIBLE);
            mRvSelect.setVisibility(View.GONE);
            mIvHome.setVisibility(View.GONE);
        }else{
            mLlState.setVisibility(View.GONE);
            mRvSelect.setVisibility(View.VISIBLE);
            mIvHome.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCountrySelect(View v,CountryModel model, int position) {

        SelectManager selectManager = MyClient.getMyClient().getSelectManager();

        if (type.equals("normal")){
            View selectIcon = v.findViewById(R.id.iv_public_list_country_select);
            if (selectManager.isUserSelected(model)){
                selectManager.removeUserSelect(model);
                selectIcon.setSelected(false);
            }else{
                selectManager.addUserSelect(model);
                selectIcon.setSelected(true);
            }
            adapterSelect.notifyItemChanged(position);
        }else{
            if (selectManager.isUserSelected(model)){
                Toast.makeText(this,"已存在该时区",Toast.LENGTH_SHORT).show();
            }else{
                if (index != -1){
                    selectManager.replaceUserSelect(model,index);
                }
                finish();
            }
        }

        MyClient.getMyClient().getSelectManager().setDataChange(true);
    }

    @Override
    public void onBackPressed() {
        onBackHandle();
    }

    @Override
    public void onGetCountryFinish(String state) {
        hideProgress();

        if (TextUtils.isEmpty(state)){
            return;
        }

        switch (state){
            case SelectManager.STATE_EUROPE_E:{
                handleStateClick(SelectManager.STATE_EUROPE);
                break;
            }
            case SelectManager.STATE_ASIA_E:{
                handleStateClick(SelectManager.STATE_ASIA);
                break;
            }
            case SelectManager.STATE_AFRICA_E:{
                handleStateClick(SelectManager.STATE_AFRICA);
                break;
            }
            case SelectManager.STATE_AMERICA_E:{
                handleStateClick(SelectManager.STATE_AMERICA);
                break;
            }
            case SelectManager.STATE_OCEANIA_E:{
                handleStateClick(SelectManager.STATE_OCEANIA);
                break;
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            finish();
        }
    }
}
