package br.com.viniciuscoscia.data.remote.source

import br.com.viniciuscoscia.data.remote.api.TruckPadTicTacAPI
import br.com.viniciuscoscia.data.remote.model.pricebycargotype.PriceByCargoTypeRequest
import br.com.viniciuscoscia.data.remote.model.pricebycargotype.PriceByCargoTypeResponse

interface PriceByCargoTypeSourceRemote {
    suspend fun fetchPricesByTruckCargoType(priceByCargoTypeRequest: PriceByCargoTypeRequest)
            : PriceByCargoTypeResponse
}

class PriceByCargoTypeSourceRemoteImpl(private val truckPadTicTacAPI: TruckPadTicTacAPI)
    : PriceByCargoTypeSourceRemote {
    override suspend fun fetchPricesByTruckCargoType(priceByCargoTypeRequest: PriceByCargoTypeRequest)
            : PriceByCargoTypeResponse {
        return truckPadTicTacAPI.fetchPricesByTruckCargoType(priceByCargoTypeRequest)
    }
}