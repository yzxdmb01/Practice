package com.jr.practice.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2016-10-24.
 */

public class PercentView extends TextView {
    private Paint bgPaint;
    private Paint strokePaint;
    private int strokeWidth = 2;
    private int space = 5;
    private float percent = 0.74f;

    public PercentView(Context context) {
        super(context);
        init();
    }

    public PercentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PercentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setBackgroundColor(Color.TRANSPARENT);      //屏蔽掉设置的背景
        String text = getText().toString();
        text = text.replace("%", "");
        float textPercent = Float.parseFloat(text) / 100f;
        percent = textPercent;
        setPercent(98);
        setText(percent * 100 + "%");
        bgPaint = new Paint();
        bgPaint.setColor(Color.parseColor("#00b31b"));
        bgPaint.setStyle(Paint.Style.FILL);

        strokePaint = new Paint();
        strokePaint.setColor(Color.parseColor("#e1e1e1"));
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setAntiAlias(true);
        strokePaint.setFlags(Paint.ANTI_ALIAS_FLAG);    //消除锯齿
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawStroke(canvas, strokePaint);
        drawBg(canvas, bgPaint);
        super.onDraw(canvas);
    }

    private void drawBg(Canvas canvas, Paint p) {
        int l = 0 + space;
        int t = 0 + space;
        int r = (int) (getWidth() * percent - space);
        int b = getHeight() - space;
        RectF rectF = new RectF(l, t, r, b);
        canvas.drawRoundRect(rectF, (b - space) / 6, (b - space) / 6, p);
    }

    private void drawStroke(Canvas canvas, Paint p) {
        RectF oval = new RectF(0, 0, getWidth(), getHeight());
        canvas.drawRoundRect(oval, getHeight() / 6, getHeight() / 6, p);
    }

    public void setPercent(float percent) {
        if (percent > 100) percent = 1;
        if (percent < 0) percent = 0;
        if (percent > 1) percent /= 100;
        this.percent = percent;
    }
}
