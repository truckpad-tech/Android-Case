package dev.khalil.freightpad.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLngBounds.Builder
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import dev.khalil.freightpad.R
import dev.khalil.freightpad.common.BRAZIL_BOUNDS
import dev.khalil.freightpad.common.BRAZIL_GEO_LOCATION
import dev.khalil.freightpad.common.ZERO
import dev.khalil.freightpad.databinding.ActivityMapsBinding
import dev.khalil.freightpad.extensions.toLatLng
import dev.khalil.freightpad.ui.fragment.CalculatorFragment

class MapsActivity : FragmentActivity(), OnMapReadyCallback, OnShowRoute {

  private lateinit var googleMap: GoogleMap
  private lateinit var binding: ActivityMapsBinding
  private lateinit var calculatorFragment: CalculatorFragment

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_maps)

    initMap()
    initCalculator()
  }

  override fun showRoute(route: List<List<Double>>) {

    drawRoute(route)
  }

  private fun drawRoute(route: List<List<Double>>) {
    googleMap.clear()
    if (route.isEmpty()) {
      return
    }

    val bounds = Builder()
    val opts = PolylineOptions().apply {
      width(10f)
      color(ContextCompat.getColor(this@MapsActivity, R.color.truckPadYellow))
    }
    route.forEach {
      val latLng = it.toLatLng()
      opts.add(latLng)
      bounds.include(latLng)
    }
    googleMap.addPolyline(opts)

    val origin = route.first().toLatLng()
    val destination = if (route.size == 1) {
      origin
    } else {
      route.last().toLatLng()
    }
    if (origin != null && destination != null) {
      listOf(origin, destination).forEach {
        googleMap.addMarker(MarkerOptions().position(it))
      }
    }
    val sheetHeight = resources.getDimension(R.dimen.a_maps_peek_height)
    val toolbarHeight = binding.toolbar.height
    val offset = (sheetHeight + toolbarHeight)
    googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), offset.toInt()))
  }

  private fun initCalculator() {
    supportFragmentManager.findFragmentById(R.id.calculator_fragment).let { fragment ->
      calculatorFragment = fragment as CalculatorFragment
    }
  }

  override fun onMapReady(map: GoogleMap) {
    googleMap = map
    setGoogleLogoPadding()

    googleMap.animateCamera(CameraUpdateFactory.newLatLng(BRAZIL_GEO_LOCATION))
    googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(BRAZIL_BOUNDS, 0))
  }

  private fun initMap() {
    val mapFragment = supportFragmentManager
      .findFragmentById(R.id.googleMap) as SupportMapFragment
    mapFragment.getMapAsync(this)
  }

  private fun setGoogleLogoPadding() {
    val paddingBottomPx = resources.getDimension(R.dimen.a_maps_margin_bottom_calculator).toInt()
    googleMap.setPadding(ZERO, ZERO, ZERO, paddingBottomPx)
  }

  companion object {
    fun createIntent(context: Context): Intent {
      return Intent(context, MapsActivity::class.java)
    }
  }
}