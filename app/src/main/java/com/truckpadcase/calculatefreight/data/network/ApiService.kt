package com.truckpadcase.calculatefreight.data.network

import com.google.gson.GsonBuilder
import com.truckpadcase.calculatefreight.utils.Constants.BASE_ROUTE_URL
import com.truckpadcase.calculatefreight.utils.Constants.PRICE_ROUTE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiService {
    private fun initRouteRetrofit(): Retrofit {
        val gsonBuilder: GsonBuilder =
            GsonBuilder()
        gsonBuilder.serializeNulls()

        return Retrofit.Builder()
            .baseUrl(BASE_ROUTE_URL)
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
            .build()
    }

    val ROUTE_SERVICE: RouteApiService = initRouteRetrofit().create(RouteApiService::class.java)

    private fun initPriceRetrofit(): Retrofit {

        return Retrofit.Builder()
            .baseUrl(PRICE_ROUTE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val PRICE_SERVICE: PriceApiService = initPriceRetrofit().create(PriceApiService::class.java)

}