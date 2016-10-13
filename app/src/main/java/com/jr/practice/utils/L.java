package com.jr.practice.utils;

import android.util.Log;

/**
 * Log工具类
 * Created by Administrator on 2016-08-15.
 */

public class L {
    private static final int VERBOSE = 1;
    private static final int DEBUG = 2;
    private static final int INFO = 3;
    private static final int WARN = 4;
    private static final int ERROR = 5;
    private static final int ASSENT = 6;

    private static int LEVEL = INFO;


    public static void i(String msg) {
        StackTraceElement[] stes = Thread.currentThread().getStackTrace();
        int i = 0;
        for (StackTraceElement ste : stes) {
            i += 1;
            if (ste.getClassName().equals("com.jr.practice.utils.L")) break;
        }
        if (LEVEL >= INFO) {
            StackTraceElement ste = Thread.currentThread().getStackTrace()[i];
            i(ste.getClassName().replace("com.jr.practice.", "custom.") + "/L:" + ste.getLineNumber(), msg);
        }
    }

    public static void d(String msg) {
        if (LEVEL >= DEBUG) d(Thread.currentThread().getStackTrace()[3].getClassName(), msg);
    }

    public static void d(String TAG, String msg) {
        Log.d(TAG, msg);
    }

    public static void i(String tag, String msg) {
        if (LEVEL >= INFO) Log.i(tag, msg);
    }

    public static void e(String msg) {
        if (LEVEL >= ERROR) e(Thread.currentThread().getStackTrace()[3].getClassName(), msg);
    }

    public static void e(String tag, String msg) {
        if (LEVEL >= ERROR) Log.i(tag, msg);
    }
}
