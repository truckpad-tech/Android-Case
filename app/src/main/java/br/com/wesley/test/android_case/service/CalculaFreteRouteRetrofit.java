package br.com.wesley.test.android_case.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CalculaFreteRouteRetrofit {

    private final Retrofit retrofit;
    private static CalculaFreteRouteRetrofit instance;

    private CalculaFreteRouteRetrofit() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://geo.api.truckpad.io")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    public static synchronized CalculaFreteRouteRetrofit getInstance() {
        if (instance == null) {
            instance = new CalculaFreteRouteRetrofit();
        }
        return instance;
    }

    public TruckPadRouteService getTruckPadRouteService() {
        return this.retrofit.create(TruckPadRouteService.class);
    }
}
