package com.jr.practice.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.TextUtils;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.jr.practice.R;
import com.jr.practice.utils.L;

/**
 * Created by Administrator on 2016-08-22.
 */

public class MyAppWidgetProvider extends AppWidgetProvider {
    public static final String CLICK_ACTION = "CLICK";

    public MyAppWidgetProvider() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        L.i("onReceive : action = " + intent.getAction());
        //这里判断是自己的action，做自己的事情，比如小部件被单击了要做什么，这里是做一个动画效果
        if (TextUtils.equals(intent.getAction(), CLICK_ACTION)) {
            Toast.makeText(context, "clicked it", Toast.LENGTH_SHORT).show();

            new Thread(() -> {
                Bitmap srcBitmpa = BitmapFactory.decodeResource(context.getResources(), R.mipmap.shibainu2);
                AppWidgetManager appWidgetProvider = AppWidgetManager.getInstance(context);
                for (int i = 0; i < 37; i++) {
                    float degree = (i * 10) % 360;
                    RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
                    remoteViews.setImageViewBitmap(R.id.imageView1, rotateBitmap(context, srcBitmpa, degree));
                    appWidgetProvider.updateAppWidget(new ComponentName(context, MyAppWidgetProvider.class), remoteViews);
                }
            }).start();
        }
    }

    /**
     * @param context
     * @param appWidgetManager
     * @param appWidgetIds     每次小部件更新都会调用一次
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        L.i("onUpdate");

        final int counter = appWidgetIds.length;
        L.i("counter:" + context);
        for (int i = 0; i < counter; i++) {
            int appWidgetId = appWidgetIds[i];
            onWidgetUpdate(context, appWidgetManager, appWidgetId);
        }
    }

    private void onWidgetUpdate(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        L.i("appWidget = " + appWidgetId);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

        //“桌面小部件”单击事件发送的Intent广播
        Intent intentClick = new Intent();
        intentClick.setAction(CLICK_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentClick, 0);
        remoteViews.setOnClickPendingIntent(R.id.imageView1, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    private Bitmap rotateBitmap(Context context, Bitmap srcBitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setRotate(degree);
        Bitmap tmpBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);
        return tmpBitmap;
    }
}
