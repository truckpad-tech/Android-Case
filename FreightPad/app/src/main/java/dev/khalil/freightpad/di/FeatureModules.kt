package dev.khalil.freightpad.di

import dev.khalil.freightpad.BuildConfig
import dev.khalil.freightpad.api.repository.GeoApiRepository
import dev.khalil.freightpad.api.repository.GeoApiRepositoryImpl
import dev.khalil.freightpad.api.repository.SearchApiRepository
import dev.khalil.freightpad.api.repository.SearchApiRepositoryImpl
import dev.khalil.freightpad.api.repository.TictacApiRepository
import dev.khalil.freightpad.api.repository.TictacApiRepositoryImpl
import dev.khalil.freightpad.api.service.GeoApiService
import dev.khalil.freightpad.api.service.SearchApiService
import dev.khalil.freightpad.api.service.TictacApiService
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
private const val SEARCH_MODULE = "searchModule"
private const val CALCULATE_MODULE = "calculateModule"

val infoModule = Kodein.Module(INFO_MODULE) {
  import(viewModelProviderModule)
  import(calculate)

  bind<InfoFragmentViewModel>() with provider { InfoFragmentViewModel(instance(), instance()) }
}

val calculate = Kodein.Module(CALCULATE_MODULE) {

  bind<GeoApiRepository>() with singleton { GeoApiRepositoryImpl(instance()) }
  bind<GeoApiService>() with singleton {
    Retrofit.Builder()
      .baseUrl(BuildConfig.GEO_API_BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .build()
      .create(GeoApiService::class.java)
  }

  bind<TictacApiRepository>() with singleton { TictacApiRepositoryImpl(instance()) }
  bind<TictacApiService>() with singleton {
    Retrofit.Builder()
      .baseUrl(BuildConfig.TICTAC_API_BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .build()
      .create(TictacApiService::class.java)
  }

}

val searchModule = Kodein.Module(SEARCH_MODULE) {
  import(viewModelProviderModule)

  bind<SearchActivityViewModel>() with provider { SearchActivityViewModel(instance()) }
  bind<SearchApiRepository>() with singleton { SearchApiRepositoryImpl(instance()) }
  bind<SearchApiService>() with singleton {
    Retrofit.Builder()
      .baseUrl(BuildConfig.SEARCH_API_BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .build()
      .create(SearchApiService::class.java)
  }
}
