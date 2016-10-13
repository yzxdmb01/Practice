package com.jr.practice.componet;

import com.jr.practice.activity.AActivity;
import com.jr.practice.moudle.AModule;

import dagger.Subcomponent;

/**
 * Created by Administrator on 2016-08-30.
 */

@AScope
@Subcomponent(modules = AModule.class)
public interface AComponent {
    void inject(AActivity aActivity);
}
