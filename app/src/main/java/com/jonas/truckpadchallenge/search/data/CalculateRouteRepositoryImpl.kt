package com.jonas.truckpadchallenge.search.data

import com.jonas.truckpadchallenge.core.api.AnttApi
import com.jonas.truckpadchallenge.core.api.GeoApi
import com.jonas.truckpadchallenge.core.utils.NetworkUtils
import com.jonas.truckpadchallenge.core.utils.NoInternetConnectionException
import com.jonas.truckpadchallenge.search.data.Mapper.mapperToRouteRequest
import com.jonas.truckpadchallenge.search.data.Mapper.toAnttResult
import com.jonas.truckpadchallenge.search.data.Mapper.toRouteResult
import com.jonas.truckpadchallenge.search.data.request.AnttCalculation
import com.jonas.truckpadchallenge.search.domain.entities.AnttResult
import com.jonas.truckpadchallenge.search.domain.entities.RouteCalculationInfo
import com.jonas.truckpadchallenge.search.domain.entities.RouteResult
import io.reactivex.Maybe

class CalculateRouteRepositoryImpl(
    private val networkUtils: NetworkUtils,
    private val geoApi: GeoApi,
    private val anttApi: AnttApi
) : CalculateRouteRepository {

    override fun calculateRoute(routeInfo: RouteCalculationInfo): Maybe<RouteResult> {
        return if (networkUtils.isNetworkAvailable()) {
            geoApi.calculateRoute(mapperToRouteRequest(routeInfo))
                .flatMap { response ->
                    if (response.isSuccessful) {
                        val routeResult = response.body()

                        if (routeResult != null)
                            Maybe.just(toRouteResult(routeResult))
                        else
                            Maybe.empty()
                    } else {
                        Maybe.error(Throwable("Occurs an error"))
                    }
                }
        } else {
            Maybe.error(NoInternetConnectionException())
        }
    }

    override fun calculatePriceByType(anttInfo: AnttCalculation): Maybe<AnttResult> {
        return if (networkUtils.isNetworkAvailable()) {
            anttApi.calculatePriceByType(anttInfo)
                .flatMap { response ->
                    if (response.isSuccessful) {
                        val anttResponse = response.body()

                        if (anttResponse != null)
                            Maybe.just(toAnttResult(anttResponse))
                        else
                            Maybe.empty()
                    } else {
                        Maybe.error(Throwable("It was not possible to request the price by type"))
                    }
                }
        } else {
            Maybe.error(NoInternetConnectionException())
        }
    }
}