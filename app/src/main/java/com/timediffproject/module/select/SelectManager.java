package com.timediffproject.module.select;

import android.content.Context;

import com.timediffproject.application.BaseActivity;
import com.timediffproject.application.GlobalPreferenceManager;
import com.timediffproject.application.MyClient;
import com.timediffproject.constants.Constant;
import com.timediffproject.model.CountryModel;

import com.alibaba.fastjson.JSONObject;
import com.timediffproject.model.CountryModelList;
import com.timediffproject.module.alarm.OnLoadAlarmFinishListener;
import com.timediffproject.module.home.homepage.OnNotifyUserDataFinishListener;
import com.timediffproject.network.IRequest;
import com.timediffproject.network.IRequestCallback;
import com.timediffproject.storage.StorageManager;
import com.timediffproject.storage.TaskExecutor;
import com.timediffproject.util.CharUtil;
import com.timediffproject.util.FileUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by melon on 2017/1/8.
 */

public class SelectManager {

    private static final String URL_GET_COUNTRY_BY_STATE = Constant.MAIN_URL + "/country/list/state";
    private static final String URL_GET_COUNTRY_BY_IDS = Constant.MAIN_URL + "/country/list/ids";
//    private static final String URL_GET_COUNTRY_BY_IDS = "http://192.168.2.156:12344/country/list/ids";



    private static final String DATA_NAME = "country_data.txt";



    public static final int STATE_ALL = 0;
    public static final int STATE_EUROPE = 1;
    public static final int STATE_ASIA = 2;
    public static final int STATE_AFRICA = 3;
    public static final int STATE_AMERICA = 4;
    public static final int STATE_OCEANIA = 5;

    public static final String STATE_EUROPE_E = "europe";
    public static final String STATE_ASIA_E = "asia";
    public static final String STATE_AFRICA_E = "africa";
    public static final String STATE_AMERICA_E = "america";
    public static final String STATE_OCEANIA_E = "oceania";

    private Context context;

    private HashMap<String,List<CountryModel>> countryHashMap = new HashMap<>();

//    private List<CountryModel> countryModelList = new ArrayList<>();

    private List<CountryModel> countryEuropeList = new ArrayList<>();
    private List<CountryModel> countryAfricaList = new ArrayList<>();
    private List<CountryModel> countryAsiaList = new ArrayList<>();
    private List<CountryModel> countryAmericaList = new ArrayList<>();
    private List<CountryModel> countryOceaniaList = new ArrayList<>();

    private HashMap<Long,CountryModel> userCountryMap = new HashMap<>();

    private List<CountryModel> userCountry = new ArrayList<>();

    private boolean isCountryDataChange = false;

    private boolean isLoadUserInfoFinish = false;
    private OnNotifyUserDataFinishListener listener;

    public void registerUserDataListener(OnNotifyUserDataFinishListener listener){
        this.listener = listener;
    }

    public void init(Context context){

//        if (countryModelList !=null && !countryModelList.isEmpty()){
//            return;
//        }

        this.context = context;
        getUserSelectFromFile();
//        initUserSelect();
//        TaskExecutor.getInstance().post(new Runnable() {
//            @Override
//            public void run() {
//                loadData();
//            }
//        });


    }



    private void sortCountry(List<CountryModel> modelList){
        Collections.sort(modelList, new Comparator<CountryModel>() {
            @Override
            public int compare(CountryModel lhs, CountryModel rhs) {
                return lhs.getNationNamePy().compareTo(rhs.getNationNamePy());
            }
        });

    }

    private void sortChar(List<CountryModel> modelList){
        if (modelList == null){
            return;
        }
        String chars = "";
        for (int i = 0;i<modelList.size();i++){
            CountryModel model = modelList.get(i);
            if (!chars.equals(model.getFirstChar())){
                model.setFirstData(true);
            }else{
                model.setFirstData(false);
            }
            chars = model.getFirstChar();
        }
    }

    private void initUserSelect(List<CountryModel> modelList){
        if (modelList == null || modelList.isEmpty()){
            CountryModel model = getBJCountry();
            userCountry.add(model);
            String str = ":"+model.getId();
            saveUserSelectToFile(str);
        }else{
            userCountry = modelList;
        }

        for (CountryModel model : userCountry){
            CountryModel modelC = (CountryModel) model.clone();
            userCountryMap.put(model.getId(),modelC);
//            countryModelList.add(modelC);
        }

        isLoadUserInfoFinish = true;
        if (listener != null){
            listener.onUserDataFinish();
        }
    }

    private CountryModel getBJCountry(){
        CountryModel model = new CountryModel();
        model.setId(342L);
        model.setState("asia");
        model.setNationName("中国");
        model.setCityName("北京");
        model.setCityNameE("Beijing");
        model.setLogo("flag_of_china");
        model.setDiffTime(0);
        model.setNationNamePy(CharUtil.cn2py(model.getNationName()));

        return model;
    }

    public void exchangeUserSelect(int positionO,int positionT){
        CountryModel modelO = getUserCountry().get(positionO);
        CountryModel modelT = getUserCountry().get(positionT);

        //交换数据的位置
        Collections.swap(userCountry,positionO,positionT);

        String str = GlobalPreferenceManager.getUserSelect(context);
        str = str.replace(":"+modelO.getId(),":"+"?"+modelT.getId());
        str = str.replace(":"+modelT.getId(),":"+modelO.getId());
        str = str.replace("?","");
        saveUserSelectToFile(str);
    }

    public void addUserSelect(CountryModel mo){
        CountryModel model = (CountryModel) mo.clone();
        userCountry.add(model);
        String str = GlobalPreferenceManager.getUserSelect(context);
        str += ":"+model.getId();
        userCountryMap.put(model.getId(),model);
//        countryModelList.add(model);
        saveUserSelectToFile(str);
    }

    public void removeUserSelect(CountryModel model){
        for (CountryModel mo : userCountry){
            if (mo.getId().equals(model.getId())){
                userCountry.remove(mo);
                break;
            }
        }
        userCountryMap.remove(model.getId());
        String str = GlobalPreferenceManager.getUserSelect(context);
        str = str.replace(":"+model.getId(),"");
        saveUserSelectToFile(str);
        MyClient.getMyClient().getMyAlarmManager().checkAlarmWithUserCountry();
    }

    public void replaceUserSelect(CountryModel model,int position){
        String id = String.valueOf(userCountry.get(position).getId());

        userCountry.set(position,model);

        String str = GlobalPreferenceManager.getUserSelect(context);
        str = str.replace(id,String.valueOf(model.getId()));
        saveUserSelectToFile(str);
    }

    /**
     * 保存用户选择城市到文件
     */
    public synchronized void saveUserSelectToFile(String str){
        GlobalPreferenceManager.saveUserSelect(context,str);

        TaskExecutor.getInstance().post(new Runnable() {
            @Override
            public void run() {
                CountryModelList list = new CountryModelList();
                if (userCountry != null){
                    list.setCountryModelList(userCountry);
                }
                String path = MyClient.getMyClient().getStorageManager().getPackageFiles() + "userselect";
                FileUtil.writeObjectToPath(list, path);
            }
        });
    }

    /**
     * 从文件中用户选择城市设置信息
     */
    public void getUserSelectFromFile(){


        TaskExecutor.getInstance().post(new Runnable() {
            @Override
            public void run() {
                synchronized (SelectManager.class) {

                    String path = MyClient.getMyClient().getStorageManager().getPackageFiles() + "userselect";
                    Object object = FileUtil.readObjectFromPath(path);

                    if (object != null && object instanceof CountryModelList) {
                        CountryModelList list = (CountryModelList) object;
                        initUserSelect(list.getCountryModelList());
                    }else{
                        initUserSelect(null);
                    }
                }
            }
        });
    }

    public CountryModel getNationById(Long id){
        return userCountryMap.get(id);
    }

    public List<CountryModel> getCountryModelList(int state) {
        if (state == STATE_EUROPE){
            sortChar(countryHashMap.get(STATE_EUROPE_E));
            return countryHashMap.get(STATE_EUROPE_E);
        }else if (state == STATE_ASIA){
            sortChar(countryHashMap.get(STATE_ASIA_E));
            return countryHashMap.get(STATE_ASIA_E);
        }else if (state == STATE_AFRICA){
            sortChar(countryHashMap.get(STATE_AFRICA_E));
            return countryHashMap.get(STATE_AFRICA_E);
        }else if (state == STATE_AMERICA){
            sortChar(countryHashMap.get(STATE_AMERICA_E));
            return countryHashMap.get(STATE_AMERICA_E);
        }else if (state == STATE_OCEANIA){
            sortChar(countryHashMap.get(STATE_OCEANIA_E));
            return countryHashMap.get(STATE_OCEANIA_E);
        }
//        else if (state == STATE_ALL){
//            sortChar(countryModelList);
//            return countryModelList;
//        }
        else {
            return new ArrayList<>();
        }
    }

//    private void adjustData(String state){

//        countryModelList.clear();
//        for (String state1 : countryHashMap.keySet()){
//            for (CountryModel model : countryHashMap.get(state)){
//                if (countryMap.containsKey(model.getId())){
//                    countryModelList.remove(countryMap.get(model.getId()));
//                    countryMap.remove(model.getId());
//                }

//                countryMap.put(model.getId(),model);
//                countryModelList.add(model);

//            }

//        }
//        sortCountry(countryModelList);
//        initUserSelect();
//    }

    public List<Long> getUserSelectIds(){
        List<Long> ids = new ArrayList<>();
        if (userCountry == null){
            return ids;
        }

        for (CountryModel model : userCountry){
            ids.add(model.getId());
        }

        return ids;
    }

    public void setUserSelectCountry(List<CountryModel> modelList){
        if (modelList == null){
            return;
        }

        userCountry.clear();
        userCountryMap.clear();
        String str = "";
        for (CountryModel model : modelList){
            userCountry.add(model);
            str += ":"+model.getId();
            userCountryMap.put(model.getId(),model);
        }

        saveUserSelectToFile(str);

        if (MyClient.getMyClient().getMyAlarmManager().isLoadSuccess()){
            MyClient.getMyClient().getMyAlarmManager().checkAlarmWithUserCountry();
        }else {
            MyClient.getMyClient().getMyAlarmManager().registerLoadAlarmListener(new OnLoadAlarmFinishListener() {
                @Override
                public void loadDataFinish() {
                    MyClient.getMyClient().getMyAlarmManager().checkAlarmWithUserCountry();
                }
            });
        }
    }

    public void requestCountryByIds(String ids, final OnGetCountryByIdsListener listener) {

        final IRequest request = (IRequest) MyClient.getMyClient().getService(MyClient.SERVICE_HTTP_REQUEST);
        if (request == null) {
            return;
        }

        HashMap<String,String> map = new HashMap<>();

        map.put("ids",ids);

        request.sendRequestForPostWithJson(URL_GET_COUNTRY_BY_IDS, map, new IRequestCallback() {
            @Override
            public void onResponseSuccess(JSONObject jsonObject) {
                if (listener == null) {
                    return;
                }

                if (jsonObject == null) {
                    listener.onGetCountryFinish(null);
                    return;
                }

                CountryModelList model = new CountryModelList();
                model.decode(jsonObject);

                if (model.getCode() == 0){
                    listener.onGetCountryFinish(model.getCountryModelList());
                }else{
                    listener.onGetCountryFinish(null);
                }



            }

            @Override
            public void onResponseSuccess(String str) {

            }

            @Override
            public void onResponseError(int code) {
                if (listener != null) {
                    listener.onGetCountryFinish(null);
                }
            }
        });


    }

    public void requestCountryByState(Context activityContext, final String state, final OnGetCountryByStateListener listener) {
        if (state == null){
            return;
        }

        if (countryHashMap.containsKey(state)){
            if (listener!=null){
                listener.onGetCountryFinish(state);
            }
            return;
        }

        if (activityContext != null){
            ((BaseActivity)activityContext).showProgress();
        }

        final IRequest request = (IRequest) MyClient.getMyClient().getService(MyClient.SERVICE_HTTP_REQUEST);
        if (request == null) {
            return;
        }

        HashMap<String,String> map = new HashMap<>();

        map.put("state",state);

        request.sendRequestForPostWithJson(URL_GET_COUNTRY_BY_STATE, map, new IRequestCallback() {
            @Override
            public void onResponseSuccess(JSONObject jsonObject) {
                if (listener == null) {
                    return;
                }

                if (jsonObject == null) {
                    listener.onGetCountryFinish(state);
                    return;
                }

                CountryModelList model = new CountryModelList();
                model.decode(jsonObject);

                if (model.getCode() == 0){
                    countryHashMap.put(state,model.getCountryModelList());
                    sortCountry(countryHashMap.get(state));
//                    adjustData(state);
                }

                listener.onGetCountryFinish(state);

            }

            @Override
            public void onResponseSuccess(String str) {

            }

            @Override
            public void onResponseError(int code) {
                if (listener != null) {
                    listener.onGetCountryFinish(state);
                }
            }
        });


    }

    public List<CountryModel> getUserCountry() {
        return userCountry;
    }

    public void setUserCountry(List<CountryModel> userCountry) {
        this.userCountry = userCountry;
    }

    public int getUserSelectIndex(CountryModel model){
        return userCountry.indexOf(model);
    }

    public boolean isUserSelected(CountryModel model){
        for (CountryModel mo : userCountry){
            if (mo.getId().equals(model.getId())){
                return true;
            }
        }
        return false;
    }

    public boolean isCountryDataChange() {
        return isCountryDataChange;
    }

    public void setCountryDataChange(boolean countryDataChange) {
        isCountryDataChange = countryDataChange;
    }

    public boolean isLoadUserInfoFinish(){
        return  isLoadUserInfoFinish;
    }

}
