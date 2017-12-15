package com.timediffproject.module.search;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.timediffproject.R;
import com.timediffproject.application.BaseActivity;
import com.timediffproject.application.MyClient;
import com.timediffproject.model.CountryModel;
import com.timediffproject.module.select.SelectManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by melon on 2017/10/2.
 */

public class SearchCountryActivity extends BaseActivity implements View.OnClickListener,OnSearchCountryListener,OnSearchClickListener {

    private EditText mEtSearch;

    private ImageView mIvSearch;

    private ListView mLvSearch;
    private SearchCountryAdapter adapter;

    private String type;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        umengPage = "搜索地区页";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_country);

        type = getIntent().getStringExtra("type");
        index = getIntent().getIntExtra("index",-1);

        initView();
        initData();
        initListener();

    }

    private void initView(){
        mEtSearch = (EditText)findViewById(R.id.et_search);
        mIvSearch = (ImageView)findViewById(R.id.iv_search);

        mLvSearch = (ListView)findViewById(R.id.lv_search);
    }

    private void initData(){
        adapter = new SearchCountryAdapter(this);
        adapter.setListener(this);
        mLvSearch.setAdapter(adapter);

    }

    private void initListener(){
        mIvSearch.setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.iv_return_home).setOnClickListener(this);
    }

    private void handleSearch(){
        String keyword = mEtSearch.getText().toString().trim();

        if (TextUtils.isEmpty(keyword)){
            adapter.setData(new ArrayList<CountryModel>());
        }else{
            MyClient.getMyClient().getSearchManager().searchCountry(keyword,this);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:{
                finish();
                break;
            }
            case R.id.iv_search:{
                handleSearch();
                break;
            }
            case R.id.iv_return_home:{
                setResult(RESULT_OK);
                finish();
                break;
            }
        }

    }

    @Override
    public void onSearchCountryFinish(List<CountryModel> countryModelList) {
        adapter.setData(countryModelList);
    }

    @Override
    public void onSelectItem(View view, CountryModel model) {
        SelectManager selectManager = MyClient.getMyClient().getSelectManager();
        if (type.equals("normal")){
            if (selectManager.isUserSelected(model)){
                selectManager.removeUserSelect(model);
                view.setSelected(false);
            }else{
                selectManager.addUserSelect(model);
                view.setSelected(true);
            }
        }else{
            if (selectManager.isUserSelected(model)){
                Toast.makeText(this,"已存在该时区",Toast.LENGTH_SHORT).show();
            }else{
                if (index != -1){
                    selectManager.replaceUserSelect(model,index);
                }
                setResult(RESULT_OK);
                finish();
            }
        }
        MyClient.getMyClient().getSelectManager().setCountryDataChange(true);
    }
}
