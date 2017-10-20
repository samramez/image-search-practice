package com.stashinvest.stashchallenge.ui.search;


import android.util.Log;

import com.stashinvest.stashchallenge.api.GettyImageService;
import com.stashinvest.stashchallenge.api.model.ImageResponse;
import com.stashinvest.stashchallenge.ui.base.BasePresenter;
import com.stashinvest.stashchallenge.ui.viewmodel.SubmitUiEvent;
import com.stashinvest.stashchallenge.util.RxUtils;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchPresenter extends BasePresenter<SearchMvpView> {

    private static final int MIN_CHARACTER = 1;

    // at least x millis since user last modified query
    private static final int MIN_TIME = 300;

    private Disposable disposable;
    private GettyImageService gettyImageService;

    @Inject
    public SearchPresenter(GettyImageService gettyImageService) {
        this.gettyImageService = gettyImageService;
    }

    @Override
    public void attachView(SearchMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        RxUtils.disposeIfNeeded(disposable);
    }

    // TODO: Replace RxJava side effects with an event object.
    public void search(Observable<CharSequence> textChangeObservable) {
        checkViewAttached();
        disposable = textChangeObservable
                /*.doOnNext(query ->  {
                    if (query.length() == 0) {
                        getMvpView().showEmpty();
                    }
                })*/
                //.filter(charSequence -> charSequence.length() >= MIN_CHARACTER)
                //.doOnNext(ignore ->  getMvpView().showLoading(true))
                .debounce(MIN_TIME, TimeUnit.MILLISECONDS)
                .switchMap(query ->
                        gettyImageService.searchImages(query.toString())
                                .map(SubmitUiEvent::success)
                                .onErrorReturn(t -> SubmitUiEvent.error(t.getMessage()))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .startWith(SubmitUiEvent.inProgress()))
/*                .switchMap(query ->
                        query.length() != 0
                            ? gettyImageService.searchImages(query.toString())
                                    .map(SubmitUiEvent::success)
                                    .onErrorReturn(t -> SubmitUiEvent.error(t.getMessage()))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .startWith(SubmitUiEvent.inProgress())
                            : Observable.empty())*/
                .subscribe(response -> {


                    getMvpView().showLoading(response.isProgress());
                    getMvpView().showEmpty(response.isShowEmpty());


                    /*Log.d("##", "successful response");
                    if (response != null || !response.getImages().isEmpty()) {
                        getMvpView().showResults(response);
                    } else {
                        getMvpView().showEmpty();
                    }*/
                }, error -> {
                    Log.e("##", "error getting result: " + error);
                    getMvpView().showError();
                });
    }
}
