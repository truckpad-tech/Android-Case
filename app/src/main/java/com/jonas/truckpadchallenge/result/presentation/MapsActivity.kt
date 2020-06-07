package com.jonas.truckpadchallenge.result.presentation

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.lifecycle.Observer
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.jonas.truckpadchallenge.R
import com.jonas.truckpadchallenge.search.domain.entities.SearchResult
import kotlinx.android.synthetic.main.activity_maps.bottom_sheet_info_input
import kotlinx.android.synthetic.main.activity_maps.map_fragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val viewModel: MapsViewModel by viewModel()

    private lateinit var maps: GoogleMap
    private lateinit var marker: Marker
    private lateinit var searchResult: SearchResult

    companion object {
        private const val LOCATION_REQUEST_CODE = 1
        const val SEARCH_RESULT = "SearchResult"

        fun getIntent(context: Context, searchResult: SearchResult) =
            Intent(context, MapsActivity::class.java).also {
                it.putExtra(SEARCH_RESULT, searchResult)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        (map_fragment as SupportMapFragment).getMapAsync(this)

        setupObservers()
        unpackBundle()
        showFragmentOnBottomSheet()
        listenerUserLocation()
    }

    override fun onResume() {
        super.onResume()

        val behavior = BottomSheetBehavior.from(bottom_sheet_info_input)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun unpackBundle() {
        searchResult = intent.getSerializableExtra(SEARCH_RESULT) as SearchResult
    }

    private fun setupObservers() {
        viewModel.uiState.observe(this, Observer(::updateUI))
    }

    override fun onMapReady(googleMap: GoogleMap) {
        maps = googleMap
        getUserLocation()
    }

    private fun getUserLocation() {
        if (checkLocationPermission()) viewModel.getCurrentLocation()
        else requestPermissions()
    }

    private fun updateUI(state: MapsUiState) {
        when (state) {
            is MapsUiState.Success -> onSuccess(state.location)
            is MapsUiState.Error -> onError(state.error)
        }
    }

    private fun onSuccess(location: Location) {
        val currentLatLng = LatLng(location.latitude, location.longitude)
        placeMarkerOnMap(currentLatLng)
        maps.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
    }

    private fun onError(error: Throwable) {
        TODO("Implements error state")
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

    private fun listenerUserLocation() {
        object : LocationCallback() {
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
                getUserLocation()
            }
        }
    }

    private fun showFragmentOnBottomSheet() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_info_input, ResultFragment.newInstance(searchResult))
            .addToBackStack(null)
            .commit()
    }
}
