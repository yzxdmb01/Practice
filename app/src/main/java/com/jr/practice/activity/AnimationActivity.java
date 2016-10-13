package com.jr.practice.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jr.practice.R;

import static android.view.animation.Animation.REVERSE;

/**
 * Created by Administrator on 2016-07-14.
 */
public class AnimationActivity extends BaseActivity {
    Button btnFly;
    Button btnCenter;
    RadioGroup rg;

    private int mLastX;
    private int mLastY;
    private TextView tvPercent;
    private RelativeLayout rlRoot;
    //试一下content
    private ViewGroup content;

    private static final int MESSAGE_SCROLL_TO = 1;
    private static final int FRAME_COUNT = 30;
    private static final int DELAYED_TIME = 33;
    private int mCount = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_SCROLL_TO:
                    mCount++;
                    if (mCount <= FRAME_COUNT) {
                        float fraction = mCount / (float) FRAME_COUNT;
                        int scrollX = (int) (fraction * 100);
                        content.scrollTo(scrollX, 0);
                        mHandler.sendEmptyMessageDelayed(MESSAGE_SCROLL_TO, DELAYED_TIME);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        btnFly = (Button) findViewById(R.id.btn_fly);
        tvPercent = (TextView) findViewById(R.id.tv_percent);
        rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
        content = (ViewGroup) findViewById(android.R.id.content);
        btnFly.bringToFront();
        btnCenter = (Button) findViewById(R.id.btn_center);
        rg = (RadioGroup) findViewById(R.id.radio_group);
        btnFly.setOnTouchListener((v, event) -> {
            int x = (int) event.getRawX();
            int y = (int) event.getRawY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mLastX = (int) event.getRawX();
                    mLastY = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int translateX = x - mLastX;
                    int translateY = y - mLastY;
                    v.setTranslationX(v.getTranslationX() + translateX);
                    v.setTranslationY(v.getTranslationY() + translateY);
                    mLastX = x;
                    mLastY = y;
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            return true;
        });
    }

    public void startAnima(View v) {
        btnFly.computeScroll();
        int[] btnFlyLocals = new int[2];
        btnFly.getLocationInWindow(btnFlyLocals);
        int[] btnFlyLocalScreen = new int[2];
        btnFly.getLocationOnScreen(btnFlyLocalScreen);
        Log.i(TAG, "windowX:" + btnFlyLocals[0] + ",windowY:" + btnFlyLocals[1] + ",screenX:" + btnFlyLocalScreen[0] + ",screenY:" + btnFlyLocalScreen[1]);
        int[] btnCenLocals = new int[2];
        btnCenter.getLocationInWindow(btnCenLocals);
        final float x = btnFlyLocals[0] - btnCenLocals[0];
        final float y = btnFlyLocals[1] - btnCenLocals[1];

        switch (rg.getCheckedRadioButtonId()) {
            case R.id.rb_line:
                /*直线*/
                PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("translationX", 0f, x);
                PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("translationY", 0f, y);
                ObjectAnimator a1 = ObjectAnimator.ofPropertyValuesHolder(v, pvhX, pvhY).setDuration(500);
                PropertyValuesHolder ppX = PropertyValuesHolder.ofFloat("scaleX", 1f,
                        1.2f, 0.8f, 1f);
                PropertyValuesHolder ppY = PropertyValuesHolder.ofFloat("scaleY", 1f,
                        1.2f, 0.8f, 1f);
                ObjectAnimator a2 = ObjectAnimator.ofPropertyValuesHolder(v, ppX, ppY).setDuration(300);
                AnimatorSet animSet = new AnimatorSet();
                animSet.play(a2).after(a1);
                animSet.start();
                break;
            case R.id.rb_parabola:
                /*抛物线*/
                ObjectAnimator anmi = ObjectAnimator.ofFloat(v, "yzx", 0f, 1f).setDuration(500);
                final float sudu = x;
                anmi.start();
                anmi.addUpdateListener(animation -> {
                    float cVal = (Float) animation.getAnimatedValue();
                    btnCenter.setTranslationX(x * cVal);
                    /*反向抛物线*/
//                        btnCenter.setTranslationY(y * (float) Math.sqrt(cVal));
                    btnCenter.setTranslationY(y * cVal * cVal);
                });
                break;
            case R.id.rb_view_anim:
                /*view动画*/
                //view动画很鸡肋，只是躯壳动了，灵魂还在原处。
                TranslateAnimation translateAnimation = new TranslateAnimation(0, x, 0, y);
                translateAnimation.setDuration(1000);
                translateAnimation.setFillAfter(false);
                translateAnimation.setRepeatCount(200);
                translateAnimation.setRepeatMode(REVERSE);  //反转动画
                btnCenter.startAnimation(translateAnimation);
                break;
            case R.id.rb_lp:
                /*LayoutParams*/
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) btnCenter.getLayoutParams();
//                layoutParams.topMargin += y;
                layoutParams.leftMargin += x;
                layoutParams.topMargin += y;
                Log.i(TAG, "X:" + x + ",y:" + y + ",marginTop:" + layoutParams.topMargin);
                btnCenter.setLayoutParams(layoutParams);
//                scrollBy和scrollTo只使用于对内容的移动
//                btnCenter.scrollBy(3,3);
                break;
            case R.id.rb_value_anmi:
                /*利用value动画*/
                ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 100).setDuration(1000);
                valueAnimator.addUpdateListener(animation -> {
                    //getAnimatedFraction 是获取的0-1的小数
                    //getAnimatedValue是获取的ofInt的区间的值
                    float fraction = animation.getAnimatedFraction();
                    tvPercent.setText(animation.getAnimatedValue() + "%");
                });
                valueAnimator.start();
                break;
            case R.id.rb_delay:
                /*延时策略*/
                mHandler.sendEmptyMessage(MESSAGE_SCROLL_TO);
                break;
        }

        new Handler().postDelayed(() -> {
            btnCenter.setTranslationY(0);
            btnCenter.setTranslationX(0);
            btnCenter.setScrollX(0);
            btnCenter.setScrollY(0);
        }, 1000);

    }
}