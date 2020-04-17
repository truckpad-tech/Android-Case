package dev.khalil.freightpad.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.kodein.di.Kodein
import org.kodein.di.TT
import org.kodein.di.direct

class ViewModelFactory(private val kodein: Kodein) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T =
    kodein.direct.Instance(TT(modelClass))
}