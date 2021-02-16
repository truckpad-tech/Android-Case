package br.com.viniciuscoscia.data.remote.api

import br.com.viniciuscoscia.data.remote.model.route.RouteRequest
import br.com.viniciuscoscia.data.remote.model.route.RouteResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface TruckPadGeoAPI {

    @POST(ROUTE_ENDPOINT)
    suspend fun fetchRoute(
        @Body routeRequest: RouteRequest
    ): RouteResponse

    companion object {
        private const val ROUTE_ENDPOINT = "route"
    }
}