package br.com.viniciuscoscia.truckpad.domain.repositories

import br.com.viniciuscoscia.truckpad.domain.entities.CalcResults
import br.com.viniciuscoscia.truckpad.domain.entities.Place
import br.com.viniciuscoscia.truckpad.domain.entities.PriceByCargoType
import br.com.viniciuscoscia.truckpad.domain.entities.RouteCalc

interface ITruckPadRepository {
    suspend fun fetchRouteCalcs(fuelConsumptionKilometersPerLiter: Int,
                                fuelPrice: Double,
                                places: List<Place>
    ): RouteCalc

    suspend fun fetchPricesByCargoType(axis: Int,
                                       distanceMeters: Double,
                                       hasReturnShipment: Boolean): PriceByCargoType

    suspend fun saveCalcResults(calcResults: CalcResults)

    suspend fun getCalcResultsHistory(): List<CalcResults>
}