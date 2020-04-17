package dev.khalil.freightpad.di

import dev.khalil.freightpad.BuildConfig.SEARCH_BASE_URL
import dev.khalil.freightpad.di.KodeinTags.SEARCH_RETRO_FIT
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "networkModule"

val networkModule = Kodein.Module(TAG) {

  bind<Retrofit>(SEARCH_RETRO_FIT) with singleton {
    Retrofit.Builder()
      .baseUrl(SEARCH_BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .build()

  }
}