package com.jonas.truckpadchallenge.core.utils

import android.content.Context
import android.net.ConnectivityManager

open class NetworkUtils(context: Context) {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)

    open fun isNetworkAvailable(): Boolean {
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }
}
