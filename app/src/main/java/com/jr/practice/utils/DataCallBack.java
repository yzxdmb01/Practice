package com.jr.practice.utils;

import java.io.IOException;

import okhttp3.Request;

/**
 * 数据回调时的接口
 * Created by Administrator on 2016-09-01.
 */

public interface DataCallBack {
    //请求失败的回调
    void requestFailure(Request request, IOException e);

    //请求成功时的回调
    void requestSuccess(String result) throws Exception;
}
