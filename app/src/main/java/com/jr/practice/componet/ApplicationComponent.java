package com.jr.practice.componet;

import com.google.gson.Gson;
import com.jr.practice.moudle.AModule;
import com.jr.practice.moudle.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Administrator on 2016-08-30.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    Gson getGson();

    //添加声明
    AComponent plus(AModule module);
}
