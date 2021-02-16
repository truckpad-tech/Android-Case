package br.com.viniciuscoscia.data

import br.com.viniciuscoscia.data.local.mapper.CalcResultsMapper
import br.com.viniciuscoscia.data.local.source.CalcResultsLocalSource
import br.com.viniciuscoscia.data.remote.mapper.PriceByCargoTypeMapper
import br.com.viniciuscoscia.data.remote.mapper.RouteCalcsMapper
import br.com.viniciuscoscia.data.remote.model.pricebycargotype.PriceByCargoTypeRequest
import br.com.viniciuscoscia.data.remote.model.route.RouteRequest
import br.com.viniciuscoscia.data.remote.source.PriceByCargoTypeSourceRemote
import br.com.viniciuscoscia.data.remote.source.RoutesCalcSourceRemote
import br.com.viniciuscoscia.truckpad.domain.entities.CalcResults
import br.com.viniciuscoscia.truckpad.domain.entities.Place
import br.com.viniciuscoscia.truckpad.domain.entities.PriceByCargoType
import br.com.viniciuscoscia.truckpad.domain.entities.RouteCalc
import br.com.viniciuscoscia.truckpad.domain.repositories.ITruckPadRepository

class TruckPadRepository(
    private val priceByCargoTypeSourceRemote: PriceByCargoTypeSourceRemote,
    private val routesCalcSourceRemote: RoutesCalcSourceRemote,
    private val searchResultsLocalSource: CalcResultsLocalSource
) : ITruckPadRepository {
    override suspend fun fetchRouteCalcs(
        fuelConsumptionKilometersPerLiter: Int,
        fuelPrice: Double,
        places: List<Place>
    ): RouteCalc {
        val routeRequest = RouteRequest(
            fuelConsumption = fuelConsumptionKilometersPerLiter,
            fuelPrice = fuelPrice,
            requestPlaces = RouteCalcsMapper.coordinateListToRequestPlaceList(places)
        )

        return RouteCalcsMapper.map(routesCalcSourceRemote.fetchRoute(routeRequest))
    }

    override suspend fun fetchPricesByCargoType(
        axis: Int,
        distanceMeters: Double,
        hasReturnShipment: Boolean
    ): PriceByCargoType {
        val priceByCargoTypeRequest = PriceByCargoTypeRequest(
            axis,
            distanceMeters,
            hasReturnShipment
        )

        return PriceByCargoTypeMapper
            .map(priceByCargoTypeSourceRemote.fetchPricesByTruckCargoType(priceByCargoTypeRequest))
    }

    override suspend fun saveCalcResults(calcResults: CalcResults) {
        searchResultsLocalSource.insertData(CalcResultsMapper.map(calcResults))
    }

    override suspend fun getCalcResultsHistory(): List<CalcResults> {
        return CalcResultsMapper.mapCalcResultsEntity(searchResultsLocalSource.getCalcResults())
    }
}