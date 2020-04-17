package dev.khalil.freightpad.api.service

import dev.khalil.freightpad.model.SearchLocationResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchLocationService {

  @GET("autocomplete")
  fun searchLocation(@Query("search") search: String): Single<SearchLocationResponse>

}