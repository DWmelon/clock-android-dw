package com.timediffproject.model;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by melon on 2017/7/2.
 */

public class BaseModel {

    protected static final String KEY_CODE = "code";
    protected static final String KEY_DATA = "data";
    protected static final String KEY_MSG = "msg";

    private int code;
    private String msg;

    public void decode(JSONObject object){
        if (object == null){
            return;
        }

        code = object.getIntValue(KEY_CODE);
        msg = object.getString(KEY_MSG);

    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
