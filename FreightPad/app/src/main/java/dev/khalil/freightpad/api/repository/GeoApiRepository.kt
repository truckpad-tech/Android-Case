package dev.khalil.freightpad.api.repository

import dev.khalil.freightpad.model.Place
import dev.khalil.freightpad.model.RouteResponse
import io.reactivex.Single

interface GeoApiRepository {
  fun getRoute(
    fuelConsume: Double,
    fuelPrice: Double,
    start: Place,
    destination: Place): Single<RouteResponse>
}