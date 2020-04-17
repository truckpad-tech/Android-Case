package dev.khalil.freightpad.di

import dev.khalil.freightpad.BuildConfig
import dev.khalil.freightpad.api.repository.SearchLocationRepository
import dev.khalil.freightpad.api.repository.SearchLocationRepositoryImpl
import dev.khalil.freightpad.api.service.SearchLocationService
import dev.khalil.freightpad.ui.viewModel.InfoFragmentViewModel
import dev.khalil.freightpad.ui.viewModel.SearchActivityViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

private const val INFO_MODULE = "infoModule"
private const val SEARCH_MODULE = "infoModule"

val infoModule = Kodein.Module(INFO_MODULE) {
  import(viewModelProviderModule)
  import(networkModule)

  bind<InfoFragmentViewModel>() with provider { InfoFragmentViewModel() }
}

val searchModule = Kodein.Module(SEARCH_MODULE) {
  import(viewModelProviderModule)

  bind<SearchActivityViewModel>() with provider { SearchActivityViewModel(instance()) }
  bind<SearchLocationRepository>() with singleton { SearchLocationRepositoryImpl(instance()) }
  bind<SearchLocationService>() with singleton {
    Retrofit.Builder()
      .baseUrl(BuildConfig.SEARCH_BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .build()
      .create(SearchLocationService::class.java)
  }
}