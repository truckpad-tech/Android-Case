package com.truckpadcase.calculatefreight.data.network

import com.truckpadcase.calculatefreight.domain.model.remote.PriceRequest
import com.truckpadcase.calculatefreight.domain.model.remote.PriceResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface PriceApiService {

    @Headers("Content-Type: application/json;charset=utf-8")
    @POST("v1/antt_price/all")
    suspend fun pricePost(@Body priceRequest: PriceRequest): PriceResponse

}