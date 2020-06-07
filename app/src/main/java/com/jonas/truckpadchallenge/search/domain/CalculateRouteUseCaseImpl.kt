package com.jonas.truckpadchallenge.search.domain

import com.jonas.truckpadchallenge.search.data.CalculateRouteRepository
import com.jonas.truckpadchallenge.search.data.Mapper.toAnttCalculation
import com.jonas.truckpadchallenge.search.data.Mapper.toSearchResult
import com.jonas.truckpadchallenge.search.domain.entities.RouteCalculationInfo
import com.jonas.truckpadchallenge.search.domain.entities.RouteResult
import com.jonas.truckpadchallenge.search.domain.entities.SearchResult
import io.reactivex.Maybe

class CalculateRouteUseCaseImpl(private val repository: CalculateRouteRepository) :
    CalculateRouteUseCase {

    override fun execute(routeInfo: RouteCalculationInfo): Maybe<SearchResult> {
        return repository.calculateRoute(routeInfo)
            .flatMap { result ->
                getPriceByType(result)
            }.doOnError { error ->
                Maybe.error<Throwable>(error)
            }
    }

    private fun getPriceByType(routeResult: RouteResult): Maybe<SearchResult> {
        return repository.calculatePriceByType(toAnttCalculation(routeResult))
            .flatMap { result ->
                Maybe.just(toSearchResult(routeResult, result))
            }.doOnError { error ->
                Maybe.error<Throwable>(error)
            }
    }
}