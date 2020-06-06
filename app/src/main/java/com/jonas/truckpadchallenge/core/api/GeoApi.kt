package com.jonas.truckpadchallenge.core.api

import com.jonas.truckpadchallenge.search.data.request.RouteCalculation
import com.jonas.truckpadchallenge.search.data.response.RouteResponse
import io.reactivex.Maybe
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface GeoApi {
    companion object {
        const val BASE_URL_ROUTE = "https://geo.api.truckpad.io/v1/"
    }

    @POST("route")
    fun calculateRoute(
        @Body infoRoute: RouteCalculation
    ): Maybe<Response<RouteResponse>>
}