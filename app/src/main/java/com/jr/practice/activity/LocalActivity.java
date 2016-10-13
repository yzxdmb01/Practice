package com.jr.practice.activity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jr.practice.R;

public class LocalActivity extends BaseActivity {
    private TextView tvShake;
    private String TAG = "LocalActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);
        tvShake = (TextView) findViewById(R.id.tv_shake);

        Button btn = (Button) findViewById(R.id.btn_test);
        LinearLayout llRoot = (LinearLayout) findViewById(R.id.ll_root);
        TextView tv = (TextView) findViewById(R.id.tv_test);

//        btn.setOnTouchListener((v, event) -> {
//            Log.i(TAG,"Button onTouch");
//            return false;
//        });
//        tv.setOnTouchListener((v, event) -> {
//            Log.i(TAG,"TextView onTouch");
//            return false;
//        });
        llRoot.setOnTouchListener((v, event) -> {
            Log.i(TAG,"LinearLayout onTouch");
            return false;
        });
        btn.setOnClickListener(v -> Log.i(TAG, "Button OnClick"));
    }

    private void init() {

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        SensorEventListener listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float xValue = Math.abs(event.values[0]);
                float yValue = Math.abs(event.values[1]);
                float zValue = Math.abs(event.values[2]);
                Log.i(TAG, "x:" + xValue + ",y:" + yValue + ",z:" + zValue);
                if (xValue > 10 || yValue > 10 || zValue > 10) {
                    tvShake.setText("摇一摇");
                } else {
                    tvShake.setText("abc");
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
}
