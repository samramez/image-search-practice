package com.stashinvest.stashchallenge.ui.fragment.popupdialog;

import com.stashinvest.stashchallenge.api.model.ImageResponse;
import com.stashinvest.stashchallenge.api.model.MetadataResponse;
import com.stashinvest.stashchallenge.ui.base.MvpView;

public interface PopUpDialogMvpView extends MvpView {

    void showError(String error);

    void showResult();

    void onSuccessGettingImageData(ImageResponse response);

    void onSuccessGettingMetaData(MetadataResponse response);
}
