package com.truckpadcase.calculatefreight.domain.repository

import com.truckpadcase.calculatefreight.data.network.ResultWrapper
import com.truckpadcase.calculatefreight.domain.model.remote.PriceRequest
import com.truckpadcase.calculatefreight.domain.model.remote.PriceResponse
import com.truckpadcase.calculatefreight.domain.model.remote.RouteRequest
import com.truckpadcase.calculatefreight.domain.model.remote.RouteResponse

interface RouteAndPriceServiceRepository {

    suspend fun getRoute(routeRequest: RouteRequest) : ResultWrapper<RouteResponse?>
    suspend fun getPrice(priceRequest: PriceRequest) : ResultWrapper<PriceResponse?>

}