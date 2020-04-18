package dev.khalil.freightpad.api.service

import dev.khalil.freightpad.model.RouteRequest
import dev.khalil.freightpad.model.RouteResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface GeoApiService {

  @POST("route")
  fun getRoute(@Body routeRequest: RouteRequest): Single<RouteResponse>

}