package br.com.wesley.test.android_case.service;

import br.com.wesley.test.android_case.model.AddressNominatim;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NominatimService {

    @GET("reverse")
    Call<AddressNominatim> getAddress(@Query("format") String formart, @Query("lat") double latitude, @Query("lon") double longitude);
}
