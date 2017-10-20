package com.stashinvest.stashchallenge.ui.viewmodel;

import com.stashinvest.stashchallenge.api.model.ImageResponse;

public class SubmitUiEvent {

    boolean isProgress;
    boolean showEmpty;
    ImageResponse response;
    String errorMessage;

    public SubmitUiEvent(boolean isProgress, boolean showEmpty, ImageResponse response, String errorMessage) {
        this.isProgress = isProgress;
        this.showEmpty = showEmpty;
        this.response = response;
        this.errorMessage = errorMessage;
    }

    public static SubmitUiEvent success(ImageResponse response) {
        return new SubmitUiEvent(false, false, response, null);
    }

    public static SubmitUiEvent inProgress() {
        return new SubmitUiEvent(true, true, null, null);
    }

    public static SubmitUiEvent error(String error) {
        return new SubmitUiEvent(false, false, null, error);
    }

    public static SubmitUiEvent showEmpty() {
        return new SubmitUiEvent(false, true, null, null);
    }

    public boolean isProgress() {
        return isProgress;
    }

    public boolean isShowEmpty() {
        return showEmpty;
    }

    public ImageResponse getResponse() {
        return response;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
