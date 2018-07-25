package com.timediffproject.module.home;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;

import com.timediffproject.R;
import com.timediffproject.application.BaseActivity;
import com.timediffproject.application.GlobalPreferenceManager;
import com.timediffproject.application.MyClient;
import com.timediffproject.application.UpdateActivity;
import com.timediffproject.module.misc.OnUpdateCheckListener;
import com.timediffproject.network.UrlConstantV2;
import com.timediffproject.storage.StorageManager;

import java.io.File;

/**
 * Created by melon on 2017/1/3.
 */

public class MyMainActivity extends UpdateActivity{

    DrawerLayout drawerLayout;

    private RecyclerView mRvLeftLayout;
    private LeftDrawerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_drawer);
        TabFragment fragment = TabFragment.newInstant(null);
        getSupportFragmentManager().beginTransaction().add(R.id.f_tab_fragment,fragment).commit();

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            //将侧边栏顶部延伸至status bar
            drawerLayout.setFitsSystemWindows(true);
            //将主页面顶部延伸至status bar;虽默认为false,但经测试,DrawerLayout需显示设置
            drawerLayout.setClipToPadding(false);
        }

        adapter = new LeftDrawerAdapter(this);
        mRvLeftLayout = (RecyclerView)findViewById(R.id.left_drawer);
        mRvLeftLayout.setAdapter(adapter);
        mRvLeftLayout.setLayoutManager(new LinearLayoutManager(this));

        checkUpdate();
    }

    public void openDrawerLayout(){
        drawerLayout.openDrawer(Gravity.LEFT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UrlConstantV2.REQUEST.SELECT_COUNTRY){
            if (MyClient.getMyClient().getSelectManager().isCountryDataChange()){
                MyClient.getMyClient().getMoneyManager().requestEMoney();
                MyClient.getMyClient().getSelectManager().setCountryDataChange(false);
            }
        }
    }





}
