package com.timediffproject.module.search;

import com.timediffproject.model.CountryModel;

import java.util.List;

/**
 * Created by melon on 2017/10/2.
 */

public interface OnSearchCountryListener {

    void onSearchCountryFinish(List<CountryModel> countryModelList);

}
