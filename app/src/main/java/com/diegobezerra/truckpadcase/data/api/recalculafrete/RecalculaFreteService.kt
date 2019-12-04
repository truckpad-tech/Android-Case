package com.diegobezerra.truckpadcase.data.api.recalculafrete

import com.diegobezerra.truckpadcase.data.api.recalculafrete.RecalculaFreteService.Companion.BASE_URL
import com.diegobezerra.truckpadcase.domain.model.Place
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RecalculaFreteService {

    companion object {
        const val BASE_URL = "https://api.recalculafrete.com.br/"
    }

    @GET("autocomplete")
    suspend fun autocomplete(
        @Query("search") search: String
    ): Response<AutocompleteResponse>

}

fun provideRecalculaFreteService(): RecalculaFreteService {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RecalculaFreteService::class.java)
}

data class AutocompleteResponse(
    val places: List<Place>,
    val provider: String
)