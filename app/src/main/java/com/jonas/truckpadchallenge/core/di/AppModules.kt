package com.jonas.truckpadchallenge.core.di

import androidx.lifecycle.ViewModelProvider
import com.jonas.truckpadchallenge.core.api.AnttApi
import com.jonas.truckpadchallenge.core.api.AnttApi.Companion.BASE_URL_ANTT
import com.jonas.truckpadchallenge.core.api.GeoApi
import com.jonas.truckpadchallenge.core.api.GeoApi.Companion.BASE_URL_ROUTE
import com.jonas.truckpadchallenge.core.utils.LocationUtils
import com.jonas.truckpadchallenge.core.utils.NetworkUtils
import com.jonas.truckpadchallenge.maps.presentation.MapsViewModel
import com.jonas.truckpadchallenge.search.data.CalculateRouteRepositoryImpl
import com.jonas.truckpadchallenge.search.data.CalculateRouteRepository
import com.jonas.truckpadchallenge.search.domain.CalculateRouteUseCase
import com.jonas.truckpadchallenge.search.domain.CalculateRouteUseCaseImpl
import com.jonas.truckpadchallenge.search.presentation.SearchViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single { NetworkUtils(get()) }
    factory { provideOkHttpClient() }
    single { provideRetrofitGeo(get()) }
    single { provideRetrofitAntt(get()) }
}

val dataModule = module {
    single { LocationUtils(get()) }
    single<CalculateRouteRepository> { CalculateRouteRepositoryImpl(get(), get(), get()) }
}

val domainModule = module {
    single<CalculateRouteUseCase> { CalculateRouteUseCaseImpl(get()) }
}

val presentationModule = module {
    factory { ViewModelProvider.AndroidViewModelFactory(androidApplication()) }

    viewModel { MapsViewModel(get()) }
    viewModel { SearchViewModel(get(), get()) }
}

fun provideRetrofitGeo(okHttpClient: OkHttpClient): GeoApi {
    return Retrofit.Builder()
        .baseUrl(BASE_URL_ROUTE)
        .client(okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GeoApi::class.java)
}

fun provideRetrofitAntt(okHttpClient: OkHttpClient): AnttApi {
    return Retrofit.Builder()
        .baseUrl(BASE_URL_ANTT)
        .client(okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(AnttApi::class.java)
}

fun provideOkHttpClient(): OkHttpClient = OkHttpClient().newBuilder().build()