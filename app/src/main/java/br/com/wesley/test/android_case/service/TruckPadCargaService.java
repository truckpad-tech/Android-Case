package br.com.wesley.test.android_case.service;

import br.com.wesley.test.android_case.model.PrecoCarga;
import br.com.wesley.test.android_case.model.PrecoCargaResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface TruckPadCargaService {

    @Headers({
            "Accept: application/json",
    })
    @POST("v1/antt_price/all")
    Call<PrecoCargaResponse> getPrecoCarga(@Body PrecoCarga precoCarga);
}
