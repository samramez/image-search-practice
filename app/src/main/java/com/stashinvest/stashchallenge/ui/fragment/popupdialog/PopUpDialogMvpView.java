package com.stashinvest.stashchallenge.ui.fragment.popupdialog;

import com.stashinvest.stashchallenge.api.model.ImageResponse;
import com.stashinvest.stashchallenge.ui.base.MvpView;

public interface PopUpDialogMvpView extends MvpView {

    void shodError(String error);

    void onSuccessGettingImageData(ImageResponse response);
}
