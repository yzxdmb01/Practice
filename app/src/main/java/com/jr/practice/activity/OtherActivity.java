package com.jr.practice.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jr.practice.R;
import com.jr.practice.componet.MainComponent;
import com.jr.practice.moudle.Poetry;

import javax.inject.Inject;

public class OtherActivity extends BaseActivity {
    @Inject
    Poetry mPoetry;
    @Inject
    Gson mGson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);

        MainComponent.getInstance().inject(this);

        initView();
    }

    private void initView() {
        TextView tv = (TextView) findViewById(R.id.textView);
        String json = mGson.toJson(mPoetry.getmPemo() + ",mPoetry:" + mPoetry);
        tv.setText(json);


        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
