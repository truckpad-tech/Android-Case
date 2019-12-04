package com.diegobezerra.truckpadcase.data.api.tictac

import com.diegobezerra.truckpadcase.data.api.tictac.TictacService.Companion.BASE_URL
import com.diegobezerra.truckpadcase.domain.model.AnttPrices
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface TictacService {

    companion object {
        const val BASE_URL = "https://tictac.api.truckpad.io/v1/"
    }

    @POST("antt_price/all")
    suspend fun getAnttPrices(@Body body: AnttPricesBody): Response<AnttPrices>

}

fun provideTictacService(): TictacService {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TictacService::class.java)
}

data class AnttPricesBody(
    val axis: Int,
    val distance: Float,
    val has_return_shipment: Boolean = true
)