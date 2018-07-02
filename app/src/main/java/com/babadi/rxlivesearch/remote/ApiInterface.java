package com.babadi.rxlivesearch.remote;

import com.babadi.rxlivesearch.remote.model.TownModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface ApiInterface {

    String PRE_URL_TARABAR_NET_V1 = "/api/v1/";

    @GET(PRE_URL_TARABAR_NET_V1 + "get/livesearchtowns/{keyword}")
    @Headers({"Accept: application/json", "Content-Type: application/json"})
    Observable<List<TownModel>> searchTown(@Path("keyword") String keyword);

}
