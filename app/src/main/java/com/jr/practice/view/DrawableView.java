package com.jr.practice.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.jr.practice.header.RentalsSunDrawable;

/**
 * Created by Administrator on 2016-08-25.
 */

public class DrawableView extends View{
    private RentalsSunDrawable mDrawable;
    public DrawableView(Context context) {
        super(context);
        init();
    }

    public DrawableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mDrawable = new RentalsSunDrawable(getContext(),this);
        mDrawable.offsetTopAndBottom(0);
        mDrawable.setPercent(0.1f);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mDrawable.draw(canvas);
    }
}
