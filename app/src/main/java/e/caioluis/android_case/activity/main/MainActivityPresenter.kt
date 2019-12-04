package e.caioluis.android_case.activity.main

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import e.caioluis.android_case.R
import e.caioluis.android_case.util.CheckPermission
import kotlinx.android.synthetic.main.activity_maps.*

class MainActivityPresenter(

    private val mainView: MainActivityContract.MainView

) : MainActivityContract.MainPresenter, OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private val mainActivity = mainView as Activity

    private var locationManager: LocationManager =
        (mainActivity as Context).getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private var mapFragment =
        (mainActivity as FragmentActivity).supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

    private var willShowDialog = true
    private val checkPermission = CheckPermission(mainActivity)

    private var actualLatLng: LatLng = LatLng(0.0, 0.0)
    private var initialLocation: LatLng = LatLng(0.0, 0.0)
    private var finalLocation: LatLng = LatLng(0.0, 0.0)
    private var isInitialPoint = true

    override fun handlePermissionResult(result: IntArray) {

        if (result.isEmpty() || result.first() == PackageManager.PERMISSION_DENIED) {
            showPermissionAlert()
            return
        }

        startApp()
    }

    override fun startApp() {

        if (!checkPermission.checkGpsPermission())
            return

        mapFragment.getMapAsync(this)
    }

    override fun handleSearch() {

        val searchString = mainActivity.map_et_searchBar.text.toString()

        val address = geolocate(searchString)

        if (address != null) {

            val addressLatLng = LatLng(address.latitude, address.longitude)
            navigateToLocation(addressLatLng)
        }
    }

    override fun handleStartPointClick() {

        if (isActualLocationEmpty()) return

        mainView.showToastMessage("Defina o endereço INICIAL")

        isInitialPoint = true

        mainView.expandBottomSheet(false)
        mainView.showUseLocationButton(true)
    }

    override fun handleFinalPointClick() {

        if (isActualLocationEmpty()) return

        mainView.showToastMessage("Defina o endereço FINAL")

        isInitialPoint = false

        mainView.expandBottomSheet(false)
        mainView.showUseLocationButton(true)
    }

    override fun handleUseLocationClick() {

        val address = geolocate(actualLatLng)

        if (isInitialPoint) {
            mainActivity.bottom_tv_startPoint.text = address!!.getAddressLine(0)
            initialLocation = actualLatLng
            mainView.showToastMessage("Endereço inicial aplicado!")
        } else {
            mainActivity.bottom_tv_finalPoint.text = address!!.getAddressLine(0)
            finalLocation = actualLatLng
            mainView.showToastMessage("Endereço final aplicado!")
        }

        mainView.expandBottomSheet(true)
    }

    private fun geolocate(searchString: String): Address? {

        val geocode = Geocoder(mainActivity)
        var result: ArrayList<Address> = arrayListOf()

        try {
            result = geocode.getFromLocationName(searchString, 1) as ArrayList<Address>

        } catch (ex: Exception) {
            mainView.showToastMessage(ex.message.toString())
        }

        if (result.size > 0) {

            return result.first()
        }

        return null
    }

    private fun geolocate(latLng: LatLng): Address? {

        val geocode = Geocoder(mainActivity)
        var result: ArrayList<Address> = arrayListOf()

        try {
            result =
                geocode.getFromLocation(latLng.latitude, latLng.longitude, 1) as ArrayList<Address>

        } catch (ex: Exception) {
            mainView.showToastMessage(ex.message.toString())
        }

        if (result.size > 0) {

            return result.first()
        }

        return null
    }

    private fun isActualLocationEmpty(): Boolean {
        if (actualLatLng == LatLng(0.0, 0.0)) {
            mainView.showToastMessage("Não existe ponto marcado no mapa")
            return true
        }
        return false
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap

        mMap.isMyLocationEnabled = true

        navigateToLocation(getMyLocationLatLng())

        mMap.setOnCameraIdleListener {

            actualLatLng = mMap.cameraPosition.target
        }
    }

    @SuppressLint("MissingPermission")
    private fun getMyLocationLatLng(): LatLng {

        val myLocation = locationManager
            .getLastKnownLocation(LocationManager.GPS_PROVIDER)
            ?: return LatLng(
                -23.533773,
                -46.625290
                // latitude e longitude do centro de São Paulo
            )
        return LatLng(myLocation.latitude, myLocation.longitude)
    }

    private fun navigateToLocation(latLng: LatLng) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }

    private fun showPermissionAlert() {

        if (willShowDialog) {

            val builder = AlertDialog.Builder(mainActivity)

            with(builder) {

                setTitle(context.getString(R.string.alert_title_warning))
                setMessage(context.getString(R.string.alert_message_permission_denied))
                setCancelable(true)
                setPositiveButton(android.R.string.yes) { _, _ ->
                    willShowDialog = !checkPermission.checkGpsPermission()
                }

                setNegativeButton(context.getString(R.string.label_exit)) { _, _ ->

                    mainActivity.finish()
                }

                show()
            }
        }
    }

    override fun bottomSheetClicked() {

        //mainView.showUseLocationButton(false)
        mainView.expandBottomSheet(true)
    }
}