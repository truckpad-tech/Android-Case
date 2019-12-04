package e.caioluis.android_case.util

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import e.caioluis.android_case.R

class CheckPermission(private val activity: Activity) {

    private var willShowDialog = true
    private val builder = AlertDialog.Builder(activity)

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

    fun showPermissionAlert() {

        if (willShowDialog) {

            with(builder) {

                setTitle(context.getString(R.string.alert_title_warning))
                setMessage(context.getString(R.string.alert_message_permission_denied))
                setCancelable(true)
                setPositiveButton(android.R.string.yes) { _, _ ->
                    willShowDialog = !checkGpsPermission()
                }

                setNegativeButton(context.getString(R.string.label_exit)) { _, _ ->

                    activity.finish()
                }

                show()
            }
        }
    }
}