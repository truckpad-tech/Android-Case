package br.com.viniciuscoscia.data.remote.mapper

import br.com.viniciuscoscia.data.remote.model.route.RequestPlace
import br.com.viniciuscoscia.data.remote.model.route.RouteResponse
import br.com.viniciuscoscia.truckpad.domain.entities.Place
import br.com.viniciuscoscia.truckpad.domain.entities.RouteCalc

object RouteCalcsMapper {
    fun map(routeResponse: RouteResponse) = RouteCalc(
        distance = routeResponse.distance,
        distanceUnit = routeResponse.distanceUnit,
        placeOfOrigin = routeResponse.points[0].provider,
        placeOfDestiny = routeResponse.points[1].provider,
        duration = routeResponse.duration,
        durationUnit = routeResponse.durationUnit,
        tollCost = routeResponse.tollCost,
        tollCostUnit = routeResponse.tollCostUnit,
        necessaryFuel = routeResponse.fuelUsage,
        necessaryFuelUnit = routeResponse.fuelUsageUnit,
        totalFuelCost = routeResponse.fuelCost,
        totalFuelCostUnit = routeResponse.fuelCostUnit,
        totalCost = routeResponse.totalCost
    )

    fun coordinateListToRequestPlaceList(places: List<Place>): List<RequestPlace> = places.map {
        RequestPlace(listOf(it.longitude, it.latitude))
    }
}