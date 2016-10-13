package com.jr.practice.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

import com.jr.practice.R;

/**
 * Created by Administrator on 2016-07-06.
 */
public class CustomViewActivity extends BaseActivity {
    private ObjectAnimator objectAnimator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);

        Button btn = (Button) findViewById(R.id.button);
        ViewWrapper viewWrapper = new ViewWrapper(btn);
        objectAnimator = ObjectAnimator.ofInt(viewWrapper, "width", 180, 700).setDuration(500);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.start();
        //帧动画开始播放
//        AnimationDrawable drawable = (AnimationDrawable) findViewById(R.id.view_anim_drawable).getBackground();
//        drawable.start();
    }

    private static class ViewWrapper {
        private View mTarget;

        public ViewWrapper(View mTarget) {
            this.mTarget = mTarget;
        }

        public int getWidth() {
            return mTarget.getLayoutParams().width;
        }

        public void setWidth(int width) {
            mTarget.getLayoutParams().width = width;
            mTarget.requestLayout();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        objectAnimator.cancel();
    }
}
