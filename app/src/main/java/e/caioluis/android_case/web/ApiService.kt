package e.caioluis.android_case.web

import e.caioluis.android_case.util.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    private fun initRouteRetrofit(): Retrofit {

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_ROUTE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            ).client(client)
            .build()
    }

    val routeService: WepApiService = initRouteRetrofit().create(WepApiService::class.java)

    private fun initPriceRetrofit(): Retrofit {

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_PRICE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            ).client(client)
            .build()
    }

    val priceService: WepApiService = initPriceRetrofit().create(WepApiService::class.java)
}