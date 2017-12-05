package com.timediffproject.origin;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.timediffproject.R;
import com.timediffproject.application.DebugConfig;
import com.timediffproject.application.GlobalPreferenceManager;
import com.timediffproject.application.MyClient;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.common.UmLog;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

public class MainApplication extends Application{

  private static final String TAG = MainApplication.class.getName();
  public static final String UPDATE_STATUS_ACTION = "com.umeng.message.example.action.UPDATE_STATUS";

  private static Context context;

  @Override
  public void onCreate() {
    super.onCreate();

    MyClient.getMyClient().init(this);
    context = this.getApplicationContext();

    Fresco.initialize(this);
    UMShareAPI.get(this);
    PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
    PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
    PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad", "http://sns.whalecloud.com");
    umengEvent();
//    umengPush();
    MobclickAgent.openActivityDurationTrack(false);



  }

  private void umengEvent(){
    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
      AnalyticsConfig.sEncrypt=true;//6.0.0版本以前
    }else{
      MobclickAgent.enableEncrypt(true);//6.0.0版本及以后
    }

    MobclickAgent.setCatchUncaughtExceptions(true);
  }

  private void umengPush(){
    PushAgent mPushAgent = PushAgent.getInstance(this);
    mPushAgent.setDebugMode(true);

    //sdk开启通知声音
//    mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
    // sdk关闭通知声音
		mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
    // 通知声音由服务端控制
//		mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SERVER);

//		mPushAgent.setNotificationPlayLights(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
//		mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);


    UmengMessageHandler messageHandler = new UmengMessageHandler() {
      /**
       * 自定义消息的回调方法
       * */
      @Override
      public void dealWithCustomMessage(final Context context, final UMessage msg) {
        new Handler().post(new Runnable() {

          @Override
          public void run() {
            // TODO Auto-generated method stub
            // 对自定义消息的处理方式，点击或者忽略
            boolean isClickOrDismissed = true;
            if (isClickOrDismissed) {
              //自定义消息的点击统计
              UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
            } else {
              //自定义消息的忽略统计
              UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
            }
            Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
          }
        });
      }

      /**
       * 自定义通知栏样式的回调方法
       * */
      @Override
      public Notification getNotification(Context context, UMessage msg) {
        switch (msg.builder_id) {
          case 1:
            Notification.Builder builder = new Notification.Builder(context);
            RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
            myNotificationView.setTextViewText(R.id.notification_title, msg.title);
            myNotificationView.setTextViewText(R.id.notification_text, msg.text);
            myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
            myNotificationView.setImageViewResource(R.id.notification_small_icon, getSmallIconId(context, msg));
            builder.setContent(myNotificationView)
                    .setSmallIcon(getSmallIconId(context, msg))
                    .setTicker(msg.ticker)
                    .setAutoCancel(true);

            return builder.getNotification();
          default:
            //默认为0，若填写的builder_id并不存在，也使用默认。
            return super.getNotification(context, msg);
        }
      }
    };
    mPushAgent.setMessageHandler(messageHandler);

    /**
     * 自定义行为的回调处理
     * UmengNotificationClickHandler是在BroadcastReceiver中被调用，故
     * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
     * */
    UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
      @Override
      public void dealWithCustomAction(Context context, UMessage msg) {
        Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
      }
    };
    //使用自定义的NotificationHandler，来结合友盟统计处理消息通知
    //参考http://bbs.umeng.com/thread-11112-1-1.html
    //CustomNotificationHandler notificationClickHandler = new CustomNotificationHandler();
    mPushAgent.setNotificationClickHandler(notificationClickHandler);


    //注册推送服务 每次调用register都会回调该接口
    mPushAgent.register(new IUmengRegisterCallback() {
      @Override
      public void onSuccess(String deviceToken) {
        UmLog.i(TAG, "device token: " + deviceToken);
        sendBroadcast(new Intent(UPDATE_STATUS_ACTION));
      }

      @Override
      public void onFailure(String s, String s1) {
        UmLog.i(TAG, "register failed: " + s + " " +s1);
        sendBroadcast(new Intent(UPDATE_STATUS_ACTION));
      }
    });

    //此处是完全自定义处理设置
//        mPushAgent.setPushIntentServiceClass(MyPushIntentService.class);

    //关闭日志输入 false
    mPushAgent.setDebugMode(DebugConfig.isDebug);

  }

  public static Context getContext(){
    return context;
  }

}
