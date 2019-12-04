package e.caioluis.android_case.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class CheckPermission(private val activity: Activity) {

    fun checkGpsPermission(): Boolean {

        return if (!hasGpsPermission()) {
            requestGPSPermission()
            false
        } else true
    }

    private fun requestGPSPermission() {

        val permission = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

        ActivityCompat.requestPermissions(
            activity,
            permission,
            0
        )
    }

    private fun hasGpsPermission(): Boolean {

        return ActivityCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}