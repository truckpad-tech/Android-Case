package br.com.wesley.test.android_case.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NominatimRetrofit {

    private final Retrofit retrofit;
    private static NominatimRetrofit instance;

    private NominatimRetrofit() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://nominatim.openstreetmap.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    public static synchronized NominatimRetrofit getInstance() {
        if (instance == null) {
            instance = new NominatimRetrofit();
        }
        return instance;
    }

    public NominatimService getNominatimService() {
        return this.retrofit.create(NominatimService.class);
    }
}
