package com.jonas.truckpadchallenge.result.presentation

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.jonas.truckpadchallenge.R
import com.jonas.truckpadchallenge.search.domain.entities.SearchResult
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val viewModel: MapsViewModel by viewModel()

    private lateinit var maps: GoogleMap
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

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        unpackBundle()
        setupObservers()
        showFragmentOnBottomSheet()
    }

    private fun unpackBundle() {
        searchResult = intent.getSerializableExtra(SEARCH_RESULT) as SearchResult
    }

    private fun setupObservers() {
        viewModel.uiState.observe(this, Observer(::updateUI))
    }

    private fun updateUI(state: MapsUiState) {
        when (state) {
            is MapsUiState.Success -> onSuccess(state.location)
            is MapsUiState.Error -> onError()
        }
    }

    private fun onSuccess(location: Location) {
        placeMarkerOnMap(LatLng(location.latitude, location.longitude))
    }

    private fun onError() {
        showErrorDialog()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        maps = googleMap
        maps.setOnMapLoadedCallback {
            getUserLocation()
            drawRoute()
        }
    }

    private fun getUserLocation() {
        if (checkLocationPermission()) viewModel.getCurrentLocation()
        else requestPermissions()
    }

    private fun placeMarkerOnMap(location: LatLng) {
        val markerOptions = MarkerOptions().position(location)
        markerOptions.icon(
            BitmapDescriptorFactory.fromBitmap(
                BitmapFactory.decodeResource(resources, R.drawable.ic_truck_pin)
            )
        )
        maps.addMarker(markerOptions)
    }

    private fun showFragmentOnBottomSheet() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_info_input, ResultFragment.newInstance(searchResult))
            .addToBackStack(null)
            .commit()
    }

    private fun drawRoute() {
        if (::searchResult.isInitialized) {
            val route = searchResult.route[0]
            val bounds = LatLngBounds.Builder()
            val polylineOptions = PolylineOptions()
                .width(12f)
                .color(Color.RED)
                .geodesic(true)

            route.indices.forEach { index ->
                val point = LatLng(route[index][1], route[index][0])
                polylineOptions.add(point)
                bounds.include(point)
            }

            val origin = LatLng(route[0][1], route[0][0])
            val destination = LatLng(route[route.lastIndex][1], route[route.lastIndex][0])

            maps.apply {
                addPolyline(polylineOptions)

                animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 48))

                addMarker(MarkerOptions().position(origin).title(getString(R.string.origin_point_title)))
                addMarker(MarkerOptions().position(destination).title(getString(R.string.destination_point_title)))
            }
        }
    }

    private fun showErrorDialog() {
        AlertDialog.Builder(this).apply {
            setTitle(R.string.attention_dialog_title)
            setMessage(R.string.generic_error_dialog_message)
            setPositiveButton(android.R.string.ok, null)
        }.create().show()
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
}
