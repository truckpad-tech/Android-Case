package dev.khalil.freightpad.api.service

import dev.khalil.freightpad.model.TictacRequest
import dev.khalil.freightpad.model.TictacResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface TictacApiService {

  @POST("antt_price/all")
  fun getPrices(@Body body: TictacRequest): Single<TictacResponse>
}