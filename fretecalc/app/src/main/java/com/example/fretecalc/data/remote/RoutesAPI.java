package com.example.fretecalc.data.remote;

import com.example.fretecalc.models.routes.RouteRequest;
import com.example.fretecalc.models.routes.RouteResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RoutesAPI {

    @Headers("Content-Type: application/json")
    @POST("route")
    Observable<RouteResponse> postRoute(@Body RouteRequest body);
}
