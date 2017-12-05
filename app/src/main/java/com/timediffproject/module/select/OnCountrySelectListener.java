package com.timediffproject.module.select;

import android.view.View;

import com.timediffproject.model.CountryModel;

/**
 * Created by melon on 2017/1/8.
 */

public interface OnCountrySelectListener {

    void onCountrySelect(View v, CountryModel model, int position);

}
