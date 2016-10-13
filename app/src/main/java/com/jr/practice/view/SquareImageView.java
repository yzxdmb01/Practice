package com.jr.practice.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 实现一个方形的ImageView
 * Created by Administrator on 2016-08-18.
 */

public class SquareImageView extends ImageView {
    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
