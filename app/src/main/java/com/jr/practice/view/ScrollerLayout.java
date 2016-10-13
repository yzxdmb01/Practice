package com.jr.practice.view;

import android.content.Context;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 需要的数据：view左右边界，触摸距离，最小滑动距离
 * Created by Administrator on 2016-08-05.
 */

public class ScrollerLayout extends ViewGroup {
    /*用于完成滚动操作的实例*/
    private Scroller mScroller;
    /*判断为拖动的最小移动像素数*/
    private int mTouchSlop;
    /*手指按下时屏幕的坐标*/
    private float mXDown;
    /*手指现在的坐标*/
    private float mXMove;
    /*上次触发ACTION_DOWN时的坐标*/
    private float mXLastMove;
    /*界面可滚动的左边界*/
    private int leftBorder;
    /*界面可滚动的右边界*/
    private int rightBorder;

    float thisScroll = 0; //记录本次滚动的距离

    public ScrollerLayout(Context context) {
        super(context);
    }

    public ScrollerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //创建Scroller实例
        mScroller = new Scroller(context);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        //获取mTouchSlop
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }
    }

    //布局子view
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.i("ScrollerLayout", "onLayout:l t r b:" + l + "," + t + "," + r + "," + b + ",changed:" + changed);
        if (changed) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                //给每一个子view布局
                View childView = getChildAt(i);
                //l t r b
                Log.i("ScrollLayout", "getHeight():" + childView.getMeasuredHeight() + ",getWidth():" + childView.getMeasuredWidth());
                childView.layout(childView.getMeasuredWidth() * i, 0, childView.getMeasuredWidth() * (i + 1), childView.getMeasuredHeight());
            }
            leftBorder = getChildAt(0).getLeft();
            rightBorder = getChildAt(childCount - 1).getRight();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i("ScrollOn", "onInterceptTouchEvent");
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i("ScrollOn", "ACTION_DOWN");
                mXDown = ev.getRawX();
                mXLastMove = mXDown;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("ScrollOn", "ACTION_MOVE");
                mXMove = ev.getRawX();
                float diff = Math.abs(mXMove - mXLastMove);
                //当手指拖动距离大于mTouchSlop时认为是进行滚动，拦截子控件的事件
                if (diff > mTouchSlop) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                mXMove = event.getRawX();
                thisScroll = mXMove - mXDown;
                int scrollX = (int) (mXLastMove - mXMove);
                Log.i("Scroll", "scrollX:" + getScrollX());
                if (getScrollX() + scrollX < leftBorder) {
                    //滚到最左边就不滚了
                    scrollTo(leftBorder, 0);
                    mXLastMove = mXMove;
                    return true;
                } else if (getScrollX() + getWidth() + scrollX >= rightBorder) {
                    //滚到最右边就不滚了
//                    scrollTo(rightBorder - getWidth(), 0);
                    mXLastMove = mXMove;
                    return true;
                }
                scrollBy(scrollX, 0);
                mXLastMove = mXMove;
                break;
            case MotionEvent.ACTION_UP:
//
//                //手机抬起时，根据当前的滚动值来判断应该滚到哪个控件
//                int targetIndex = (getScrollX() + getWidth() / 3) / getWidth();
//                int dx = targetIndex * getWidth() - getScrollX();
//                mScroller.startScroll(getScrollX(), 0, dx, 0);
                if (getScrollX() - thisScroll > getWidth() * (getChildCount() - 1)) return true;
                boolean add = getWidth() / Math.abs(thisScroll) < 5;    //本次滑动距离大于宽度的三分之一
                int targetIndex = getScrollX() / getWidth();
                if (add) targetIndex = thisScroll < 0 ? targetIndex + 1 : targetIndex;
                int dx = targetIndex * getWidth() - getScrollX();
                mScroller.startScroll(getScrollX(), 0, dx, 0);
                invalidate();
                break;

        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }
}

