package com.jr.practice.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.jr.practice.MainActivity;
import com.jr.practice.R;

public class MyService extends Service {
    private DownloadBinder mBinder = new DownloadBinder();
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Intent notificationIntent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);
        Notification notification = new Notification.Builder(this).setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("tickTitle")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setContentTitle("contentTitle")
                .setContentInfo("contentInfo")
                .setContentText("contentText")
                .build();
        startForeground(1,notification);
        Log.i("MyService", "onCreate");
        Toast.makeText(this,"启动服务",Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("MyService", "onStartCommand");
        MainActivity.SERVICE_IS_RUNNING = true;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("MyService", "onDestroy");
        Toast.makeText(this,"停止服务",Toast.LENGTH_SHORT).show();
        MainActivity.SERVICE_IS_RUNNING = false;
    }

   public class DownloadBinder extends Binder {
        public void startDownload(){
            Log.i("MyService","startDownload executed");
        }
        public int getProgress(){
            Log.i("MyService","getProgress executed");
            return 0;
        }
    }
}
