package com.jr.practice.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.jr.practice.R;
import com.jr.practice.utils.L;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        initEmoji();
    }

    private void initEmoji() {
        String textStr = "今天很开心[s:1]嘿嘿嘿[s:0]完了";
        String reg = "\\[s:\\d+\\]";
        Matcher matcher = Pattern.compile(reg).matcher(textStr);
        StringBuilder sb = new StringBuilder();
        int startIndex = 0;
        while (matcher.find()) {
            sb.append(textStr.substring(startIndex, matcher.start()));
            String id = matcher.group().substring(3, matcher.group().length() - 1);
            sb.append("<img src='" + "http://my_emotion_img/" + id + ".gif'/>");
            startIndex = matcher.end();
        }
        sb.append(textStr.substring(startIndex));
        TextView textView = (TextView) findViewById(R.id.text_view);
        Spanned mSpanned = Html.fromHtml(sb.toString(), new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(CustomViewActivity.this.getAssets().open("emotions/0.gif"));
                } catch (IOException e) {
                    L.i("???");
                    e.printStackTrace();
                }
                BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
                L.i("source:"+source+",drawable:"+bitmapDrawable);
                bitmapDrawable.setBounds(150,150,150,150);
                Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
                drawable.setBounds(50,50,50,50);
                return drawable;
            }
        },null);

        textView.setText(mSpanned);
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
