package com.timediffproject.module.select;

import com.timediffproject.model.CountryModel;

import java.util.List;

/**
 * Created by melon on 2017/7/2.
 */

public interface OnGetCountryByIdsListener {

    void onGetCountryFinish(List<CountryModel> countryModelList);

}
