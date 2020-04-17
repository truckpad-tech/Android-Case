package dev.khalil.freightpad.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.khalil.freightpad.common.DESTINATION_LOCATION
import dev.khalil.freightpad.common.MAX_AXIS_VALUE
import dev.khalil.freightpad.common.MIN_AXIS_VALUE
import dev.khalil.freightpad.common.START_LOCATION
import dev.khalil.freightpad.model.Place

class InfoFragmentViewModel : ViewModel() {

  private val axisMutableLiveData = MutableLiveData<Int>()
  val axis: LiveData<Int>
    get() = axisMutableLiveData

  private val startMutableLiveData = MutableLiveData<Place>()
  val start: LiveData<Place>
    get() = startMutableLiveData

  private val destinationMutableLiveData = MutableLiveData<Place>()
  val destination: LiveData<Place>
    get() = destinationMutableLiveData

  init {
    start()
  }

  private fun start() {
    axisMutableLiveData.value = 2
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

}