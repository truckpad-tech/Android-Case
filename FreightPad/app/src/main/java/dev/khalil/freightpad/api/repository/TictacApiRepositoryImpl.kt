package dev.khalil.freightpad.api.repository

import dev.khalil.freightpad.api.service.TictacApiService
import dev.khalil.freightpad.model.TictacRequest
import dev.khalil.freightpad.model.TictacResponse
import io.reactivex.Single

class TictacApiRepositoryImpl(private val api: TictacApiService) : TictacApiRepository {

  override fun getPrices(
    axis: Int,
    distance: Double,
    hasReturnShipment: Boolean): Single<TictacResponse> {
    return api.getPrices(TictacRequest(axis, distance, hasReturnShipment))
  }
}
