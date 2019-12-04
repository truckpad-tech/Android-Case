package com.diegobezerra.truckpadcase.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.diegobezerra.truckpadcase.R
import com.diegobezerra.truckpadcase.databinding.ActivityMainBinding
import com.diegobezerra.truckpadcase.domain.model.toLatLng
import com.diegobezerra.truckpadcase.ui.main.calculator.CalculatorFragment
import com.diegobezerra.truckpadcase.util.EventObserver
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        const val RC_LOCATION_PERM = 1
        const val MAPVIEW_BUNDLE_KEY = "key.MAP_BUNDLE"
    }

    private val mainViewModel: MainViewModel by viewModel()
    private lateinit var calculatorFragment: CalculatorFragment

    private lateinit var map: GoogleMap
    private lateinit var mapView: MapView

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
                .apply {
                    lifecycleOwner = this@MainActivity
                    viewModel = mainViewModel
                }
        mapView = binding.map

        supportFragmentManager.findFragmentById(R.id.calculator_sheet)?.let {
            calculatorFragment = it as CalculatorFragment
        }


        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
        }
        mapView.onCreate(mapViewBundle)
        mapView.getMapAsync(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            outState.putBundle(MAPVIEW_BUNDLE_KEY, Bundle())
        }
        mapView.onSaveInstanceState(mapViewBundle)
    }

    override fun onBackPressed() {
        if (::calculatorFragment.isInitialized &&
            calculatorFragment.onBackPressed()
        ) {
            return
        }
        super.onBackPressed()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode != RC_LOCATION_PERM) {
            // Unexpected permission result
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }

        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation()
        }
    }

    private fun requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            ActivityCompat.requestPermissions(this, permissions, RC_LOCATION_PERM)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        mainViewModel.showRouteAction.observe(this, EventObserver {
            if (it.route.isNotEmpty()) {
                drawRoute(googleMap, it.route[0])
            }
        })

        mainViewModel.useCurrentLocationAction.observe(this, EventObserver {
            if (isLocationPermissionGranted()) {
                getCurrentLocation()
                return@EventObserver
            }
            requestLocationPermission()
        })

    }

    private fun drawRoute(map: GoogleMap, route: List<List<Double>>) {
        map.clear()
        if (route.isEmpty()) {
            return
        }

        val bounds = LatLngBounds.Builder()
        val opts = PolylineOptions().apply {
            width(6f)
            color(Color.RED)
            geodesic(true)
        }
        route.forEach {
            val latLng = it.toLatLng()
            opts.add(latLng)
            bounds.include(latLng)
        }
        map.addPolyline(opts)

        // Animate camera to bounds
        val origin = route.first().toLatLng()
        val destination = if (route.size == 1) {
            origin
        } else {
            route.last().toLatLng()
        }
        if (origin != null && destination != null) {
            listOf(origin, destination).forEach {
                map.addMarker(MarkerOptions().position(it))
            }
        }
        val sheetHeight = resources.getDimension(R.dimen.calculator_sheet_peek_height)
        val toolbarHeight = toolbar.height
        val offset = (sheetHeight + toolbarHeight)
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), offset.toInt()))
    }

    private fun getCurrentLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    mainViewModel.onGetLocation(
                        getString(R.string.text_current_location),
                        listOf(it.longitude, it.latitude)
                    )
                }
            }
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}
