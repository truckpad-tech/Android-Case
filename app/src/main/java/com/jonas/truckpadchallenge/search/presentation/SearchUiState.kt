package com.jonas.truckpadchallenge.search.presentation

import androidx.lifecycle.MutableLiveData
import com.jonas.truckpadchallenge.search.domain.entities.SearchResult
import com.jonas.truckpadchallenge.search.presentation.SearchUiState.Empty
import com.jonas.truckpadchallenge.search.presentation.SearchUiState.Error
import com.jonas.truckpadchallenge.search.presentation.SearchUiState.Loading
import com.jonas.truckpadchallenge.search.presentation.SearchUiState.Success

sealed class SearchUiState {
    object Loading : SearchUiState()
    object Empty : SearchUiState()
    data class Success(val searchResult: SearchResult) : SearchUiState()
    data class Error(val error: Throwable) : SearchUiState()
}

fun MutableLiveData<SearchUiState>.toLoading() {
    value = Loading
}

fun MutableLiveData<SearchUiState>.toEmpty() {
    value = Empty
}

fun MutableLiveData<SearchUiState>.toSuccess(searchResult: SearchResult) {
    value = Success(searchResult)
}

fun MutableLiveData<SearchUiState>.toError(error: Throwable) {
    value = Error(error)
}