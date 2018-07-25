package com.timediffproject.oss;

import com.alibaba.fastjson.JSONObject;
import com.timediffproject.model.BaseModel;

/**
 * Created by melon on 2017/8/5.
 */

public class OSSConfigModel extends BaseModel {

    private static String KEY_ACCESS_KEY_ID = "accessKeyId";
    private static String KEY_ACCESS_KEY_SECRET = "accessKeySecret";
    private static String KEY_SECURITY_TOKEN = "securityToken";
    private static String KEY_EXPIRATION = "expiration";

    private String accessKeyId;

    private String accessKeySecret;

    private String securityToken;

    private String expiration;

    public void decode(JSONObject object){
        if (object == null){
            return;
        }
        super.decode(object);

        object = object.getJSONObject(KEY_DATA);
        if (object == null){
            return;
        }

        accessKeyId = object.getString(KEY_ACCESS_KEY_ID);
        accessKeySecret = object.getString(KEY_ACCESS_KEY_SECRET);
        securityToken = object.getString(KEY_SECURITY_TOKEN);
        expiration = object.getString(KEY_EXPIRATION);

    }



    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }
}
