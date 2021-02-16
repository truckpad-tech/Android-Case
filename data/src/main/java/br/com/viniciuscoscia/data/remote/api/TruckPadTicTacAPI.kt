package br.com.viniciuscoscia.data.remote.api

import br.com.viniciuscoscia.data.remote.model.pricebycargotype.PriceByCargoTypeRequest
import br.com.viniciuscoscia.data.remote.model.pricebycargotype.PriceByCargoTypeResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface TruckPadTicTacAPI {

    @POST(VALUES_BY_TRUCK_CARGO_TYPE_ENDPOINT)
    suspend fun fetchPricesByTruckCargoType(
        @Body priceByCargoTypeRequest: PriceByCargoTypeRequest
    ): PriceByCargoTypeResponse

    companion object {
        const val VALUES_BY_TRUCK_CARGO_TYPE_ENDPOINT = "all"
    }
}