package com.jr.practice.componet;

import com.jr.practice.JrApplication;
import com.jr.practice.activity.DrawableActivity;
import com.jr.practice.activity.OtherActivity;
import com.jr.practice.moudle.MainModule;
import com.jr.practice.moudle.PoteryModule;


import dagger.Component;

/**
 * Created by Administrator on 2016-08-30.
 */
@PoetryScope
@Component(dependencies = ApplicationComponent.class, modules = {MainModule.class, PoteryModule.class})
public abstract class MainComponent {

    /**
     * 需要用到这个连接器的对象，就是这个对象里面有需要注入的属性
     */
    public abstract void inject(DrawableActivity activity);

    public abstract void inject(OtherActivity activity);

    private static MainComponent sComponent;

    public static MainComponent getInstance() {
        if (sComponent == null) {
            sComponent = DaggerMainComponent.builder().applicationComponent(JrApplication.getInstence().getmApplicationComponent()).build();
        }
        return sComponent;
    }
}
