package com.timediffproject.module.emoney;

import com.timediffproject.module.money.EMoneyResultModel2;

/**
 * Created by melon on 2017/12/18.
 */

public interface OnGetExchangeMoneyListener {

    void onGetExchangeMoneyFinish(boolean isSuccess,EMoneyResultModel2 model);

}
