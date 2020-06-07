package com.jonas.truckpadchallenge.search.data

import com.jonas.truckpadchallenge.core.BaseNetworkTest
import com.jonas.truckpadchallenge.core.utils.NetworkUtils
import com.jonas.truckpadchallenge.core.utils.NoInternetConnectionException
import com.jonas.truckpadchallenge.search.data.request.AnttCalculation
import com.jonas.truckpadchallenge.search.domain.entities.AnttResult
import com.jonas.truckpadchallenge.search.domain.entities.Points
import com.jonas.truckpadchallenge.search.domain.entities.RouteCalculationInfo
import com.jonas.truckpadchallenge.search.domain.entities.RoutePoints
import com.jonas.truckpadchallenge.search.domain.entities.RouteResult
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class CalculateRouteRepositoryTest : BaseNetworkTest() {

    private lateinit var repository: CalculateRouteRepository
    private val networkUtils: NetworkUtils = mockk()

    private val routeCalculationInfo = getRouteCalculationInfo()
    private val anttCalculation = getAnttCalculation()

    private val routeResult = getRouteResult()
    private val anttResult = getAnttResult()

    @Before
    fun setup() {
        super.setUp()
        repository = CalculateRouteRepositoryImpl(networkUtils, geoApi, anttApi)
    }

    @Test
    fun `Should return route result successfully`() {
        every { networkUtils.isNetworkAvailable() } returns true

        setSuccessResponse(200)

        repository.calculateRoute(routeCalculationInfo)
            .test()
            .assertNoErrors()
            .assertValue {
                it == routeResult
            }
    }

    @Test
    fun `Should occur an error when requesting the route`() {
        every { networkUtils.isNetworkAvailable() } returns true

        setFailureResponse(404)

        repository.calculateRoute(routeCalculationInfo)
            .test()
            .assertNoValues()
            .assertError(Throwable::class.java)
    }

    @Test
    fun `Should occur internet error when requesting the route`() {
        every { networkUtils.isNetworkAvailable() } returns false

        setFailureResponse(404)

        repository.calculateRoute(routeCalculationInfo)
            .test()
            .assertNoValues()
            .assertError(NoInternetConnectionException::class.java)
    }

    @Test
    fun `Should return antt result successfully`() {
        every { networkUtils.isNetworkAvailable() } returns true

        setSuccessResponse(200)

        repository.calculatePriceByType(anttCalculation)
            .test()
            .assertNoErrors()
            .assertValue {
                it == anttResult
            }
    }

    @Test
    fun `Should occur an error when requesting the antt`() {
        every { networkUtils.isNetworkAvailable() } returns true

        setFailureResponse(404)

        repository.calculatePriceByType(anttCalculation)
            .test()
            .assertNoValues()
            .assertError(Throwable::class.java)
    }

    @Test
    fun `Should occur internet error when requesting the antt`() {
        every { networkUtils.isNetworkAvailable() } returns false

        setFailureResponse(404)

        repository.calculatePriceByType(anttCalculation)
            .test()
            .assertNoValues()
            .assertError(NoInternetConnectionException::class.java)
    }

    private fun getRouteCalculationInfo() = RouteCalculationInfo(
        Points(listOf()),
        Points(listOf()),
        7.50,
        3.73
    )

    private fun getAnttCalculation() = AnttCalculation(
        2,
        0.0,
        false
    )

    private fun getRouteResult() = RouteResult(
        listOf(RoutePoints(listOf(), "")),
        0.0,
        "-",
        0,
        "-",
        false,
        0,
        0,
        "-",
        listOf(),
        "-",
        false,
        0.0,
        "-",
        0.0,
        "-",
        0.0
    )

    private fun getAnttResult() = AnttResult(
        0.0,
        0.0,
        0.0,
        0.0,
        0.0
    )
}