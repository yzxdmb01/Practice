package com.jr.practice.splash;

import com.jr.practice.BasePresenter;
import com.jr.practice.BaseView;

/**
 * Created by Administrator on 2016-08-31.
 */

public interface SplashContract {
    interface Presenter extends BasePresenter<View> {
        void initData();
        void taskComplete();
    }

    interface View extends BaseView<Presenter> {
        void toMainActivity();
    }
}
