package com.diegobezerra.truckpadcase.data.api.geo

import com.diegobezerra.truckpadcase.data.api.geo.GeoService.Companion.BASE_URL
import com.diegobezerra.truckpadcase.domain.model.RequestPlace
import com.diegobezerra.truckpadcase.domain.model.RouteInfo
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface GeoService {

    companion object {
        const val BASE_URL = "https://geo.api.truckpad.io/v1/"
    }

    @POST("route")
    suspend fun getRouteInfo(@Body body: RouteBody): Response<RouteInfo>
}

fun provideGeoService(): GeoService {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GeoService::class.java)
}

data class RouteBody(
    @SerializedName("fuel_consumption")
    val fuelConsumption: Float,
    @SerializedName("fuel_price")
    val fuelPrice: Float,
    val places: List<RequestPlace>
)