package com.timediffproject.module.home;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;

import com.timediffproject.R;
import com.timediffproject.application.BaseActivity;
import com.timediffproject.application.GlobalPreferenceManager;
import com.timediffproject.application.MyClient;
import com.timediffproject.model.CountryModel;
import com.timediffproject.module.misc.OnUpdateCheckListener;
import com.timediffproject.network.UrlConstantV2;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by melon on 2017/1/3.
 */

public class MyMainActivity extends BaseActivity implements UMShareListener,OnUpdateCheckListener{

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

        MyClient.getMyClient().getMiscManager().updateCheck(this);
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
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (requestCode == UrlConstantV2.REQUEST.CHANGE_CITY && resultCode == RESULT_OK){
            MyClient.getMyClient().getMoneyManager().requestEMoney();
        }
    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {

    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {

    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {

    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {

    }

    private void handleVersion(AppUpdateCheckModel model){
        int code = Integer.parseInt(model.getvCode());
        String pkName = this.getPackageName();
        int vCode = GlobalPreferenceManager.getVersionCode(this);
        try {
            int versionCode = getPackageManager().getPackageInfo(
                    pkName, 0).versionCode;
            if (code > versionCode && code != vCode){
                GlobalPreferenceManager.saveVersionCode(this,code);
                GlobalPreferenceManager.saveVersionName(this,model.getvName());
                GlobalPreferenceManager.saveVersionInfo(this,model.getvInfo());
                showCommonAlert(getString(R.string.update_title),model.getvInfo());
                //兼容老版本
                if (code<=4){
                    GlobalPreferenceManager.setRefreshAlarm(this,true);
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateCheck(AppUpdateCheckModel model) {
        if (model == null || model.getvCode().isEmpty()){
            return;
        }

        handleVersion(model);

    }

}
