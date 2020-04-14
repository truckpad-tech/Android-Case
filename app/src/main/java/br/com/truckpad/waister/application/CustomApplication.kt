package br.com.truckpad.waister.application

import android.app.Application
import com.orhanobut.hawk.Hawk

class CustomApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Hawk.init(this).build()
    }
}
