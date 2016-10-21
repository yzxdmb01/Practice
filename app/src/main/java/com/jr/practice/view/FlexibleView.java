package com.jr.practice.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jr.practice.R;

/**
 * 想弄一个圆形变圆角矩形动画的view
 * 自定义属性：背景色，背景图，边框色，边框粗细，文字色，文字内容,文字大小
 * Created by Administrator on 2016-10-17.
 */

public class FlexibleView extends View {
    private ImageView imageView;    //图片部分
    private Drawable drawable;
    private int bgColor;
    private int strokeColor;
    private int strokeWidth;
    private int textColor;
    private float textSize;
    private String text;
    private TextView textView;      //文字部分

    private float textWidth;
    private float textHeight;

    private Path mBackgroundPath;

    private Paint textPaint;

    private int MAX_WIDTH;
    private int MAX_HEIGHT;

    private Paint mPaintBg;
    private Paint mPaintStrokeBg;


    public FlexibleView(Context context) {
        this(context, null);
    }

    public FlexibleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlexibleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        //读取自定义属性
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.FlexibleView);
        bgColor = typedArray.getColor(R.styleable.FlexibleView_fvBackgroundColor, Color.parseColor("#00548F"));
        strokeColor = typedArray.getColor(R.styleable.FlexibleView_fvStrokeColor, Color.parseColor("#000000"));
        text = typedArray.getString(R.styleable.FlexibleView_fvText);
        drawable = typedArray.getDrawable(R.styleable.FlexibleView_fvDrawable);
        textSize = typedArray.getDimension(R.styleable.FlexibleView_fvTextSize, 30);
        //...

        initPaint();
        textWidth = getTextWidth(text, textPaint);
        textHeight = getTextHeight(text, textPaint);
        mBackgroundPath = new Path();
        mBackgroundPath.reset();

        typedArray.recycle();
    }

    private float getTextHeight(String text, Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height() / 1.1f;
    }

    private void initPaint() {
        textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
    }

    private float getTextWidth(String text, Paint paint) {
        return paint.measureText(text);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        measureChild(imageView,widthMeasureSpec,heightMeasureSpec);
//        setMeasuredDimension(imageView.getMeasuredWidth()+20,imageView.getMeasuredHeight());
    }
}
