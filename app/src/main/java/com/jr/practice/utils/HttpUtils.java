package com.jr.practice.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 对okHttp进行简单的封装
 * 应该包含的功能:
 * 异步的get请求，post请求
 * HttpUtils.get(url,callBack)+
 * HttpUtls.post(url,callBack).addParams()
 * Created by Administrator on 2016-09-01.
 * get post url
 * 回调带状态，数据
 *
 * 先写一个activity试一试各种请求
 * 在试着封装一下
 */

public class HttpUtils {
    private static HttpUtils httpUtils;
    private static OkHttpClient client;

    public HttpUtils() {
        client = new OkHttpClient();
        //设置连接超时，读取超时，写入超市
        client.newBuilder().connectTimeout(10, TimeUnit.SECONDS);
        client.newBuilder().readTimeout(20, TimeUnit.SECONDS);
        client.newBuilder().writeTimeout(20, TimeUnit.SECONDS);
    }

    public static HttpUtils getInstance() {
        if (httpUtils == null) {
            httpUtils = new HttpUtils();
        }
        return httpUtils;
    }


    /**
     * 同步的get请求方法
     *
     * @param url
     * @return
     */
    public static Response getSync(String url) {
        Request request = new Request.Builder().url(url).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static String getSyncString(String url) {
        String result = null;
        try {
            result = getSync(url).body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void deliverDataFailure(final Request request, final IOException e, final DataCallBack callBack) {
        //这里需要使用异步处理?
        callBack.requestFailure(request, e);
        MediaType.parse("image/png");
    }

    private void deliverDataSuccess(String result, DataCallBack callBack) {
        try {
            callBack.requestSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getAsync(String url, final DataCallBack callBack) {
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(call.request(), e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result;
                result = response.body().string();
                deliverDataSuccess(result, callBack);
            }
        });
    }

    public static void postAsync(String url, Map<String, String> params, final DataCallBack dataCallBack) {
        if (params == null) {
            params = new HashMap<>();
        }
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> map : params.entrySet()) {
            builder.add(map.getKey(), map.getValue());
        }
        RequestBody requestBody = builder.build();

    }


}
