package e.caioluis.android_case.activity.main

import android.app.Activity
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import e.caioluis.android_case.R
import e.caioluis.android_case.map.MyMap
import e.caioluis.android_case.util.CheckPermission
import kotlinx.android.synthetic.main.activity_maps.*

class MainActivityPresenter(

    private val mainView: MainActivityContract.MainView

) : MainActivityContract.MainPresenter {

    private val mainActivity = mainView as Activity
    private val googleMap = MyMap(mainActivity)
    private val checkPermission = CheckPermission(mainActivity)
    private var initialLocation: LatLng = LatLng(0.0, 0.0)
    private var finalLocation: LatLng = LatLng(0.0, 0.0)
    private var isInitialPoint = true

    override fun handlePermissionResult(result: IntArray) {

        if (result.isEmpty() || result.first() == PackageManager.PERMISSION_DENIED) {
            checkPermission.showPermissionAlert()
            return
        }

        startApp()
    }

    override fun startApp() {

        if (!checkPermission.checkGpsPermission())
            return

        googleMap.startMap()
    }

    override fun handleSearch() {

        val searchString = mainActivity.map_et_searchBar.text.toString()

        val address = geoLocate(searchString)

        if (address != null) {

            val addressLatLng = LatLng(address.latitude, address.longitude)
            googleMap.navigateToLocation(addressLatLng)
        }
    }

    override fun handleStartPointClick() {

        if (googleMap.isActualLocationEmpty()) {
            mainView.showToastMessage(mainActivity.getString(R.string.error_no_location))
            return
        }

        mainView.showToastMessage(mainActivity.getString(R.string.message_define_inicial_location))

        isInitialPoint = true

        mainView.expandBottomSheet(false)
        mainView.showUseLocationButton(true)
    }

    override fun handleFinalPointClick() {

        if (googleMap.isActualLocationEmpty()) {
            mainView.showToastMessage(mainActivity.getString(R.string.error_no_location))
            return
        }

        mainView.showToastMessage(mainActivity.getString(R.string.message_define_inicial_location))

        isInitialPoint = false

        mainView.expandBottomSheet(false)
        mainView.showUseLocationButton(true)
    }

    override fun handleUseLocationClick() {

        val address = geoLocate(googleMap.getActualLatLng())

        if (isInitialPoint) {
            mainActivity.bottom_tv_startPoint.text = address!!.getAddressLine(0)
            initialLocation = googleMap.getActualLatLng()
            mainView.showToastMessage(mainActivity.getString(R.string.message_initial_location_applied))
        } else {
            mainActivity.bottom_tv_finalPoint.text = address!!.getAddressLine(0)
            finalLocation = googleMap.getActualLatLng()
            mainView.showToastMessage(mainActivity.getString(R.string.message_final_location_applied))
        }

        mainView.expandBottomSheet(true)
    }

    private fun geoLocate(searchString: String): Address? {

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

    private fun geoLocate(latLng: LatLng): Address? {

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

    override fun bottomSheetClicked() {

        //mainView.showUseLocationButton(false)
        mainView.expandBottomSheet(true)
    }
}