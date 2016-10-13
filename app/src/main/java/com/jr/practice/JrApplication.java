package com.jr.practice;

import android.app.Application;
import android.content.Context;

import com.jr.practice.componet.AComponent;
import com.jr.practice.componet.ApplicationComponent;
import com.jr.practice.componet.DaggerApplicationComponent;
import com.jr.practice.moudle.AModule;
import com.jr.practice.utils.CrashHandler;

/**
 * Created by Administrator on 2016-08-10.
 */

public class JrApplication extends Application {
    private static JrApplication application;
    private AComponent mAComponent;
    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        application = JrApplication.this;
        //在这里设置程序异常处理，然后程序才能获得未处理的异常
        CrashHandler crashHandler = CrashHandler.getsInstance();
        crashHandler.init(this.getApplicationContext());

        mApplicationComponent = DaggerApplicationComponent.builder().build();
    }

    public ApplicationComponent getmApplicationComponent() {
        return mApplicationComponent;
    }

    public AComponent getAComponent() {
        if (mAComponent == null) {
            mAComponent = mApplicationComponent.plus(new AModule());
        }
        return mAComponent;
    }

    public static JrApplication getInstence() {
        return application;
    }

    public static Context getContext() {
        return application;
    }
}
