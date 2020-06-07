package com.jonas.truckpadchallenge.core

import com.jonas.truckpadchallenge.core.api.AnttApi
import com.jonas.truckpadchallenge.core.api.GeoApi
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

abstract class BaseNetworkTest {

    lateinit var geoApi: GeoApi
    lateinit var anttApi: AnttApi

    private val mockServer = MockWebServer()

    @Before
    open fun setUp() {
        val okHttpClient = OkHttpClient.Builder()
            .build()

        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(mockServer.url("/").toString())
            .build()

        geoApi = retrofit.create(GeoApi::class.java)
        anttApi = retrofit.create(AnttApi::class.java)
    }

    fun setSuccessResponse(httpCode: Int) {
        val mockedResponse = MockResponse()
        mockedResponse.setResponseCode(httpCode)
        mockedResponse.setBody("{}")
        mockServer.enqueue(mockedResponse)
    }

    fun setFailureResponse(errorCode: Int) {
        val mockedResponse = MockResponse()
        mockedResponse.setResponseCode(errorCode)
        mockedResponse.setBody("")

        mockServer.enqueue(mockedResponse)
    }
}