package com.jonas.truckpadchallenge.result.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jonas.truckpadchallenge.core.utils.LocationUtils
import com.jonas.truckpadchallenge.core.utils.observeOnMain
import com.jonas.truckpadchallenge.core.utils.subscribeOnIO
import io.reactivex.disposables.CompositeDisposable

class MapsViewModel(private val locationUtils: LocationUtils) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _uiState by lazy { MutableLiveData<MapsUiState>() }
    val uiState: LiveData<MapsUiState> get() = _uiState

    fun getCurrentLocation() {
        compositeDisposable.add(
            locationUtils.getCurrentLocation()
                .subscribeOnIO()
                .observeOnMain()
                .subscribe({ location ->
                    _uiState.toSuccess(location)
                }, {
                    _uiState.toError(it)
                })
        )
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}