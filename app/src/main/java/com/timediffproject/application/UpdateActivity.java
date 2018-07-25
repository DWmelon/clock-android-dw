package com.timediffproject.application;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.Toast;

import com.timediffproject.R;
import com.timediffproject.module.home.AppUpdateCheckModel;
import com.timediffproject.module.misc.OnUpdateCheckListener;
import com.timediffproject.origin.MainApplication;
import com.timediffproject.oss.callback.OnDownloadListener;
import com.timediffproject.oss.callback.OnGetUploadConfigListener;
import com.timediffproject.storage.StorageManager;

import java.io.File;

/**
 * Created by melon on 2018/7/2.
 */

public class UpdateActivity extends BaseActivity implements OnGetUploadConfigListener,OnUpdateCheckListener,OnDownloadListener {

    private AppUpdateCheckModel updateModel;
    protected boolean isShow = false;

    protected void setShow(boolean isShow){
        this.isShow = isShow;
    }

    public void checkUpdate(){
        MyClient.getMyClient().getOssManager().getUploadConfig(this);
    }

    public void handleVersion(final AppUpdateCheckModel model){
        hideProgress();
        updateModel = model;
        int code = Integer.parseInt(updateModel.getvCode());
        String pkName = getPackageName();
        int vCode = GlobalPreferenceManager.getVersionCode(this);
        try {
            int versionCode = getPackageManager().getPackageInfo(
                    pkName, 0).versionCode;

            if (code > versionCode){
                GlobalPreferenceManager.setUpdatePointShow(this,true);
            }

            GlobalPreferenceManager.saveVersionCode(this,code);
            GlobalPreferenceManager.saveVersionName(this,updateModel.getvName());
            GlobalPreferenceManager.saveVersionInfo(this,updateModel.getvInfo());

            //这个版本已经检测过了
            if (code == vCode && !isShow){
                return;
            }

            if (code > versionCode){

                GlobalPreferenceManager.setUpdatePointShow(this,true);
                showCommonAlert(R.string.update_title, updateModel.getvInfo(), R.string.update_ok, R.string.cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        handleUpdateApk();
                    }
                },null);
//                兼容老版本
                if (code<=4){
                    GlobalPreferenceManager.setRefreshAlarm(this,true);
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void handleUpdateApk(){
        int result = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,R.string.update_permission_fail,Toast.LENGTH_LONG).show();
            return;
        }
        String filePath = StorageManager.getNewVerisonPath(String.valueOf(updateModel.getvCode()));
        File file = new File(filePath);
        if (file.exists()){
            installApk(file);
        }else{
            downloadApk();
        }
    }

    private void installApk(File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(MainApplication.getContext(),"com.easyads.fileprovider.update",apkFile);
            intent.setDataAndType(contentUri,"application/vnd.android.package-archive");
        }else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }

        startActivity(intent);

    }

    private void downloadApk(){
        String filePath = StorageManager.getNewVerisonPath(String.valueOf(updateModel.getvCode()));
        String shortPath = MyClient.getMyClient().getOssManager().getOssShortPath(updateModel.getvPath());
        MyClient.getMyClient().getOssManager().downloadSourceFromOSS(filePath,shortPath,this);
    }

    @Override
    public void onDownloadFinish(boolean isSuccess,String msg) {
        if (isSuccess){
            String filePath = StorageManager.getNewVerisonPath(String.valueOf(updateModel.getvCode()));
            File file = new File(filePath);
            if (file.exists()){
                installApk(file);
            }
        }else{
            hideProgress();
        }

    }

    @Override
    public void onGetConfigFinish(boolean isSuccess) {
        if (isSuccess){
            MyClient.getMyClient().getMiscManager().updateCheck(this);
        }else {
            hideProgress();
        }
    }

    @Override
    public void onUpdateCheck(AppUpdateCheckModel model) {
        if (model == null || model.getvCode().isEmpty()){
            hideProgress();
            return;
        }

        handleVersion(model);

    }

}
