package com.stashinvest.stashchallenge.ui.fragment.popupdialog;

import android.util.Pair;

import com.stashinvest.stashchallenge.api.GettyImageService;
import com.stashinvest.stashchallenge.api.model.ImageResponse;
import com.stashinvest.stashchallenge.api.model.MetadataResponse;
import com.stashinvest.stashchallenge.ui.base.BasePresenter;
import com.stashinvest.stashchallenge.util.RxUtils;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PopUpDialogPresenter extends BasePresenter<PopUpDialogMvpView> {

    private GettyImageService gettyImageService;

    private Disposable disposable;

    @Inject
    public PopUpDialogPresenter(GettyImageService gettyImageService) {
        this.gettyImageService = gettyImageService;
    }

    @Override
    public void attachView(PopUpDialogMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        RxUtils.disposeIfNeeded(disposable);
    }


    void getImageData(String id) {
        checkViewAttached();
        disposable = Observable.zip(
                        getSimilarImagesObservable(id),
                        getImageMetaDataObservable(id),
                        Pair::new)
                    .subscribe(responsePair -> {

                            getMvpView().showResult();

                            if (responsePair.first != null) {
                                getMvpView().onSuccessGettingImageData(responsePair.first);
                            }

                            if (responsePair.second != null) {
                                getMvpView().onSuccessGettingMetaData(responsePair.second);
                            }
                        },
                            error -> getMvpView().showError(error.getMessage())
                    );
    }

    private Observable<ImageResponse> getSimilarImagesObservable(String id) {
        return gettyImageService.getSimilarImages(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(t -> {
                    getMvpView().showError(t.getMessage());
                    return new ImageResponse();
                });
    }

    private Observable<MetadataResponse> getImageMetaDataObservable(String id) {
        return gettyImageService.getImageMetadata(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(t -> {
                    getMvpView().showError(t.getMessage());
                    return new MetadataResponse();
                });
    }
}
