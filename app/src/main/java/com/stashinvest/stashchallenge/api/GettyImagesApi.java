package com.stashinvest.stashchallenge.api;

import com.stashinvest.stashchallenge.api.model.ImageResponse;
import com.stashinvest.stashchallenge.api.model.MetadataResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GettyImagesApi {
    @GET("search/images")
    Call<ImageResponse> searchImages(@Query("phrase") String phrase,
                                     @Query("fields") String fields,
                                     @Query("sort_order") String sortOrder);

    @GET("images/{id}")
    Call<MetadataResponse> getImageMetadata(@Path("id") String id);

    @GET("images/{id}/similar")
    Call<ImageResponse> getSimilarImages(@Path("id") String id);
}
