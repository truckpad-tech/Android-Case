package dev.khalil.freightpad.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.khalil.freightpad.api.repository.GeoApiRepository
import dev.khalil.freightpad.api.repository.TictacApiRepository
import dev.khalil.freightpad.common.DEFAULT_AXIS_VALUE
import dev.khalil.freightpad.common.DESTINATION_LOCATION
import dev.khalil.freightpad.common.MAX_AXIS_VALUE
import dev.khalil.freightpad.common.MIN_AXIS_VALUE
import dev.khalil.freightpad.common.START_LOCATION
import dev.khalil.freightpad.extensions.toKm
import dev.khalil.freightpad.model.Place
import dev.khalil.freightpad.model.RouteResponse
import dev.khalil.freightpad.model.RouteUiModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class InfoFragmentViewModel(
  private val geoRepository: GeoApiRepository,
  private val tictacRepository: TictacApiRepository) : ViewModel() {

  private val axisMutableLiveData = MutableLiveData<Int>()
  val axis: LiveData<Int>
    get() = axisMutableLiveData

  private val startMutableLiveData = MutableLiveData<Place>()
  val start: LiveData<Place>
    get() = startMutableLiveData

  private val destinationMutableLiveData = MutableLiveData<Place>()
  val destination: LiveData<Place>
    get() = destinationMutableLiveData

  private val routeMutableLiveData = MutableLiveData<RouteUiModel>()
  val route: LiveData<RouteUiModel>
    get() = routeMutableLiveData

  private val compositeDisposable = CompositeDisposable()

  init {
    start()
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

  fun calculate(fuelConsume: Double, fuelPrice: Double) {

    val startPlace = startMutableLiveData.value
    val destinationPlace = destinationMutableLiveData.value
    val axisValue = axisMutableLiveData.value

    if (fuelConsume > 0 && fuelPrice > 0 && startPlace != null && destinationPlace != null) {
      compositeDisposable.add(
        geoRepository.getRoute(
          fuelConsume,
          fuelPrice,
          startPlace,
          destinationPlace)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe({ routeResponse ->
            if (axisValue != null) {
              calculatePrices(
                routeResponse,
                startPlace,
                destinationPlace,
                axisValue,
                fuelConsume,
                fuelPrice)
            }
          }, {
            it.printStackTrace()
          })
      )
    }
  }

  private fun calculatePrices(
    routeResponse: RouteResponse,
    startPlace: Place,
    destinationPlace: Place,
    axisValue: Int,
    fuelConsume: Double,
    fuelPrice: Double
  ) {
    compositeDisposable.add(
      tictacRepository.getPrices(axisValue, routeResponse.distance.toKm())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ tictacResponse ->

          val routeUiModel =
            RouteUiModel.toRouteUiModel(
              routeResponse,
              tictacResponse,
              startPlace.displayName,
              destinationPlace.displayName,
              axisValue,
              fuelConsume,
              fuelPrice)

          routeMutableLiveData.value = routeUiModel

        }, {
          it.printStackTrace()
        })
    )
  }

  fun onDestroy() {

  }

  private fun start() {
    axisMutableLiveData.value = DEFAULT_AXIS_VALUE
  }
}

