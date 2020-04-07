package com.truckpadcase.calculatefreight.presentation.views

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.truckpadcase.calculatefreight.R
import com.truckpadcase.calculatefreight.presentation.viewmodels.ResultSearchViewModelImpl
import com.truckpadcase.calculatefreight.presentation.views.base.BaseActivity
import com.truckpadcase.calculatefreight.utils.Constants
import com.truckpadcase.calculatefreight.utils.UnitConversion.Companion.metersToKm
import com.truckpadcase.calculatefreight.utils.UnitConversion.Companion.replacePrices
import com.truckpadcase.calculatefreight.utils.UnitConversion.Companion.secondsToHours
import kotlinx.android.synthetic.main.activity_result_search.*


class ResultSearchActivity : BaseActivity(), OnMapReadyCallback {

    private val viewModelImpl: ResultSearchViewModelImpl by lazy {
        ViewModelProviders.of(this).get(ResultSearchViewModelImpl::class.java)
    }

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_search)

        setupToolbar(toolbar_show_results_route, R.string.results, true)

        prepareMap()

        viewModelImpl.searchData(intent.getLongExtra("Search-result-id", Long.MIN_VALUE)).observe(this, Observer { freightData ->
            if (freightData != null ){

                origin_result_fiel.text = freightData.initial_address
                destination_result_fiel.text = freightData.final_address
                axes_result_fiel.text = freightData.axes.toString()
                distance_result_fiel.text = metersToKm(freightData.distance.toString())
                duration_result_field.text = secondsToHours(freightData.duration.toString())
                tools_result_fiel.text = freightData.toll_cost.toString()
                fuel_usage_result_fiel.text = freightData.fuel_usage.toString()+" Litros"
                fuel_cost_result_fiel.text = "R$: "+freightData.fuel_cost.toString()
                refrigerated_result_fiel.text = replacePrices(freightData.refrigerated.toString())
                general_result_fiel.text = replacePrices(freightData.general.toString())
                granel_result_fiel.text = replacePrices(freightData.granel.toString())
                neogranel_result_fiel.text = replacePrices(freightData.neogranel.toString())
                hazardous_result_fiel.text = replacePrices(freightData.hazardous.toString())

                drawMapRoute(freightData.route[0])
            }

        })

    }

    private fun drawMapRoute(list: List<List<Double>>) {

        val options : PolylineOptions  = PolylineOptions().width(8.0F).color(Color.BLACK).geodesic(true)
        val boundsBuilder = LatLngBounds.Builder()
        for (z in list.indices) {
            val point = LatLng(list[z][1], list[z][0])
            options.add(point)
            boundsBuilder.include(point)
        }

        val routePadding = 0
        val latLngBounds = boundsBuilder.build()
        mMap.addPolyline(options)
        mMap.setPadding(80, 180, 80, 350)
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding))

        val location_Origin = LatLng(list[0][1],list[0][0])

        val location_destiny = LatLng(list[list.lastIndex][1],list[list.lastIndex][0])

        mMap.addMarker(MarkerOptions().position(location_Origin))
        mMap.addMarker(MarkerOptions().position(location_destiny))

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        setUpMap()

    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                Constants.LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
    }

    private fun prepareMap(){

        // Obtain the  and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_result) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    companion object {
        fun getStartIntent(context: Context) : Intent {
            return Intent(context, ResultSearchActivity::class.java).apply {}
        }
    }
}
