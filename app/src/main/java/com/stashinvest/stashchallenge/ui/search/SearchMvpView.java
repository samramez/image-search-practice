package com.stashinvest.stashchallenge.ui.search;

import com.stashinvest.stashchallenge.api.model.ImageResponse;
import com.stashinvest.stashchallenge.ui.base.MvpView;

/**
 * Created by samramezanli on 10/10/17.
 */

public interface SearchMvpView extends MvpView {

    void showError();

    void showResults(ImageResponse response);

    void showLoading(boolean showLoading);

    void showEmpty(boolean showEmpty);
}
