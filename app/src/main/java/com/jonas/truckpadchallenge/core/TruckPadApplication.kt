package com.jonas.truckpadchallenge.core

import android.app.Application
import com.google.android.libraries.places.api.Places
import com.jonas.truckpadchallenge.BuildConfig
import com.jonas.truckpadchallenge.core.di.dataModule
import com.jonas.truckpadchallenge.core.di.domainModule
import com.jonas.truckpadchallenge.core.di.networkModule
import com.jonas.truckpadchallenge.core.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class TruckPadApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initDependencyInjection()
        initGooglePlacesApi()
    }

    private fun initDependencyInjection() {
        startKoin {
            if (BuildConfig.DEBUG) {
                androidLogger(Level.DEBUG)
            }
            androidContext(this@TruckPadApplication)
            modules(listOf(networkModule, dataModule, domainModule, presentationModule))
        }
    }

    private fun initGooglePlacesApi() {
        Places.initialize(applicationContext, BuildConfig.GOOGLE_API_KEY)
        Places.createClient(this)
    }
}