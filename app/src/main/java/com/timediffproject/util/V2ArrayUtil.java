package com.timediffproject.util;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by melon on 2017/3/25.
 */

public class V2ArrayUtil {

    public static<T> String getJsonArrData(List<T> data){
        if (data == null){
            return "";
        }
        JSONArray array = new JSONArray();
        Iterator<T> iterator=data.iterator();
        while(iterator.hasNext()){
            array.add(iterator.next());
        }
        if (array.isEmpty()){
            return "";
        }
        return array.toString();
    }

    public static<T> String getJsonArrData(T data){
        List<T> values = new ArrayList<>();
        values.add(data);
        return getJsonArrData(values);
    }

    public static<T> String mergeJsonArrData(List<String> jsonArr1,List<String> jsonArr2){
        List<String> list = new ArrayList<>();
        if (jsonArr1 != null){
            list.addAll(jsonArr1);
        }
        if (jsonArr2 != null){
            list.addAll(jsonArr2);
        }
        return getJsonArrData(list);
    }

    public static List<String> getListByJson(String json){
        List<String> list = new ArrayList<>();
        if (TextUtils.isEmpty(json)){
            return list;
        }

        JSONArray array = JSONArray.parseArray(json);
        for (int i = 0; i<array.size();i++){
            list.add(array.getString(i));
        }
        return list;
    }

}
