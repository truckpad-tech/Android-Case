package br.com.viniciuscoscia.data.remote.source

import br.com.viniciuscoscia.data.remote.api.TruckPadGeoAPI
import br.com.viniciuscoscia.data.remote.model.route.RouteRequest
import br.com.viniciuscoscia.data.remote.model.route.RouteResponse

interface RoutesCalcSourceRemote {
    suspend fun fetchRoute(routeRequest: RouteRequest)
            : RouteResponse
}

class RoutesCalcSourceRemoteImpl(private val truckPadGeoAPI: TruckPadGeoAPI)
    : RoutesCalcSourceRemote {
    override suspend fun fetchRoute(routeRequest: RouteRequest)
            : RouteResponse {
        return truckPadGeoAPI.fetchRoute(routeRequest)
    }
}