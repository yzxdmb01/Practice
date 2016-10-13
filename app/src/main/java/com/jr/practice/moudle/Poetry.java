package com.jr.practice.moudle;

import javax.inject.Inject;

/**
 * Created by Administrator on 2016-08-30.
 */

public class Poetry {
    private String mPemo;

    @Inject
    public Poetry() {
        this.mPemo = "生活就像海洋";
    }

    public Poetry(String mPemo) {
        this.mPemo = mPemo;
    }

    public String getmPemo() {
        return mPemo;
    }

}
