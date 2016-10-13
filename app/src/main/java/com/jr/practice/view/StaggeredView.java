package com.jr.practice.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * 1.得到所有的子view
 * 2.为每个子view分组
 * 3.给每个子view布局
 * <p>
 * 缺点:没有处理padding、warp_content等,computer中换成dp
 * Created by Administrator on 2016-08-10.
 */

public class StaggeredView extends FrameLayout {
    private int screenWidth;        //屏幕宽度
    private int shortLineWidth;     //短边行宽度
    private int longLineWidth;      //长边行宽度 两边留20dp
    private Map<Integer, Integer> lineInfo;   //记录行数和长度
    private String TAG = "StaggeredView";


    public StaggeredView(Context context) {
        super(context);
        init();
    }

    public StaggeredView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            int highSpc = MeasureSpec.makeMeasureSpec(400, MeasureSpec.EXACTLY);
            super.onMeasure(widthMeasureSpec, highSpc);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private void computeView() {
        //初始化屏幕宽度，短边行宽度，长边行宽度
        screenWidth = getMeasuredWidth();
//        shortLineWidth = (int) (screenWidth - ScreenUtils.dp2px(100));
//        longLineWidth = (int) (screenWidth - ScreenUtils.dp2px(10));
        shortLineWidth = screenWidth / 10 *6;
        longLineWidth = screenWidth / 10 * 9;
        lineInfo = new HashMap<>();

        lineInfo.put(1, 0);

        //给view分组的方法
        int line = 1;
        int viewIndex = 0;      //当前view在本行的index
        for (int i = 0; i < getChildCount(); i++) {
            int width = line % 2 == 0 ? longLineWidth : shortLineWidth;
            View view = getChildAt(i);
            int lineWidth = lineInfo.get(line);
            if (lineWidth + view.getMeasuredWidth() > width) {
                line = line + 1;
                viewIndex = 0;
                lineInfo.put(line, view.getMeasuredWidth());
            } else {
                lineInfo.put(line, lineWidth + view.getMeasuredWidth());
            }
            view.setTag(new ViewInfo(line, viewIndex));
            viewIndex += 1;
        }
        //测试分组情况
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
//            Log.i(TAG, v.getTag().toString());
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        computeView();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);
        int lineLeft = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            ViewInfo viewInfo = (ViewInfo) view.getTag();
            int viewsLength = lineInfo.get(viewInfo.getLine());
            if (viewInfo.getViewIndex() == 0) {
                lineLeft = (screenWidth - viewsLength) / 2;
            }
            //l t r b
            int l = lineLeft;
            int t = viewInfo.getLine() * view.getMeasuredHeight();
            int r = lineLeft + view.getMeasuredWidth();
            int b = (viewInfo.getLine() + 1) * view.getMeasuredHeight();
            lineLeft = r;
//            Log.i(TAG, "line:" + viewInfo.getLine() + ", l:" + l + ", t:" + t + ", r:" + r + ", b: " + b + ",MeasuredWidth:" + view.getMeasuredWidth());
            view.layout(l, t, r, b);
        }
    }

    private class ViewInfo {
        int line;
        int viewIndex;

        public ViewInfo(int line, int viewIndex) {
            this.line = line;
            this.viewIndex = viewIndex;
        }

        public int getLine() {
            return line;
        }

        public void setLine(int line) {
            this.line = line;
        }

        public int getViewIndex() {
            return viewIndex;
        }

        public void setViewIndex(int viewIndex) {
            this.viewIndex = viewIndex;
        }

        @Override
        public String toString() {
            return "ViewInfo{" +
                    "line=" + line +
                    ", viewIndex=" + viewIndex +
                    '}';
        }
    }
}
