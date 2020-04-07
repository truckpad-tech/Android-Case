package br.com.wesley.test.android_case.service;

import br.com.wesley.test.android_case.model.PlaceRoute;
import br.com.wesley.test.android_case.model.PlaceRouteResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface TruckPadRouteService {

    @Headers({
            "Accept: application/json",
    })
    @POST("v1/route")
    Call<PlaceRouteResponse> getRoute(@Body PlaceRoute placeRoute);
}
