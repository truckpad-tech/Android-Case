package br.com.wesley.test.android_case.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CalculaPrecoCargaRetrofit {

    private final Retrofit retrofit;
    private static CalculaPrecoCargaRetrofit instance;

    private CalculaPrecoCargaRetrofit() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://tictac.api.truckpad.io")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized CalculaPrecoCargaRetrofit getInstance() {
        if (instance == null) {
            instance = new CalculaPrecoCargaRetrofit();
        }
        return instance;
    }

    public TruckPadCargaService getTruckPadCargaService() {
        return this.retrofit.create(TruckPadCargaService.class);
    }
}
