package com.jonas.truckpadchallenge.search.data

import com.jonas.truckpadchallenge.search.data.request.AnttCalculation
import com.jonas.truckpadchallenge.search.data.request.Places
import com.jonas.truckpadchallenge.search.data.request.RouteCalculation
import com.jonas.truckpadchallenge.search.data.response.AnttResponse
import com.jonas.truckpadchallenge.search.data.response.RouteResponse
import com.jonas.truckpadchallenge.search.domain.entities.RouteCalculationInfo
import com.jonas.truckpadchallenge.search.data.response.Points
import com.jonas.truckpadchallenge.search.domain.entities.AnttResult
import com.jonas.truckpadchallenge.search.domain.entities.RoutePoints
import com.jonas.truckpadchallenge.search.domain.entities.RouteResult
import com.jonas.truckpadchallenge.search.domain.entities.SearchResult
import com.jonas.truckpadchallenge.search.domain.entities.SearchRoutePoints

object Mapper {

    fun mapperToRouteRequest(routeInfo: RouteCalculationInfo) = RouteCalculation(
        listOf(Places(routeInfo.origin.point), Places(routeInfo.destination.point)),
        routeInfo.fuelConsumption,
        routeInfo.fuelPrice
    )

    fun toRouteResult(routeInfo: RouteResponse?) =
        RouteResult(
            listOf(RoutePoints(toDoubleList(routeInfo?.points), routeInfo?.provider ?: "")),
            routeInfo?.distance ?: 0.0,
            routeInfo?.distanceUnit ?: "-",
            routeInfo?.duration ?: 0,
            routeInfo?.durationUnit ?: "-",
            routeInfo?.hasTolls ?: false,
            routeInfo?.tollCount ?: 0,
            routeInfo?.tollCost ?: 0,
            routeInfo?.tollCostUnit ?: "-",
            routeInfo?.route ?: listOf(),
            routeInfo?.provider ?: "-",
            routeInfo?.cached ?: false,
            routeInfo?.fuelUsage ?: 0.0,
            routeInfo?.fuelUsageUnit ?: "-",
            routeInfo?.fuelCost ?: 0.0,
            routeInfo?.fuelUsageUnit ?: "-",
            routeInfo?.totalCost ?: 0.0
        )

    fun toSearchResult(routeResult: RouteResult, anttResult: AnttResult) = SearchResult(
        listOf(
            SearchRoutePoints(
                routePointstoDoubleList(routeResult.points),
                routeResult.provider
            )
        ),
        routeResult.distance,
        routeResult.distanceUnit,
        routeResult.duration,
        routeResult.durationUnit,
        routeResult.hasTolls,
        routeResult.tollCount,
        routeResult.tollCost,
        routeResult.tollCostUnit,
        routeResult.route,
        routeResult.provider,
        routeResult.cached,
        routeResult.fuelUsage,
        routeResult.fuelUsageUnit,
        routeResult.fuelCost,
        routeResult.fuelUsageUnit,
        routeResult.totalCost,
        anttResult.refrigerated,
        anttResult.general,
        anttResult.bulk,
        anttResult.neogranel,
        anttResult.dangerous
    )

    fun toAnttResult(anttResponse: AnttResponse?) = AnttResult(
        anttResponse?.refrigerated ?: 0.0,
        anttResponse?.general ?: 0.0,
        anttResponse?.bulk ?: 0.0,
        anttResponse?.neogranel ?: 0.0,
        anttResponse?.dangerous ?: 0.0
    )

    fun toAnttCalculation(routeResult: RouteResult) = AnttCalculation(
        2, //TODO review it
        routeResult.distance,
        false //TODO review it
    )

    private fun toDoubleList(points: List<Points>?): List<Double> {
        val list = mutableListOf<Double>()
        points?.forEach { response -> list += response.point }
        return list
    }

    private fun routePointstoDoubleList(points: List<RoutePoints>?): List<Double> {
        val list = mutableListOf<Double>()
        points?.forEach { response -> list += response.point }
        return list
    }
}