package com.jr.practice.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * Created by Administrator on 2016-08-08.
 */

public class Button2 extends Button{
    private String TAG = "BUTTON";
    public Button2(Context context) {
        super(context);
    }

    public Button2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.i(TAG,"dispatchTouchEvent:"+System.currentTimeMillis());
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG,"onTouchEvetn:"+System.currentTimeMillis());
        return super.onTouchEvent(event);
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);
    }
}
