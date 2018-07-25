package com.timediffproject.application;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.timediffproject.R;
import com.timediffproject.storage.StorageManager;
import com.timediffproject.util.DeviceInfoManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by melon on 2018/5/10.
 */

public class PermissionActivity extends BaseActivity {

    private List<String> permissionList = new ArrayList<>();
    protected boolean isPermissionCheck = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermission();
    }

    private void requestPermission(){
        int result1 = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int result2 = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result3 = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        permissionList.clear();
        if (result1 != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (result2 != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (result3 != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (!permissionList.isEmpty()){
            String[] array = new String[permissionList.size()];
            permissionList.toArray(array);
            ActivityCompat.requestPermissions(this, array ,1);
        }else{
            isPermissionCheck = true;
            handleIntentNext();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1){
            for (int i = 0; i < grantResults.length; i++) {
                if (permissions[i].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
//                        Toast.makeText(this,"获取不到读写文件权限，无法读写文件", Toast.LENGTH_LONG).show();
                    }else{
                        MyClient.getMyClient().getStorageManager().init();
                    }
                }
                if (permissions[i].equals(Manifest.permission.READ_PHONE_STATE)){
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED){
                        DeviceInfoManager.getInstance().init();
                    }
                }
            }
//            boolean isAllAllow = true;
//            for (Integer result : grantResults){
//                if (result != PackageManager.PERMISSION_GRANTED){
//                    isAllAllow = false;
//                    break;
//                }
//            }
//            if (isAllAllow){
//                isPermissionAllow = true;
//                handleIntentNext();
//            }else{
//                Toast.makeText(this, R.string.permission_lack, Toast.LENGTH_LONG).show();
//            }
        }
        isPermissionCheck = true;
        handleIntentNext();
    }

    protected void handleIntentNext(){}

}
