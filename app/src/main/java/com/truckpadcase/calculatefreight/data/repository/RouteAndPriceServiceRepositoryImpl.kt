package com.truckpadcase.calculatefreight.data.repository

import com.squareup.moshi.Moshi
import com.truckpadcase.calculatefreight.data.network.ApiService
import com.truckpadcase.calculatefreight.data.network.ErrorResponse
import com.truckpadcase.calculatefreight.data.network.ResultWrapper
import com.truckpadcase.calculatefreight.domain.model.remote.PriceRequest
import com.truckpadcase.calculatefreight.domain.model.remote.PriceResponse
import com.truckpadcase.calculatefreight.domain.model.remote.RouteRequest
import com.truckpadcase.calculatefreight.domain.model.remote.RouteResponse
import com.truckpadcase.calculatefreight.domain.repository.RouteAndPriceServiceRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class RouteAndPriceServiceRepositoryImpl: RouteAndPriceServiceRepository {

    override suspend fun getRoute( routeRequest: RouteRequest): ResultWrapper<RouteResponse?> {
        return safeApiCall(dispatcher = Dispatchers.IO) { ApiService.ROUTE_SERVICE.routePost(routeRequest)}
    }

    override suspend fun getPrice(priceRequest: PriceRequest): ResultWrapper<PriceResponse?> {
        return safeApiCall(dispatcher = Dispatchers.IO) { ApiService.PRICE_SERVICE.pricePost(priceRequest)}
    }

    suspend fun <T> safeApiCall(dispatcher: CoroutineDispatcher, apiCall: suspend () -> T): ResultWrapper<T> {
        return withContext(dispatcher) {
            try {
                ResultWrapper.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> ResultWrapper.NetworkError
                    is HttpException -> {
                        val code = throwable.code()
                        val errorResponse = convertErrorBody(throwable)
                        ResultWrapper.GenericError(code, errorResponse)
                    }
                    else -> {
                        ResultWrapper.GenericError(null, null)
                    }
                }
            }
        }
    }

    private fun convertErrorBody(throwable: HttpException): ErrorResponse? {
        return try {
            throwable.response()?.errorBody()?.source()?.let {
                val moshiAdapter =  Moshi.Builder().build().adapter(ErrorResponse::class.java)
                moshiAdapter.fromJson(it)
            }
        } catch (exception: Exception) {
            null
        }
    }

}