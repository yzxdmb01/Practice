package com.jr.practice.moudle;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016-08-30.
 */

@Module
public class ApplicationModule {
    @Singleton
    @Provides
    public Gson provideGson() {
        return new Gson();
    }
}
