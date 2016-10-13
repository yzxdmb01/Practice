package com.jr.practice.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jr.practice.JrApplication;
import com.jr.practice.R;
import com.jr.practice.componet.PoetryQualifier;
import com.jr.practice.moudle.Poetry;

import javax.inject.Inject;

public class AActivity extends AppCompatActivity {

    @PoetryQualifier("A")
    @Inject
    Poetry mPoetry;
    @PoetryQualifier("B")
    @Inject
    Poetry mPoeTryUnder;
    @Inject
    Gson mGson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);

        JrApplication.getInstence().getAComponent().inject(this);
        TextView tv = (TextView) findViewById(R.id.textView2);
        String text = mPoetry.getmPemo() + ",mPoetry:" + mPoetry + (mGson == null ? "Gson没有被注入" : "Gson已经被注入");
        tv.setText(text);
        ((TextView) findViewById(R.id.textView3)).setText(mPoeTryUnder.getmPemo());

    }
}
