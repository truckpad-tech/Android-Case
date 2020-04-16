package dev.khalil.freightpad.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import dev.khalil.freightpad.R
import dev.khalil.freightpad.R.id
import dev.khalil.freightpad.common.ZERO
import dev.khalil.freightpad.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

  private lateinit var googleMap: GoogleMap
  private lateinit var binding: ActivityMapsBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_maps)

    initMap()
  }

  override fun onMapReady(map: GoogleMap) {
    googleMap = map
    setGoogleLogoPadding()
  }

  private fun initMap() {
    val mapFragment = supportFragmentManager
      .findFragmentById(id.googleMap) as SupportMapFragment
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