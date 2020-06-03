package com.jonas.truckpadchallenge.maps.view

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.core.app.ActivityCompat.requestPermissions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.jonas.truckpadchallenge.R
import kotlinx.android.synthetic.main.activity_maps.map_fragment

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var maps: GoogleMap
    private lateinit var marker: Marker
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var locationUpdateState = false
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    companion object {
        private const val LOCATION_REQUEST_CODE = 1
        private const val FIVE_SECONDS: Long = 5000
        private const val THREE_SECONDS: Long = 3000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        (map_fragment as SupportMapFragment).getMapAsync(this)

        createLocationRequest()
        listenerUserLocation()
        showFragmentOnBottomSheet()
    }

    public override fun onResume() {
        super.onResume()
        if (!locationUpdateState) startLocationUpdates()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        maps = googleMap
        getUserLocation()
    }

    private fun getUserLocation() {
        if (checkLocationPermission())
            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                location.let {
                    val currentLatLng = LatLng(it.latitude, it.longitude)
                    placeMarkerOnMap(currentLatLng)
                    maps.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                }
            }
        else requestPermissions()
    }

    private fun placeMarkerOnMap(location: LatLng) {
        val markerOptions = MarkerOptions().position(location)
        markerOptions.icon(
            BitmapDescriptorFactory.fromBitmap(
                BitmapFactory.decodeResource(resources, R.drawable.ic_truck_pin)
            )
        )
        if (::marker.isInitialized) marker.remove()
        marker = maps.addMarker(markerOptions)
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest().apply {
            interval = FIVE_SECONDS
            fastestInterval = THREE_SECONDS
            priority = PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())

        with(task) {
            addOnSuccessListener {
                locationUpdateState = true
                startLocationUpdates()
            }
            addOnFailureListener {
                // TODO handle this
            }
        }
    }

    private fun startLocationUpdates() {
        if (checkLocationPermission()) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null
            )
        } else {
            requestPermissions()
        }
    }

    private fun listenerUserLocation() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(location: LocationResult) {
                super.onLocationResult(location)

                placeMarkerOnMap(
                    LatLng(
                        location.lastLocation.latitude,
                        location.lastLocation.longitude
                    )
                )
            }
        }
    }

    private fun checkLocationPermission() =
        checkSelfPermission(this, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            LOCATION_REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
                startLocationUpdates()
                getUserLocation()
            }
        }
    }

    private fun showFragmentOnBottomSheet() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_info_input, InfoInputFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
        locationUpdateState = false
    }
}
