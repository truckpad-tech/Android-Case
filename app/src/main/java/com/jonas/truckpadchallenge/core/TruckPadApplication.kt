package com.jonas.truckpadchallenge.core

import android.app.Application
import com.jonas.truckpadchallenge.core.di.appModules
import org.koin.core.context.startKoin

class TruckPadApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initDependencyInjection()
    }

    private fun initDependencyInjection() {
        startKoin {
            modules(appModules)
        }
    }
}