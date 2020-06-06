package com.jonas.truckpadchallenge.search.data

import com.jonas.truckpadchallenge.search.data.request.AnttCalculation
import com.jonas.truckpadchallenge.search.domain.entities.AnttResult
import com.jonas.truckpadchallenge.search.domain.entities.RouteCalculationInfo
import com.jonas.truckpadchallenge.search.domain.entities.RouteResult
import io.reactivex.Maybe

interface CalculateRouteRepository {
    fun calculateRoute(routeInfo: RouteCalculationInfo): Maybe<RouteResult>
    fun calculatePriceByType(anttInfo: AnttCalculation): Maybe<AnttResult>
}