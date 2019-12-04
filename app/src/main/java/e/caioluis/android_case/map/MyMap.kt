package e.caioluis.android_case.map

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.LocationManager
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import e.caioluis.android_case.R

class MyMap(activity: Activity) : OnMapReadyCallback {

    private var locationManager: LocationManager =
        (activity as Context).getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private var mapFragment =
        (activity as FragmentActivity).supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

    private var actualLatLng: LatLng = LatLng(0.0, 0.0)

    private lateinit var mMap: GoogleMap

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap

        mMap.isMyLocationEnabled = true

        navigateToLocation(getMyLocationLatLng())

        mMap.setOnCameraIdleListener {

            actualLatLng = mMap.cameraPosition.target
        }
    }

    fun getActualLatLng(): LatLng {

        return actualLatLng
    }

    fun startMap() {

        mapFragment.getMapAsync(this)
    }

    fun navigateToLocation(latLng: LatLng) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }

    @SuppressLint("MissingPermission")
    fun getMyLocationLatLng(): LatLng {

        val myLocation = locationManager
            .getLastKnownLocation(LocationManager.GPS_PROVIDER)
            ?: return LatLng(
                -23.533773,
                -46.625290
                // latitude e longitude do centro de São Paulo
            )
        return LatLng(myLocation.latitude, myLocation.longitude)
    }

    fun isActualLocationEmpty(): Boolean {
        if (actualLatLng == LatLng(0.0, 0.0)) {
            return true
        }
        return false
    }
}