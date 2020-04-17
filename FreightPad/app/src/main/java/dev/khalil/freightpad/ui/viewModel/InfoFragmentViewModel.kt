package dev.khalil.freightpad.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.khalil.freightpad.common.MAX_AXIS_VALUE
import dev.khalil.freightpad.common.MIN_AXIS_VALUE

class InfoFragmentViewModel : ViewModel() {

  private val axisMutableLiveData = MutableLiveData<Int>()
  val axis: LiveData<Int>
    get() = axisMutableLiveData

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

}