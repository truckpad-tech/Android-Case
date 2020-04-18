package dev.khalil.freightpad.ui.activity

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationServices
import dev.khalil.freightpad.R
import dev.khalil.freightpad.common.ADDRESS_KEY
import dev.khalil.freightpad.common.LAT_KEY
import dev.khalil.freightpad.common.LONG_KEY
import dev.khalil.freightpad.databinding.ActivitySearchBinding
import dev.khalil.freightpad.di.searchModule
import dev.khalil.freightpad.extensions.viewModel
import dev.khalil.freightpad.model.Place
import dev.khalil.freightpad.ui.adapter.LocationsAdapter
import dev.khalil.freightpad.ui.adapter.holder.LocationClick
import dev.khalil.freightpad.ui.viewModel.SearchActivityViewModel
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

class SearchActivity : AppCompatActivity(), KodeinAware, LocationClick {

  override val kodein = Kodein.lazy {
    import(searchModule)
  }

  private lateinit var binding: ActivitySearchBinding

  private val locationsAdapter by lazy { LocationsAdapter(arrayListOf(), this) }
  private val fusedLocationClient by lazy { LocationServices.getFusedLocationProviderClient(this) }

  private val searchViewModel: SearchActivityViewModel by viewModel()

  companion object {
    private const val LOCATION_PERMISSION_CODE = 3

    fun createIntent(context: Context): Intent {
      return Intent(context, SearchActivity::class.java)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_search)

    initView()
    initRecycler()
    initObservers()
    initListeners()
  }

  override fun locationClicked(place: Place) {
    setLocation(place)
  }

  override fun onRequestPermissionsResult(
    requestCode: Int, permissions: Array<out String>,
    grantResults: IntArray) {

    when (requestCode) {
      LOCATION_PERMISSION_CODE -> {
        if (grantResults.isNotEmpty() && grantResults.first() == PERMISSION_GRANTED) {
          getUserLocation()
        } //TODO Create a response if permission was denied
      }
    }
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
  }

  override fun onDestroy() {
    searchViewModel.onDestroy()
    super.onDestroy()
  }

  private fun initListeners() {
    binding.myLocationText.setOnClickListener {
      if (isLocationPermissionGranted()) {
        getUserLocation()
      } else {
        requestLocationPermission()
      }

    }
    binding.placeEdit.addTextChangedListener(searchTextWatcher())
  }

  private fun initView() {
    binding.placeEdit.requestFocus()
  }

  private fun initRecycler() {
    binding.placesRecycler.apply {
      adapter = locationsAdapter
      addItemDecoration(DividerItemDecoration(this.context, VERTICAL))
      layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
    }
  }

  private fun initObservers() {
    searchViewModel.searchResult.observe(this, Observer { locationList ->
      locationsAdapter.updateLocations(locationList)
    })
  }

  private fun searchTextWatcher(): TextWatcher {
    return object : TextWatcher {
      override fun afterTextChanged(s: Editable?) {
        searchViewModel.search(s.toString())
      }

      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

      }

      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
      }
    }
  }

  private fun isLocationPermissionGranted(): Boolean {
    return checkSelfPermission(this, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED &&
        checkSelfPermission(this, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED
  }

  private fun requestLocationPermission() {
    val permissions = arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
    ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_CODE)
  }

  @SuppressLint("MissingPermission")
  private fun getUserLocation() {
    fusedLocationClient.lastLocation
      .addOnSuccessListener { location: Location ->
        val place = Place(
          getString(R.string.your_location),
          listOf(location.longitude, location.latitude))
        //TODO TEST THIS SHIT
        setLocation(place)
      }
  }

  private fun setLocation(place: Place) {
    val intent = Intent().apply {
      putExtra(ADDRESS_KEY, place.displayName)
      putExtra(LONG_KEY, place.point.first())
      putExtra(LAT_KEY, place.point.last())
    }
    setResult(Activity.RESULT_OK, intent)
    finish()
  }

}
