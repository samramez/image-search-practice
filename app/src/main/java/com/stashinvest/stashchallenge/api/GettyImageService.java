package com.stashinvest.stashchallenge.api;

import com.stashinvest.stashchallenge.api.model.ImageResponse;
import com.stashinvest.stashchallenge.api.model.MetadataResponse;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GettyImageService {
    public static final String FIELDS = "id,title,thumb";
    public static final String SORT_ORDER = "best";

    @Inject
    GettyImagesApi api;

    @Inject
    public GettyImageService() {
    }

    public Observable<ImageResponse> searchImages(String phrase) {
        return api.searchImages(phrase, FIELDS, SORT_ORDER);
    }


    public Observable<MetadataResponse> getImageMetadata(String id) {
        return api.getImageMetadata(id);
    }

    public Observable<ImageResponse> getSimilarImages(String id) {
        return api.getSimilarImages(id);
    }
}
