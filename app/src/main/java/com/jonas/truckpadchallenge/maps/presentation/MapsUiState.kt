package com.jonas.truckpadchallenge.maps.presentation

import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.jonas.truckpadchallenge.maps.presentation.MapsUiState.Error
import com.jonas.truckpadchallenge.maps.presentation.MapsUiState.Success

sealed class MapsUiState {
    data class Success(val location: Location) : MapsUiState()
    data class Error(val error: Throwable) : MapsUiState()
}

fun MutableLiveData<MapsUiState>.toSuccess(location: Location) {
    value = Success(location)
}

fun MutableLiveData<MapsUiState>.toError(error: Throwable) {
    value = Error(error)
}