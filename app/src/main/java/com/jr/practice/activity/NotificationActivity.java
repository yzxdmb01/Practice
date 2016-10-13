package com.jr.practice.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.jr.practice.MainActivity;
import com.jr.practice.R;

import java.io.File;

/**
 * 使用通知
 * Created by Administrator on 2016-07-06.
 */
public class NotificationActivity extends BaseActivity {
    NotificationManager manager;
    Notification notification;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        init();
    }

    private void init() {
        /*先需要一个NotificationManager实例*/
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        /*创建一个Notification对象,第一个参数是通知是的图标,第二个是内容，第三个是通知显示的时间*/
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        long[] vibrate = {0,1000,1000,1000};
        Uri uri = Uri.fromFile(new File("/system/media/audio/ringtones/Basic_tone.ogg"));
        notification = new Notification.Builder(this).setContentTitle("This is title")
                .setContentText("This is contentText")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setVibrate(vibrate)
                .setContentIntent(pi)
                .build();
        /*可以通过setSound,setVibrate，.ledARGB等设置通知的声音震动前置呼吸灯，嫌麻烦可以之间下面一句话用系统默认的*/
        notification.defaults = Notification.DEFAULT_ALL;
    }
    public void showNotify(View view){
        manager.notify(1,notification);
    }

}
