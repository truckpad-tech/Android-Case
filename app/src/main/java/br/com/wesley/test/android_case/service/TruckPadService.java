package br.com.wesley.test.android_case.service;

import br.com.wesley.test.android_case.model.PlacesApi;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface TruckPadService {

    @Headers({
            "Accept: application/json",
    })
    @GET("autocomplete")
    Call<PlacesApi> getCities(@Query("search") String city);
}
