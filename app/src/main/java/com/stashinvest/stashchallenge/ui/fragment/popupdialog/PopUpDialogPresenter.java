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


    public void getImageData(String id) {
        checkViewAttached();
        disposable = Observable.zip(getSimilarImagesObserbale(id),
                getImageMetaDataObservable(id), Pair::new)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responsePair -> {
                    //todo: finsih metho.
                });


    }

    private Observable<ImageResponse> getSimilarImagesObserbale(String id) {
        return gettyImageService.getSimilarImages(id);
                //.onErrorReturn(t -> ); //todo: return empty image response.
    }

    private Observable<MetadataResponse> getImageMetaDataObservable(String id) {
        return gettyImageService.getImageMetadata(id);
                //.onErrorReturn();
    }
}
