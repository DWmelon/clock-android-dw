package com.timediffproject.module.set;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.timediffproject.R;
import com.timediffproject.application.MyClient;

/**
 * Created by melon on 2017/2/28.
 */

public class SetCityDialog extends Dialog{

    private View contentView;

    private RecyclerView mRvCity;
    private SetCityDialogAdapter adapter;

    private int height;

    private OnSetCityChangeListener listener;

    public void setListener(OnSetCityChangeListener listener){
        this.listener = listener;
        if (adapter != null){
            adapter.setListener(listener);
        }
    }

    public SetCityDialog(Context context) {
        this(context, R.style.MutiDialogStyle);
    }

    private SetCityDialog(Context context, int theme) {
        super(context, theme);
        initView();
        initData();
        initAttribute();
    }

    private void initView() {

        //todo rv adpter height
        contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_set_city, null);
        mRvCity = (RecyclerView)contentView.findViewById(R.id.rv_dialog_city);
    }

    private void initData(){


        int count = MyClient.getMyClient().getSelectManager().getUserCountry().size();
        height = getContext().getResources().getDimensionPixelOffset(R.dimen.margin_15)
                + (2 + count)  * getContext().getResources().getDimensionPixelOffset(R.dimen.margin_40)
                + getContext().getResources().getDimensionPixelOffset(R.dimen.margin_05);

        int h = count * getContext().getResources().getDimensionPixelOffset(R.dimen.margin_40);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,h);
        mRvCity.setLayoutParams(params);

        adapter = new SetCityDialogAdapter(getContext(),MyClient.getMyClient().getSelectManager().getUserCountry());
        mRvCity.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvCity.setAdapter(adapter);

    }

    private void initAttribute(){
        WindowManager.LayoutParams wlp = getWindow().getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = height;
        getWindow().setAttributes(wlp);
        getWindow().setBackgroundDrawableResource(R.color.green);
        getWindow().setWindowAnimations(R.style.CustomDialog);
        setCanceledOnTouchOutside(true);



        getWindow().getDecorView().setBackgroundColor(getContext().getResources().getColor(R.color.transparent));
        ((FrameLayout) getWindow().getDecorView()).setForegroundGravity(Gravity.BOTTOM);

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        addContentView(contentView, lp);
        setCancelable(true);
    }

}
