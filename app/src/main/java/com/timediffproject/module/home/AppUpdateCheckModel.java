package com.timediffproject.module.home;

import com.alibaba.fastjson.JSONObject;
import com.timediffproject.model.BaseModel;

/**
 * Created by melon on 2017/10/27.
 */

public class AppUpdateCheckModel extends BaseModel {

    private final static String KEY_V_CODE = "v_code";
    private final static String KEY_V_NAME = "v_name";
    private final static String KEY_V_INFO = "v_info";
    private final static String KEY_V_PATH = "v_path";

    private String vCode;
    private String vName;
    private String vInfo;
    private String vPath;

    public void decode(JSONObject object){
        super.decode(object);
        if (object == null){
            return;
        }
        object = object.getJSONObject(KEY_DATA);
        if (object == null){
            return;
        }

        vCode = object.getString(KEY_V_CODE);
        vName = object.getString(KEY_V_NAME);
        vInfo = object.getString(KEY_V_INFO);
        vPath = object.getString(KEY_V_PATH);
    }

    public String getvCode() {
        return vCode;
    }

    public void setvCode(String vCode) {
        this.vCode = vCode;
    }

    public String getvName() {
        return vName;
    }

    public void setvName(String vName) {
        this.vName = vName;
    }

    public String getvInfo() {
        return vInfo;
    }

    public void setvInfo(String vInfo) {
        this.vInfo = vInfo;
    }

    public String getvPath() {
        return vPath;
    }

    public void setvPath(String vPath) {
        this.vPath = vPath;
    }
}
