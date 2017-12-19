package com.timediffproject.module.emoney;

import com.timediffproject.module.money.EMoneyResultModel;

/**
 * Created by melon on 2017/12/18.
 */

public interface OnGetExchangeMoneyListener {

    void onGetExchangeMoneyFinish(boolean isSuccess,EMoneyResultModel model);

}
