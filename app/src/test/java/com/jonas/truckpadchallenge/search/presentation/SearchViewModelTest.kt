package com.jonas.truckpadchallenge.search.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jonas.truckpadchallenge.core.RxUnitTest
import com.jonas.truckpadchallenge.core.utils.LocationUtils
import com.jonas.truckpadchallenge.search.domain.CalculateRouteUseCase
import com.jonas.truckpadchallenge.search.domain.entities.Points
import com.jonas.truckpadchallenge.search.domain.entities.RouteCalculationInfo
import com.jonas.truckpadchallenge.search.domain.entities.SearchResult
import com.jonas.truckpadchallenge.search.presentation.SearchUiState.Error
import com.jonas.truckpadchallenge.search.presentation.SearchUiState.Success
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Maybe
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchViewModelTest : RxUnitTest() {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: SearchViewModel

    private val useCase: CalculateRouteUseCase = mockk()
    private val locationUtils: LocationUtils = mockk()

    private val routeCalculationInfo = getRouteCalculationInfo()
    private val searchResult = getSearchResult()

    @Before
    fun setup() {
        viewModel = SearchViewModel(useCase, locationUtils)
    }

    @Test
    fun `Should return route information successfully`() {
        every { useCase.execute(routeCalculationInfo) } returns Maybe.just(searchResult)

        viewModel.calculateRoute(routeCalculationInfo)
        val value = viewModel.uiState.value

        assert(value is Success)
        assert((value as Success).searchResult == searchResult)
    }

    @Test
    fun `Should occurs error when requesting route information`() {
        every { useCase.execute(routeCalculationInfo) } returns Maybe.error(Throwable())

        viewModel.calculateRoute(routeCalculationInfo)
        val value = viewModel.uiState.value

        assert(value is Error)
    }

    private fun getRouteCalculationInfo() = RouteCalculationInfo(
        Points(listOf()),
        Points(listOf()),
        7.50,
        3.73
    )

    private fun getSearchResult() = SearchResult(
        1,
        listOf(),
        listOf(),
        0.0,
        "",
        0,
        "",
        false,
        1,
        1,
        "",
        listOf(),
        "",
        false,
        0.0,
        "",
        0.0,
        "",
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0
    )
}