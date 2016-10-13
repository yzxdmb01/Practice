package com.jr.practice.utils;

import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

import com.jr.practice.JrApplication;

/**
 * Created by Administrator on 2016-08-10.
 */

public class ScreenUtils {
    private static WindowManager windowManager = (WindowManager) JrApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
    private static Point point = new Point();

    //获取屏幕宽度
    public static int getScreenWidth() {
        windowManager.getDefaultDisplay().getSize(point);
        return point.x;
    }

    //获取屏幕高度
    public static int getScreenHeight() {
        windowManager.getDefaultDisplay().getSize(point);
        return point.y;
    }

    //px转dp
    public static float px2dp(float pxValue) {
        final float scale = JrApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    //dp转px
    public static float dp2px(float dpValue) {
        final float scale = JrApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
