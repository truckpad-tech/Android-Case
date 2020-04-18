package dev.khalil.freightpad.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.khalil.freightpad.api.repository.GeoApiRepository
import dev.khalil.freightpad.common.DEFAULT_AXIS_VALUE
import dev.khalil.freightpad.common.DESTINATION_LOCATION
import dev.khalil.freightpad.common.MAX_AXIS_VALUE
import dev.khalil.freightpad.common.MIN_AXIS_VALUE
import dev.khalil.freightpad.common.START_LOCATION
import dev.khalil.freightpad.model.Place
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class InfoFragmentViewModel(private val geoRepository: GeoApiRepository) : ViewModel() {

  private val axisMutableLiveData = MutableLiveData<Int>()
  val axis: LiveData<Int>
    get() = axisMutableLiveData

  private val startMutableLiveData = MutableLiveData<Place>()
  val start: LiveData<Place>
    get() = startMutableLiveData

  private val destinationMutableLiveData = MutableLiveData<Place>()
  val destination: LiveData<Place>
    get() = destinationMutableLiveData

  private val compositeDisposable = CompositeDisposable()

  init {
    start()
  }

  private fun start() {
    axisMutableLiveData.value = DEFAULT_AXIS_VALUE
  }

  fun incrementAxis() {
    axisMutableLiveData.value?.let { value ->
      if (value < MAX_AXIS_VALUE) {
        axisMutableLiveData.value = value + 1
      }
    }
  }

  fun decrementAxis() {
    axisMutableLiveData.value?.let { value ->
      if (value > MIN_AXIS_VALUE) {
        axisMutableLiveData.value = value - 1
      }
    }
  }

  fun setLocation(place: Place, to: Int) {
    when (to) {
      START_LOCATION       -> startMutableLiveData.value = place
      DESTINATION_LOCATION -> destinationMutableLiveData.value = place
    }
  }

  fun onDestroy() {

  }

  fun calculate(fuelConsume: Double, fuelPrice: Double) {
    if (fuelConsume > 0 || fuelPrice > 0) {
      compositeDisposable.add(
        geoRepository.getRoute(
          fuelConsume,
          fuelPrice,
          startMutableLiveData.value!!,
          destinationMutableLiveData.value!!)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe({ response ->
            Log.d("TEST", response.toString())
          }, {
            it.printStackTrace()
          })
      )
    }
  }
}