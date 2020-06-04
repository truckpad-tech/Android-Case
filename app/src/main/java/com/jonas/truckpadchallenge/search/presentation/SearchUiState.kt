package com.jonas.truckpadchallenge.search.presentation

import androidx.lifecycle.MutableLiveData
import com.jonas.truckpadchallenge.search.presentation.SearchUiState.Error
import com.jonas.truckpadchallenge.search.presentation.SearchUiState.Loading
import com.jonas.truckpadchallenge.search.presentation.SearchUiState.Success

sealed class SearchUiState {
    object Loading : SearchUiState()
    object Success : SearchUiState()
    data class Error(val error: Throwable) : SearchUiState()
}

fun MutableLiveData<SearchUiState>.toLoading() {
    value = Loading
}

fun MutableLiveData<SearchUiState>.toSuccess() {
    value = Success
}

fun MutableLiveData<SearchUiState>.toError(error: Throwable) {
    value = Error(error)
}