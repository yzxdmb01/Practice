package com.jr.practice.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jr.practice.R;
import com.jr.practice.utils.BaseCallBack;
import com.jr.practice.utils.BaseUtils;
import com.jr.practice.utils.Cat;
import com.jr.practice.utils.L;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

        btnSendReq.setOnClickListener(v -> saveNetPic(tvRes));


        Button btnSaveBitmap = (Button) findViewById(R.id.btn_save_bitmap);
        btnSaveBitmap.setOnClickListener(v -> saveBitmap(v, tvRes));
    }

    /**
     * 保存当前Activity的截图
     * @param v
     * @param tvRes
     */
    private void saveBitmap(View v, TextView tvRes) {
        if (!checkPermission()) {
            saveBitmap(v, tvRes);
            return;
        }
        getWindow().getDecorView().getRootView().buildDrawingCache();
        Bitmap bitmap = getWindow().getDecorView().getRootView().getDrawingCache();
        String sdcardPath = Environment.getExternalStorageDirectory().getPath();
        String picName = sdcardPath + File.separator + "测试" + File.separator + SystemClock.elapsedRealtime() + ".jpg";
        File file = new File(picName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            runOnUiThread(() -> tvRes.setText(picName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendReq(final TextView tvRes) throws NoSuchMethodException {
        //异步网络请求
        OkHttpClient client = new OkHttpClient();
        checkPermission();
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

    private boolean checkPermission() {
        boolean permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (!permission) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        return permission;
    }

    private void saveNetPic(TextView v) {
        BaseUtils.test(new BaseCallBack<Cat>() {
            @Override
            public void onSuccess(Cat cat) {
                v.setText(cat.toString());
            }
        });
    }
}
