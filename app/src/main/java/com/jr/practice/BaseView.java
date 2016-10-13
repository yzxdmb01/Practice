package com.jr.practice;

/**
 * Created by Administrator on 2016-08-31.
 */

public interface BaseView<T> {
    void showMessage(String msg);

    void close();

    void showProgress(String msg);

    void showProgress(String msg, int progress);

    void hideProgress();

    void showErrorMessage(String msg, String content);

    void setPresenter(T presenter);
}
