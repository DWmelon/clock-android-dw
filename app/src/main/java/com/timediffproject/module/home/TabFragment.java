package com.timediffproject.module.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timediffproject.R;
import com.timediffproject.application.BaseFragment;
import com.timediffproject.module.home.homepage.MyMainFragment;

/**
 * Created by melon on 2017/2/16.
 */

public class TabFragment extends BaseFragment implements View.OnClickListener{

    private static final String TAB_HOME = MyMainFragment.class.getName();

    private static final String TAB_TEST = com.timediffproject.module.home.TestFragment.class.getName();

    private BaseFragment mLastShowFragment;
    private View mLastSelectView;

    private View mContentView;

    private static TabFragment fragment;

    public static TabFragment newInstant(Bundle bundle){
        if (fragment == null){
            fragment = new TabFragment();
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTabInfo();

    }

    private void initTabInfo(){
        FragmentManager fm = getFragmentManager();
        if (fm == null){
            return;
        }
        FragmentTransaction ft = fm.beginTransaction();

        BaseFragment home = (BaseFragment) fm.findFragmentByTag(TAB_HOME);
        if (home != null){
            ft.hide(home);
        }

        BaseFragment test = (BaseFragment) fm.findFragmentByTag(TAB_TEST);
        if (test != null){
            ft.hide(test);
        }

        ft.commit();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContentView = view;
        mLastSelectView = mContentView.findViewById(R.id.btn_change_home);
        view.findViewById(R.id.btn_change_home).setOnClickListener(this);
        view.findViewById(R.id.btn_change_test).setOnClickListener(this);
        switchTo(TAB_HOME,null);
    }

    //切换Fragment的方式（FragmentB、FragmentC）
    //tab为Fragment的类名（如：FragmentB.class.getname()）
    //R.id.fl_container是在Activity的布局里，不是在FragmentA的布局里
    private void switchTo(String tab, Bundle bundle){

        //初始化管理Fragment的类
        FragmentManager fm = getFragmentManager();
        if (fm == null){
            return;
        }
        FragmentTransaction ft = fm.beginTransaction();

        //从FragmentManager里寻找类名为tab的Fragment
        BaseFragment fragment = (BaseFragment)fm.findFragmentByTag(tab);
        if (fragment == null){
            fragment = (BaseFragment) Fragment.instantiate(getActivity(),tab);
            fragment.setArguments(bundle);
            ft.add(R.id.fl_container,fragment,tab);
        }else{
            ft.show(fragment);
        }
        //隐藏现在正显示的Fragment
        if (mLastShowFragment != null) {
            ft.hide(mLastShowFragment);
        }
        //记录最后点击的Fragment
        mLastShowFragment = fragment;

        ft.commitAllowingStateLoss();


    }


    @Override
    public void onClick(View view) {
        if (mLastSelectView == view){
            return;
        }

        switch (view.getId()){
            case R.id.btn_change_home:
                switchTo(TAB_HOME,null);
                break;
            case R.id.btn_change_test:
                switchTo(TAB_TEST,null);
                break;
        }

        mLastSelectView = view;

    }
}
