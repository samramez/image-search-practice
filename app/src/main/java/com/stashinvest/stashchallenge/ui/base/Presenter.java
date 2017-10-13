package com.stashinvest.stashchallenge.ui.base;

/**
 * Created by samramezanli on 10/10/17.
 */

public interface Presenter<V extends MvpView> {

    void attachView(V mvpView);

    void detachView();
}
