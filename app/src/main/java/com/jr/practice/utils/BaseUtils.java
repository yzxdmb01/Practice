package com.jr.practice.utils;

/**
 * Created by Administrator on 2016-09-02.
 */

public class BaseUtils {
    public static void test(BaseCallBack callBack) {
        Cat c = new Cat("白");

        callBack.onSuccess(c);
    }
}
