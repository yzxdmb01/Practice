package com.jr.practice.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.jr.practice.R;
import com.jr.practice.utils.BaseCallBack;
import com.jr.practice.utils.BaseUtils;
import com.jr.practice.utils.Cat;
import com.jr.practice.utils.L;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DrawableActivity extends BaseActivity {
    private String url = "http://gc.ditu.aliyun.com/geocoding";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawable_layout);

        testHttp();
    }

    private void testHttp() {
        Button btnSendReq = (Button) findViewById(R.id.btn_send_req);
        TextView tvRes = (TextView) findViewById(R.id.tv_res);

        btnSendReq.setOnClickListener(v -> testT(tvRes));
    }

    private void sendReq(final TextView tvRes) {
        //异步网络请求
        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder().url(url).build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                runOnUiThread(() -> tvRes.setText("失败：" + e.toString()));
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                runOnUiThread(() -> {
//                    try {
//                        tvRes.setText("成功：" + response.body().string());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                });
//            }
//        });
        boolean permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (!permission) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            return;
        }
        tvRes.setText("permission:" + permission);
        String path = Environment.getExternalStorageDirectory().getPath();
        L.i(path);

        //使用okHttp下载文件
        String imgUrl = "http://isujin.com/wp-content/uploads/2016/08/wallhaven-298807.jpg";
        Request request = new Request.Builder().url(imgUrl).build();
        client.newBuilder().connectTimeout(1000, TimeUnit.SECONDS);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> tvRes.setText("失败了~"));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(() -> tvRes.setText("onResponse"));
                InputStream inputStream = response.body().byteStream();
                FileOutputStream fos = new FileOutputStream(new File(path + "/sujin2.jpg"));
                byte[] buffer = new byte[2048];
                int len = 0;
                while ((len = inputStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                fos.flush();
                runOnUiThread(() -> tvRes.setText("文件下载完了"));
            }
        });
    }

    private void testT(TextView v) {
        BaseUtils.test(new BaseCallBack<Cat>() {
            @Override
            public void onSuccess(Cat cat) {
                v.setText(cat.toString());
            }
        });
    }
}
