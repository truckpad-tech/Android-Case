package com.example.fretecalc.data.remote;

import com.example.fretecalc.models.prices.PriceRequest;
import com.example.fretecalc.models.prices.PriceResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface PricesAPI {

    @Headers("Content-Type: application/json")
    @POST("antt_price/all")
    Observable<PriceResponse> postPrice(@Body PriceRequest body);
}
