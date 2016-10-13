package com.example.jr.glidestudy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {
    String imgUrl = "http://zhuangbi.idagou.com/i/2015-07-07-47ade5e83380ff5ac0bc5ea7e1fd3a04.gif";
    private ImageView ivGif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivGif = (ImageView) findViewById(R.id.iv_gif);
    }

    public void loadGif(View view) {
        ivGif.setImageResource(android.R.drawable.screen_background_light_transparent);
        Glide.with(this).load(imgUrl)
                .crossFade().into(ivGif);
    }

    public void loadGifWithProgress(View view) {
        ivGif.setImageResource(android.R.drawable.screen_background_light_transparent);
        Glide.with(this).load(imgUrl)
                .crossFade().into(ivGif);
    }
}
