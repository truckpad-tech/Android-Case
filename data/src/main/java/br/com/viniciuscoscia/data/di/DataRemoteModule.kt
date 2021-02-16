package br.com.viniciuscoscia.data.di

import br.com.viniciuscoscia.data.remote.api.TruckPadGeoAPI
import br.com.viniciuscoscia.data.remote.api.TruckPadTicTacAPI
import br.com.viniciuscoscia.data.remote.source.PriceByCargoTypeSourceRemote
import br.com.viniciuscoscia.data.remote.source.PriceByCargoTypeSourceRemoteImpl
import br.com.viniciuscoscia.data.remote.source.RoutesCalcSourceRemote
import br.com.viniciuscoscia.data.remote.source.RoutesCalcSourceRemoteImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val TRUCKPAD_BASE_URL = "https://geo.api.truckpad.io/v1/"
const val TRUCKPAD_TICTAC_BASE_URL = "https://tictac.api.truckpad.io/v1/antt_price/"

val remoteDataSourceModule = module {
    factory { providesOkHttpClient() }
    single {
        createWebService<TruckPadGeoAPI>(
                okHttpClient = get(),
                url = TRUCKPAD_BASE_URL
        )
    }

    single {
        createWebService<TruckPadTicTacAPI>(
                okHttpClient = get(),
                url = TRUCKPAD_TICTAC_BASE_URL
        )
    }

    factory<PriceByCargoTypeSourceRemote> { PriceByCargoTypeSourceRemoteImpl(get()) }
    factory<RoutesCalcSourceRemote> { RoutesCalcSourceRemoteImpl(get()) }
}

fun providesOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
}

inline fun <reified T> createWebService(okHttpClient: OkHttpClient, url: String): T {
    return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(url)
            .client(okHttpClient)
            .build()
            .create(T::class.java)
}
