package com.jr.practice.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.jr.practice.R;
import com.jr.practice.utils.L;

/**
 * 自定义圆形view-Android开发艺术探索
 * Created by Administrator on 2016-08-09.
 */

public class CircleView extends View {
    private int mColor = Color.RED;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);    //抗锯齿
    private int percent = 0;
    private ValueAnimator valueAnimator;

    public CircleView(Context context) {
        super(context);
        init();
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleView);
        mColor = array.getColor(R.styleable.CircleView_m_color, Color.RED);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        startAnimation();
    }

    private void startAnimation() {
        valueAnimator = ValueAnimator.ofFloat(0, 1).setDuration(5500);
        valueAnimator.addUpdateListener(animation -> {
            float fraction = animation.getAnimatedFraction();
            percent = (int) (360 * fraction);
            invalidate();
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator(1f));
        valueAnimator.setStartDelay(500);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.start();
    }

    private void init() {
        mPaint.setColor(mColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();
        int width = getWidth();
        int height = getHeight() - 16;
        int radius = Math.min(width - paddingLeft - paddingRight, height - paddingTop - paddingBottom) / 2;
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(8);
        //画圆
        canvas.drawCircle(width / 2, getHeight() / 2, radius, mPaint);
        mPaint.setTextSize(30);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        //计算文字baseline
//        float baseline = (getHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        float baseline = getHeight() / 2 + fontMetrics.bottom;
        //写字
        canvas.drawText((int) (percent / 3.6) + "%", (float) width / 2, baseline, mPaint);
        //画圆弧
        Paint mPaint2 = new Paint();
        mPaint2.setColor(Color.YELLOW);
        mPaint2.setStyle(Paint.Style.STROKE); //绘制空心圆
        mPaint2.setStrokeWidth(8);
        mPaint2.setAntiAlias(true);
        //设置圆弧所在的圆/椭圆
        RectF rectF = new RectF(width / 2 - radius, 8, width / 2 + radius, getHeight() - 8);
        //rectF:圆弧所在的圆,180开始的角度，percent：圆弧经过的角度,false:是否连接圆心,mPaint2:不BB
        canvas.drawArc(rectF, 180, percent, false, mPaint2);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(200, MeasureSpec.getSize(heightMeasureSpec));
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void stopAnim() {
        if (valueAnimator != null) {
            //停止动画，防止内存泄漏
            valueAnimator.cancel();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        valueAnimator.cancel();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_CANCEL:
                L.i("ACTION_CANCEL");
                break;
        }
        return super.dispatchTouchEvent(event);
    }
}
