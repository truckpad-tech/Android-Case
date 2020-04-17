package dev.khalil.freightpad.di

import androidx.lifecycle.ViewModelProvider
import dev.khalil.freightpad.utils.ViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

private const val TAG = "viewModelProvider"

val viewModelProviderModule = Kodein.Module(TAG) {
  bind<ViewModelProvider.Factory>() with provider { ViewModelFactory(kodein) }
}