package br.com.truckpad.waister.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.truckpad.waister.R
import br.com.truckpad.waister.api.Autocomplete
import br.com.truckpad.waister.domain.SearchPoint
import br.com.truckpad.waister.ui.activity.MainActivity.Companion.PARAM_EXTRA_FROM
import br.com.truckpad.waister.ui.activity.MainActivity.Companion.PARAM_EXTRA_POINT
import br.com.truckpad.waister.ui.activity.MainActivity.Companion.REQUEST_ORIGIN
import br.com.truckpad.waister.ui.adapter.PlacesAdapter
import com.github.kittinunf.fuel.httpGet
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_search_location.*
import java.io.IOException
import java.util.*

class SearchLocationActivity : AppCompatActivity(), TextWatcher, LocationListener {

    private var mLocationManager: LocationManager? = null
    private var mRoutesAdapter: PlacesAdapter? = null
    private var mSearchHandler = Handler()
    private var mSearchRunnable = Runnable { searchPlaces() }

    companion object {
        const val REQUEST_PERMISSIONS_LOCATION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_location)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initViews()
        initRecycler()
    }

    private fun initViews() {
        val from = intent.getIntExtra(PARAM_EXTRA_FROM, 0)

        if (from == REQUEST_ORIGIN)
            et_search.setHint(R.string.origin_city)
        else
            et_search.setHint(R.string.destination_city)

        et_search.addTextChangedListener(this)

        tv_my_location.setOnClickListener {
            askForAccessLocation()
        }
    }

    private fun initRecycler() {
        val layoutManager = LinearLayoutManager(this)
        mRoutesAdapter = PlacesAdapter(this)

        rv_places.layoutManager = layoutManager
        rv_places.adapter = mRoutesAdapter

        rv_places.addItemDecoration(
            DividerItemDecoration(
                rv_places.context,
                layoutManager.orientation
            )
        )
    }

    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        mSearchHandler.postDelayed(mSearchRunnable, 1000)
    }

    private fun searchPlaces() {
        mSearchHandler.removeCallbacks(mSearchRunnable)

        if (isFinishing) return

        val terms = et_search.text.toString()

        if (terms.length < 3) return

        pb_loading.visibility = View.VISIBLE

        val url = "https://api.recalculafrete.com.br/autocomplete"

        val params = listOf("search" to terms)

        url.httpGet(params).responseString { _, _, result ->
            val (data, error) = result

            pb_loading?.visibility = View.GONE

            if (error == null && !isFinishing) {
                val autocomplete = Gson().fromJson(data, Autocomplete::class.java)

                mRoutesAdapter?.setData(autocomplete.places)

                if (autocomplete.places.isNotEmpty() && tv_my_location.visibility == View.VISIBLE)
                    tv_my_location.visibility = View.GONE
            }
        }
    }

    fun sendLocationResult(point: SearchPoint) {
        val intent = Intent()
        intent.putExtra(PARAM_EXTRA_POINT, point)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun askForAccessLocation() {
        pb_loading.visibility = View.VISIBLE

        val fine = Manifest.permission.ACCESS_FINE_LOCATION
        val coarse = Manifest.permission.ACCESS_COARSE_LOCATION
        val granted = PackageManager.PERMISSION_GRANTED

        if (
            ContextCompat.checkSelfPermission(this, fine) != granted ||
            ContextCompat.checkSelfPermission(this, coarse) != granted
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(fine, coarse), REQUEST_PERMISSIONS_LOCATION)
            } else {
                startLocationUpdates()
            }
        } else {
            startLocationUpdates()
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val isGPSEnabled = mLocationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled =
            mLocationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (!isGPSEnabled && !isNetworkEnabled) {

            AlertDialog.Builder(this)
                .setTitle(R.string.location)
                .setMessage(R.string.location_required)
                .setPositiveButton(R.string.settings) { _, _ ->
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
                .setNegativeButton(R.string.cancel, null)
                .create()
                .show()

        } else {

            val providers = mLocationManager!!.allProviders

            if (providers.contains(LocationManager.NETWORK_PROVIDER))
                mLocationManager!!.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 0L, 0f, this
                )

            if (providers.contains(LocationManager.GPS_PROVIDER))
                mLocationManager!!.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 0L, 0f, this
                )
        }
    }

    @SuppressLint("MissingPermission")
    override fun onLocationChanged(location: Location) {
        convertToAddress(location)

        mLocationManager?.removeUpdates(this)
        mLocationManager = null
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}

    private fun convertToAddress(location: Location) {
        try {
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses = geocoder.getFromLocation(
                location.latitude, location.longitude, 1
            )

            if (addresses != null && addresses.size > 0) {
                val address = addresses[0]
                var name = address.getAddressLine(0)

                if (address.locality != null)
                    name = address.locality

                sendLocationResult(
                    SearchPoint(name, location.latitude, location.longitude)
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        pb_loading?.visibility = View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }

}
