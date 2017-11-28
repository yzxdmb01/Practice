package com.jr.practice.splash;

import android.os.Handler;

/**
 * Created by Administrator on 2016-08-31.
 */

public class SplashPresenter implements SplashContract.Presenter {
    private SplashContract.View splashView;

    public SplashPresenter(SplashContract.View view) {
        splashView = view;
        view.setPresenter(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void taskComplete() {
        splashView.hideProgress();
        splashView.toMainActivity();
    }

    @Override
    public void start() {
        if (false) {
            splashView.showProgress("いく");
            new Handler().postDelayed(() -> taskComplete(), 2000);
        } else {
            taskComplete();
        }
    }
}
