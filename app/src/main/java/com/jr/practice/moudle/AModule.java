package com.jr.practice.moudle;

import com.jr.practice.componet.AScope;
import com.jr.practice.componet.PoetryQualifier;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016-08-30.
 */
@Module
public class AModule {
    @PoetryQualifier("A")
    @AScope
    @Provides
    public Poetry providePoetry() {
        return new Poetry("万物美好");
    }

    @PoetryQualifier("B")
    @AScope
    @Provides
    public Poetry getOtherPoetry() {
        return new Poetry("我在下面的");
    }

}
