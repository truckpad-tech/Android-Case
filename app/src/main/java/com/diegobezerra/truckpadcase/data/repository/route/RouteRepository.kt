package com.diegobezerra.truckpadcase.data.repository.route

import com.diegobezerra.truckpadcase.data.api.geo.GeoService
import com.diegobezerra.truckpadcase.data.api.geo.RouteBody
import com.diegobezerra.truckpadcase.domain.model.RouteInfo
import com.diegobezerra.truckpadcase.util.Result
import com.diegobezerra.truckpadcase.util.safeRequest
import kotlin.math.abs

interface GeoRepository {

    suspend fun getRouteInfo(body: RouteBody): Result<RouteInfo>

}

class MainGeoRepository(
    private val geoService: GeoService
) : GeoRepository {

    private val cache: HashMap<String, RouteInfo> = hashMapOf()

    override suspend fun getRouteInfo(body: RouteBody): Result<RouteInfo> {
        val id = getRouteBodyId(body)
        val cached = cache[id]
        if (cached != null) {
            return Result.Success(cached)
        }
        return when (val result = safeRequest(call = { geoService.getRouteInfo(body) })) {
            is Result.Success -> {
                cache[id] = result.data
                result
            }
            else -> result
        }
    }

    private fun getRouteBodyId(body: RouteBody): String {
        val places = abs(body.places.hashCode())
        return "${places}-${body.fuelConsumption}-${body.fuelPrice}"
    }


}