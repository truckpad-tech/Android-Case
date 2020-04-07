package com.truckpadcase.calculatefreight.data.network

import com.truckpadcase.calculatefreight.domain.model.remote.RouteRequest
import com.truckpadcase.calculatefreight.domain.model.remote.RouteResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RouteApiService {

    @Headers("Content-Type: application/json;charset=utf-8")
    @POST("v1/route")
    suspend fun routePost(@Body routeRequest: RouteRequest?): RouteResponse
}