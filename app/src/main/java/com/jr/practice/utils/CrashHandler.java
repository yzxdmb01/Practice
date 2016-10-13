package com.jr.practice.utils;

import android.content.Context;
import android.os.Environment;
import android.os.Process;
import android.widget.Toast;

import com.jr.practice.JrApplication;

/**
 * Created by Administrator on 2016-08-18.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    private static final boolean DEBUG = true;

    private static final String PATH = Environment.getExternalStorageDirectory().getPath() + "/CrashTest/log/";
    private static CrashHandler sInstance = new CrashHandler();
    private Thread.UncaughtExceptionHandler mDefaultExceptionHanderl;


    public static CrashHandler getsInstance() {
        return sInstance;
    }

    public void init(Context context) {
        mDefaultExceptionHanderl = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);

    }


    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ex.printStackTrace();
        L.i("崩了:" + ex.toString());
        Toast.makeText(JrApplication.getContext(), "我要崩溃了啊~", Toast.LENGTH_SHORT).show();
        Process.killProcess(Process.myPid());
    }
}
