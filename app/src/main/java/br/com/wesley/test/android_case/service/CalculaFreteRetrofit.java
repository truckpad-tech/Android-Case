package br.com.wesley.test.android_case.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CalculaFreteRetrofit {

    private final Retrofit retrofit;
    private static CalculaFreteRetrofit instance;

    private CalculaFreteRetrofit() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://api.recalculafrete.com.br")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized CalculaFreteRetrofit getInstance() {
        if (instance == null) {
            instance = new CalculaFreteRetrofit();
        }
        return instance;
    }

    public TruckPadService getTruckPadService() {
        return this.retrofit.create(TruckPadService.class);
    }
}
