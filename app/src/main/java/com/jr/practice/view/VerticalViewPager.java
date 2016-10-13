package com.jr.practice.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2016-08-26.
 */

public class VerticalViewPager extends ViewPager {
    public VerticalViewPager(Context context) {
        super(context);
    }

    private MotionEvent swapTouchEvent(MotionEvent motionEvent) {
        float width = getWidth();
        float height = getHeight();

        float swappedX = (motionEvent.getY() / height) * width;
        float swappedY = (motionEvent.getX() / width) * height;

        motionEvent.setLocation(swappedX, swappedY);
        return motionEvent;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = super.onInterceptTouchEvent(swapTouchEvent(ev));
        swapTouchEvent(ev);

        return intercept;
    }

    public VerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPageTransformer(false, new DefaultTransformer());
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(swapTouchEvent(ev));
    }
}
