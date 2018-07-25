package com.timediffproject.oss;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.DeleteObjectRequest;
import com.alibaba.sdk.android.oss.model.DeleteObjectResult;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.timediffproject.application.GlobalPreferenceManager;
import com.timediffproject.application.MyClient;
import com.timediffproject.constants.Constant;
import com.timediffproject.network.IRequest;
import com.timediffproject.network.IRequestCallback;
import com.timediffproject.oss.callback.OnDownloadListener;
import com.timediffproject.oss.callback.OnGetUploadConfigListener;
import com.timediffproject.util.FileUtil;


import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by melon on 2017/8/4.
 */

public class OSSManager {

    private Context context;

    private OSS oss;
    public static final String endPoint = "http://oss-cn-shenzhen.aliyuncs.com";
    public static final String bucketName = "time-clock";
    private OSSConfigModel ossConfigModel;

    public static final String completePathHttps = "https://time-clock.oss-cn-shenzhen.aliyuncs.com/";
    public static final String completePathHttp = "http://time-clock.oss-cn-shenzhen.aliyuncs.com/";

//    public static final String URL_UPLOAD_CONFIG = "http://119.23.222.106/simplenote" + "/sync/config";
    public static final String URL_UPLOAD_CONFIG = Constant.MAIN_URL +  "/sync/config";

    private OnGetUploadConfigListener listener;


    public void setListener(OnGetUploadConfigListener listener){
        this.listener = listener;
    }

    public OSSManager(Context context){
        this.context = context;
        initOOS();
    }

    private void initOOS(){
        // 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考访问控制章节
        String AccessKeyId = GlobalPreferenceManager.getString(context,GlobalPreferenceManager.KEY_ACCESS_KEY_ID);
        String SecretKeyId = GlobalPreferenceManager.getString(context,GlobalPreferenceManager.KEY_SECRET_KEY_ID);
        String SecurityToken = GlobalPreferenceManager.getString(context,GlobalPreferenceManager.KEY_SECURITY_TOKEN);

        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(AccessKeyId,SecretKeyId, SecurityToken);

        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次

        oss = new OSSClient(context, endPoint, credentialProvider, conf);
        if (listener != null){
            listener.onGetConfigFinish(true);
        }
    }

    private void updateOOS(){
        GlobalPreferenceManager.setString(context,GlobalPreferenceManager.KEY_ACCESS_KEY_ID,ossConfigModel.getAccessKeyId());
        GlobalPreferenceManager.setString(context,GlobalPreferenceManager.KEY_SECRET_KEY_ID,ossConfigModel.getAccessKeySecret());
        GlobalPreferenceManager.setString(context,GlobalPreferenceManager.KEY_SECURITY_TOKEN,ossConfigModel.getSecurityToken());
        oss.updateCredentialProvider(new OSSStsTokenCredentialProvider(ossConfigModel.getAccessKeyId(), ossConfigModel.getAccessKeySecret(), ossConfigModel.getSecurityToken()));
    }

    public boolean checkKeyExpire(){
        return ossConfigModel == null || oss == null;
    }

    public String getOssShortPath(String sourcePath){
        return sourcePath.replace(OSSManager.completePathHttps,"").replace(OSSManager.completePathHttp,"");
    }

    /**
     * 获取OSS的密钥
     * @param listener
     */
    public void getUploadConfig(final OnGetUploadConfigListener listener){

        if (!checkKeyExpire()){
            if (listener != null){
                listener.onGetConfigFinish(true);
            }
            return;
        }

        HashMap<String,String> map = new HashMap<String, String>();
        map.put("token","-i7NYFdeX5wpgJ7S0Vd1sb2jDzdZjaxOXDE-TF5nGV7wRUxslRV18_2avUqDsDj8aW7-JBAbFwhN9ze_vCDj6a==");

        final IRequest request = (IRequest) MyClient.getMyClient().getService(MyClient.SERVICE_HTTP_REQUEST);
        request.sendRequestForPostWithJson(URL_UPLOAD_CONFIG, map, new IRequestCallback() {
            @Override
            public void onResponseSuccess(JSONObject jsonObject) {

                if (jsonObject == null) {
                    if (listener != null){
                        listener.onGetConfigFinish(false);
                    }
                    return;
                }


                ossConfigModel = new OSSConfigModel();
                ossConfigModel.decode(jsonObject);


                if (ossConfigModel.getCode() == 0){
                    updateOOS();
                    if (listener != null){
                        listener.onGetConfigFinish(true);
                    }
                }else{
                    if (listener != null){
                        listener.onGetConfigFinish(false);
                    }
                }

            }

            @Override
            public void onResponseSuccess(String str) {

            }

            @Override
            public void onResponseError(int code) {
                if (listener != null) {
                    listener.onGetConfigFinish(false);
                }
            }
        });
    }

    public void downloadSourceFromOSS(final String localPath, String ossPath, final OnDownloadListener listener){

        if (checkKeyExpire()){
            return;
        }

        // 构造下载文件请求
        GetObjectRequest get = new GetObjectRequest(bucketName, ossPath);
        //设置下载进度回调
        get.setProgressListener(new OSSProgressCallback<GetObjectRequest>() {
            @Override
            public void onProgress(GetObjectRequest request, long currentSize, long totalSize) {
//                OSSLog.logDebug("getobj_progress: " + currentSize+"  total_size: " + totalSize, false);
//
//                if (currentSize > countSize){
//                    Message message = mHandler.obtainMessage(100);
//                    Bundle bundle = message.getData();
//                    bundle.putLong("size",currentSize / 1024 /1024);
//                    bundle.putLong("total",totalSize / 1024 /1024);
//                    message.setData(bundle);
//                    mHandler.sendMessage(message);
//                    countSize += 1024 * 1024 * 10;
//                }

            }
        });


        try {
            // 同步执行下载请求，返回结果
            GetObjectResult getResult = oss.getObject(get);
            Log.d("Content-Length", "" + getResult.getContentLength());

            // 获取文件输入流
            final InputStream inputStream = getResult.getObjectContent();

            Observable.just(inputStream)
                    .map(new Function<InputStream, Boolean>() {
                        @Override
                        public Boolean apply(InputStream inputStream) throws Exception {
                            Log.d("thread-1", Thread.currentThread().getName());
                            return FileUtil.writeStreamToFile(inputStream,localPath);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean result) throws Exception {
                            Log.d("thread-2", Thread.currentThread().getName());
                            if (result){
                                Log.d("oss", "save file finish 1");
                                listener.onDownloadFinish(true,localPath);
                            }else{
                                Log.d("oss", "save file finish 2");
                                listener.onDownloadFinish(false,localPath);
                            }
                        }
                    });
            return;
        } catch (ClientException e) {
            // 本地异常如网络异常等
            e.printStackTrace();

        } catch (ServiceException e) {
            // 服务异常
            Log.e("RequestId", e.getRequestId());
            Log.e("ErrorCode", e.getErrorCode());
            Log.e("HostId", e.getHostId());
            Log.e("RawMessage", e.getRawMessage());
            if (e.getErrorCode().equals("InvalidAccessKeyId")){
                getUploadConfig(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        listener.onDownloadFinish(false,localPath);


    }



    public OSS getOss() {
        return oss;
    }

    public void setOss(OSS oss) {
        this.oss = oss;
    }
}
