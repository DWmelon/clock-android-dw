package com.timediffproject.origin;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.timediffproject.R;
import com.timediffproject.application.DebugConfig;
import com.timediffproject.application.GlobalPreferenceManager;
import com.timediffproject.application.MyClient;
import com.timediffproject.util.CommonUtil;
import com.timediffproject.util.DeviceInfoManager;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.common.UmLog;
import com.umeng.message.entity.UMessage;

public class MainApplication extends Application{

  private static final String TAG = MainApplication.class.getName();
  public static final String UPDATE_STATUS_ACTION = "com.umeng.message.example.action.UPDATE_STATUS";

  private static Context context;

  @Override
  public void onCreate() {
    super.onCreate();

    context = this.getApplicationContext();
    MyClient.getMyClient().init(this);

    Fresco.initialize(this);

    umengEvent();
    MobclickAgent.openActivityDurationTrack(false);
    CommonUtil.resetAppLanguage(this);
    reinitChannel();
  }

  private void umengEvent(){
    /**
     * 初始化common库
     * 参数1:上下文，不能为空
     * 参数2:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
     * 参数3:Push推送业务的secret
     */
    UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "8fd1559878e9c978f37121d89abfb0b0");
  }

  /**
   * 重新赋值渠道标识，使用首次安装时的渠道
   */
  private void reinitChannel() {

    String channel = GlobalPreferenceManager.getString(context,GlobalPreferenceManager.KEY_APP_CHANNEL);
    if (TextUtils.isEmpty(channel)) {
      //说明安装后首次启动
      GlobalPreferenceManager.setString(context,GlobalPreferenceManager.KEY_APP_CHANNEL, DeviceInfoManager.CHANNEL);
    } else {
      DeviceInfoManager.CHANNEL = channel;
    }

  }

  public static Context getContext(){
    return context;
  }

}
