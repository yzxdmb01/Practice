package com.jr.practice.splash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.jr.practice.MainActivity;
import com.jr.practice.R;
import com.jr.practice.activity.BaseActivity;
import com.jr.practice.utils.L;

import javax.inject.Inject;

public class SplashActivity extends BaseActivity implements SplashContract.View {
    SplashContract.Presenter mPresenter;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //Create the presenter
        new SplashPresenter(this);
    }

    @Override
    public void toMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void showMessage(String msg) {

    }

    @Override
    public void close() {

    }

    @Override
    public void showProgress(String msg) {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showProgress(String msg, int progress) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showErrorMessage(String msg, String content) {

    }

    @Override
    public void setPresenter(SplashContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
