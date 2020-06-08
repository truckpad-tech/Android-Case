package com.jonas.truckpadchallenge.search.presentation

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jonas.truckpadchallenge.core.utils.LocationUtils
import com.jonas.truckpadchallenge.core.utils.observeOnMain
import com.jonas.truckpadchallenge.core.utils.subscribeOnIO
import com.jonas.truckpadchallenge.result.domain.LocationAddress
import com.jonas.truckpadchallenge.search.domain.CalculateRouteUseCase
import com.jonas.truckpadchallenge.search.domain.entities.RouteCalculationInfo
import io.reactivex.disposables.CompositeDisposable

class SearchViewModel(
    private val useCase: CalculateRouteUseCase,
    private val locationUtils: LocationUtils
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _uiState by lazy { MutableLiveData<SearchUiState>() }
    val uiState: LiveData<SearchUiState> get() = _uiState

    private val _userLocation by lazy { MutableLiveData<LocationAddress>() }
    val userLocation: LiveData<LocationAddress> get() = _userLocation

    fun calculateRoute(routeInfo: RouteCalculationInfo) {
        compositeDisposable.add(
            useCase.execute(routeInfo)
                .subscribeOnIO()
                .observeOnMain()
                .doOnSubscribe { _uiState.toLoading() }
                .subscribe({
                    _uiState.toSuccess(it)

                }, { error ->
                    _uiState.toError(error)
                })
        )
    }

    fun getUserLocation() {
        compositeDisposable.add(
            locationUtils.getCurrentLocation()
                .subscribeOnIO()
                .observeOnMain()
                .doOnSubscribe { _uiState.toLoading() }
                .subscribe({ location ->
                    getAddressByLatLng(location)
                }, { error ->
                    _uiState.toError(error)
                })
        )
    }

    private fun getAddressByLatLng(location: Location) {
        val result = locationUtils.getLocationByLatLng(location.latitude, location.longitude)
        _userLocation.value = result
    }

    fun onPause(){
        _uiState.toEmpty()
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}