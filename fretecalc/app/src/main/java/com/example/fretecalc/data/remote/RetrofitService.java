package com.example.fretecalc.data.remote;

import com.facebook.stetho.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private static final String ROUTES_BASE_URL = "https://geo.api.truckpad.io/v1/";
    private static final String PRICES_BASE_URL = "https://tictac.api.truckpad.io/v1/";
    private static Retrofit retrofitRoutes;
    private static Retrofit retrofitPrice;

    private static Retrofit getRoutes() {

        if (retrofitRoutes == null) {

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.readTimeout(30, TimeUnit.SECONDS);
            httpClient.connectTimeout(30, TimeUnit.SECONDS);
            httpClient.writeTimeout(30, TimeUnit.SECONDS);

            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                httpClient.addInterceptor(httpLoggingInterceptor);
            }

            retrofitRoutes = new Retrofit.Builder()
                    .baseUrl(ROUTES_BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }

        return retrofitRoutes;
    }

    public static RoutesAPI getRoutesService() {
        return getRoutes().create(RoutesAPI.class);
    }

    private static Retrofit getPrices() {

        if (retrofitPrice == null) {

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.readTimeout(30, TimeUnit.SECONDS);
            httpClient.connectTimeout(30, TimeUnit.SECONDS);
            httpClient.writeTimeout(30, TimeUnit.SECONDS);

            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                httpClient.addInterceptor(httpLoggingInterceptor);
            }

            retrofitPrice = new Retrofit.Builder()
                    .baseUrl(PRICES_BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }

        return retrofitPrice;
    }

    public static PricesAPI getPricesService() {
        return getPrices().create(PricesAPI.class);
    }
}
