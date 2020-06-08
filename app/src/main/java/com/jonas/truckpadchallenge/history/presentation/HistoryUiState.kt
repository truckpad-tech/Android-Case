package com.jonas.truckpadchallenge.history.presentation

import androidx.lifecycle.MutableLiveData
import com.jonas.truckpadchallenge.history.presentation.HistoryUiState.Empty
import com.jonas.truckpadchallenge.history.presentation.HistoryUiState.Error
import com.jonas.truckpadchallenge.history.presentation.HistoryUiState.Loading
import com.jonas.truckpadchallenge.history.presentation.HistoryUiState.Success
import com.jonas.truckpadchallenge.search.domain.entities.SearchResult

sealed class HistoryUiState {
    object Loading : HistoryUiState()
    object Empty : HistoryUiState()
    data class Success(val listSearchResult: List<SearchResult>) : HistoryUiState()
    data class Error(val error: Throwable) : HistoryUiState()
}

fun MutableLiveData<HistoryUiState>.toLoading() {
    value = Loading
}

fun MutableLiveData<HistoryUiState>.toEmpty() {
    value = Empty
}

fun MutableLiveData<HistoryUiState>.toSuccess(searchResult: List<SearchResult>) {
    value = Success(searchResult)
}

fun MutableLiveData<HistoryUiState>.toError(error: Throwable) {
    value = Error(error)
}