package com.jonas.truckpadchallenge.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jonas.truckpadchallenge.search.domain.RouteCalculationInfo

class SearchViewModel : ViewModel() {

    private val _uiState by lazy { MutableLiveData<SearchUiState>() }
    val uiState: LiveData<SearchUiState> get() = _uiState

    fun calculateRoute(routeInfo: RouteCalculationInfo) {

    }

    override fun onCleared() {
        super.onCleared()
    }
}