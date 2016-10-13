package com.jr.practice.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.jr.practice.utils.L;

/**
 * 自己滚动的view
 * Created by Administrator on 2016-08-05.
 */

public class ClickSlideView extends LinearLayout {
    private Scroller mScroller;


    public ClickSlideView(Context context) {
        super(context);
    }

    public ClickSlideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
        postDelayed(() -> startSlide(), 1500);
    }

    //点击滚动
    public void startSlide() {
        if (getChildCount() <= 0) {
            return;
        }
        mScroller.startScroll(0, 0, 0, getMeasuredHeight(), 1500);
        invalidate();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        //把高度设置为只显示一个view
        int hightSize = 0;
        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();
                int marginTop = layoutParams.topMargin;
                int marginBottom = layoutParams.bottomMargin;
                hightSize = Math.max(childView.getMeasuredHeight() + marginTop + marginBottom, hightSize);
            }
        }
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), hightSize);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        } else { //每滚完一次就把第一个放到最后一个
            if (getScrollY() > 0) {
                View v = getChildAt(0);
                removeView(v);
                addView(v);
                startSlide();
            }
        }
    }

    public void stopAnim() {
        removeAllViews();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnim();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
    }
}
