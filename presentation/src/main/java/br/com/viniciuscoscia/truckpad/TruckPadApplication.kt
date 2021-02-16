package br.com.viniciuscoscia.truckpad

import android.app.Application
import br.com.viniciuscoscia.data.di.dataModules
import br.com.viniciuscoscia.truckpad.di.applicationModule
import br.com.viniciuscoscia.truckpad.domain.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TruckPadApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@TruckPadApplication)
            modules(applicationModule + dataModules + domainModule)
        }
    }
}