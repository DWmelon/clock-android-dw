package com.timediffproject.module.home.homepage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONArray;
import com.timediffproject.R;
import com.timediffproject.application.BaseActivity;
import com.timediffproject.application.BaseFragment;
import com.timediffproject.application.MyClient;
import com.timediffproject.listener.OnUpdateTimeCallback;
import com.timediffproject.model.CountryModel;
import com.timediffproject.module.alarm.AlarmActivity;
import com.timediffproject.module.home.MyMainActivity;
import com.timediffproject.module.select.OnGetCountryByIdsListener;
import com.timediffproject.module.select.SelectActivity;
import com.timediffproject.util.V2ArrayUtil;

import java.util.List;

/**
 * Created by melon on 2017/1/3.
 */

public class MyMainFragment extends BaseFragment implements View.OnClickListener,OnUpdateTimeCallback,OnNotifyUserDataFinishListener,OnGetCountryByIdsListener {

    private Toolbar mToolbar;

    private RecyclerView mRvHomeList;
    private HomeListAdapter adapter;

    public static MyMainFragment newInstant(Bundle bundle){
        MyMainFragment fragment = new MyMainFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        UMENG_STR = "首页";
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.activity_my_main,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view){
        mToolbar = (Toolbar) view.findViewById(R.id.my_toolbar);

        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.icon_menu);
        mToolbar.setTitle("");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MyMainActivity) getActivity()).openDrawerLayout();
            }
        });

        mRvHomeList = (RecyclerView)view.findViewById(R.id.rv_home_list);
        mRvHomeList.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new HomeListAdapter(getActivity(), MyClient.getMyClient().getSelectManager());
        mRvHomeList.setAdapter(adapter);

        //关联ItemTouchHelper和RecyclerView
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRvHomeList);

        view.findViewById(R.id.fab_home_to_alarm).setOnClickListener(this);
        MyClient.getMyClient().getTimeManager().registerUpdateTimeCallBack(this);

        if (!MyClient.getMyClient().getSelectManager().isLoadUserInfoFinish()){
            ((BaseActivity)getActivity()).showProgress();
            MyClient.getMyClient().getSelectManager().registerUserDataListener(this);
        }else{
            requestUserInfo();
        }


    }

    private void requestUserInfo(){
        List<Long> ids = MyClient.getMyClient().getSelectManager().getUserSelectIds();
        MyClient.getMyClient().getSelectManager().requestCountryByIds(V2ArrayUtil.getJsonArrData(ids),this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(getActivity(), SelectActivity.class);
                intent.putExtra("type","normal");
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("main","pause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("main","resume");
        if (adapter != null){
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyClient.getMyClient().getTimeManager().unregisterUpdateTimeCallBack(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_home_to_alarm:{
                Intent intent = new Intent(getActivity(), AlarmActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    public void onUpdateTime() {
        if (getActivity() == null)return;
        if (adapter == null)return;
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onUserDataFinish() {
        ((BaseActivity)getActivity()).hideProgress();
        if (getActivity() == null){
            return;
        }

        updateAdapter();
        requestUserInfo();

    }

    @Override
    public void onGetCountryFinish(List<CountryModel> countryModelList) {
        if (getActivity() == null){
            return;
        }



        MyClient.getMyClient().getSelectManager().setUserSelectCountry(countryModelList);
        updateAdapter();
    }

    private void updateAdapter(){
        if (adapter != null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });

        }
    }

}