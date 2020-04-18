package dev.khalil.freightpad.api.repository

import dev.khalil.freightpad.model.TictacResponse
import io.reactivex.Single

interface TictacApiRepository {

  fun getPrices(
    axis: Int,
    distance: Double,
    hasReturnShipment: Boolean = true): Single<TictacResponse>
}
