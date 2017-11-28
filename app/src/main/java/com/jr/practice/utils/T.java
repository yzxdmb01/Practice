package com.jr.practice.utils;

import android.widget.Toast;

import com.jr.practice.JrApplication;

/**
 * Created by Administrator on 2016-08-31.
 */

public class T {
    private static Toast mToast;

    public static void t(String msg) {
        show(msg, Toast.LENGTH_SHORT);
    }

    public static void tLong(String msg) {
        show(msg, Toast.LENGTH_LONG);
    }


    private static void show(String msg, int duration) {
        if (mToast == null) {
            synchronized (Toast.class) {
                mToast = Toast.makeText(JrApplication.getContext(), msg, duration);
            }
        }
        mToast.setText(msg);
        mToast.setDuration(duration);
        mToast.show();
    }

}
