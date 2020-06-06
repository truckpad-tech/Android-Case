package com.jonas.truckpadchallenge.search.domain

import com.jonas.truckpadchallenge.search.domain.entities.RouteCalculationInfo
import com.jonas.truckpadchallenge.search.domain.entities.SearchResult
import io.reactivex.Maybe

interface CalculateRouteUseCase {
    fun execute(routeInfo: RouteCalculationInfo): Maybe<SearchResult>
}