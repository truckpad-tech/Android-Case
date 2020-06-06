package com.jonas.truckpadchallenge.core.api

import com.jonas.truckpadchallenge.search.data.request.AnttCalculation
import com.jonas.truckpadchallenge.search.data.response.AnttResponse
import io.reactivex.Maybe
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AnttApi {
    companion object {
        const val BASE_URL_ANTT = "https://tictac.api.truckpad.io/v1/"
    }

    @POST("antt_price/all")
    fun calculatePriceByType(
        @Body priceByType: AnttCalculation
    ): Maybe<Response<AnttResponse>>
}