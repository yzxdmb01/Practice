package com.jr.practice.moudle;

import com.jr.practice.componet.PoetryScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016-08-30.
 */

@Module
public class PoteryModule {
    @PoetryScope
    @Provides
    public Poetry providePoetry(String poems) {
        return new Poetry(poems);
    }

    @Provides
    public String providePoems() {
        return "只有意志坚强的人，才能到达彼岸";
    }
}
