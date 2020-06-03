package com.jonas.truckpadchallenge.core

import android.app.Application
import com.google.android.libraries.places.api.Places
import com.jonas.truckpadchallenge.BuildConfig
import com.jonas.truckpadchallenge.core.di.appModules
import org.koin.core.context.startKoin

class TruckPadApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initDependencyInjection()
        initGooglePlacesApi()
    }

    private fun initDependencyInjection() {
        startKoin {
            modules(appModules)
        }
    }

    private fun initGooglePlacesApi() {
        Places.initialize(applicationContext, BuildConfig.GOOGLE_API_KEY)
        Places.createClient(this)
    }
}