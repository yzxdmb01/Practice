package com.jr.practice.view;

import android.content.Context;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.jr.practice.utils.L;
import com.jr.practice.utils.ScreenUtils;

/**
 * Created by Administrator on 2016-08-08.
 */

public class TextView2 extends TextView {
    private String TAG = "TextView";
    TextPaint textPaint = null;
    StaticLayout staticLayout = null;
    Paint paint = null;
    int width = 80;
    int height = 0;
    String txt = "";

    public TextView2(Context context) {
        super(context);
    }

    public TextView2(Context context, AttributeSet attrs) {
        super(context, attrs);

        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(getTextSize());
        txt = "";
        staticLayout = new StaticLayout(txt, textPaint, ScreenUtils.getScreenWidth(), Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
        height = staticLayout.getHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
