package com.jr.practice.moudle;

import com.google.gson.Gson;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016-08-30.
 */

@Module
public class MainModule {

    /**
     * @return
     * @Provides 注解标示这个方法是用来创建某个实例对象的，这里我们创建返回Gson对象
     * 方法名随便，一般用provideXXX结构
     */
//    @Provides
//    public Gson provideGson() {
//        return new Gson();
//    }
}
