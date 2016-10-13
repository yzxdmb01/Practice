package com.jr.practice;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.jr.practice.activity.AnimationActivity;
import com.jr.practice.activity.PhotoWallActivity;
import com.jr.practice.activity.KeyboardActivity;
import com.jr.practice.activity.ListViewActivity;
import com.jr.practice.activity.LocalActivity;
import com.jr.practice.activity.NotificationActivity;
import com.jr.practice.activity.CustomViewActivity;
import com.jr.practice.activity.DrawableActivity;
import com.jr.practice.service.MyService;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static boolean SERVICE_IS_RUNNING;
    private MyService.DownloadBinder downloadBinder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (MyService.DownloadBinder) service;
            downloadBinder.startDownload();
            downloadBinder.getProgress();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView;
        init();
    }

    private void init() {
        findViewById(R.id.btn_notifi).setOnClickListener(this);
        findViewById(R.id.btn_sms).setOnClickListener(this);
        findViewById(R.id.btn_keyboard).setOnClickListener(this);
        findViewById(R.id.btn_anmi).setOnClickListener(this);
        findViewById(R.id.btn_click).setOnClickListener(this);
        findViewById(R.id.btn_service).setOnClickListener(this);
        findViewById(R.id.btn_listview).setOnClickListener(this);
        findViewById(R.id.btn_location).setOnClickListener(this);
        findViewById(R.id.btn_tablayout).setOnClickListener(this);
    }


    public static boolean checkPermission(Context context, String permission) {
        boolean result;
        result = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
        return result;
    }

    public String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 0);
                return "获取权限";
            }
            String mac = null;
            FileReader fstream = null;
            try {
                fstream = new FileReader("/sys/class/net/wlan0/address");
            } catch (FileNotFoundException e) {
                fstream = new FileReader("/sys/class/net/eth0/address");
            }
            BufferedReader in = null;
            if (fstream != null) {
                try {
                    in = new BufferedReader(fstream, 1024);
                    mac = in.readLine();
                } catch (IOException e) {
                } finally {
                    if (fstream != null) {
                        try {
                            fstream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        Intent mIntent = null;
        switch (v.getId()) {
            case R.id.btn_notifi:
                mIntent = new Intent(this, NotificationActivity.class);
                break;
            case R.id.btn_sms:
                mIntent = new Intent(this, CustomViewActivity.class);
                break;
            case R.id.btn_keyboard:
                mIntent = new Intent(this, KeyboardActivity.class);
                break;
            case R.id.btn_anmi:
                mIntent = new Intent(this, AnimationActivity.class);
                break;
            case R.id.btn_click:
                mIntent = new Intent(this, PhotoWallActivity.class);
                break;
            case R.id.btn_service:
                Intent serviceIntent = new Intent(this, MyService.class);
                if (SERVICE_IS_RUNNING) {
                    this.stopService(serviceIntent);
                } else {
                    this.startService(serviceIntent);
                }
                break;
            case R.id.btn_listview:
                mIntent = new Intent(this, ListViewActivity.class);
                break;
            case R.id.btn_location:
                mIntent = new Intent(this, LocalActivity.class);
                break;
            case R.id.btn_tablayout:
                mIntent = new Intent(this, DrawableActivity.class);
                break;
        }
        if (mIntent != null){
            startActivity(mIntent);
        }

    }
}
