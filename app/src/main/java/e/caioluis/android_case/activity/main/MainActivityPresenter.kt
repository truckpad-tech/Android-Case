package e.caioluis.android_case.activity.main

import android.app.Activity
import android.content.pm.PackageManager
import android.view.View
import androidx.core.view.isVisible
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetBehavior
import e.caioluis.android_case.R
import e.caioluis.android_case.activity.history.HistoryActivity
import e.caioluis.android_case.activity.route.RouteResultPresenter.Companion.getRouteStartIntent
import e.caioluis.android_case.json.*
import e.caioluis.android_case.map.MyMap
import e.caioluis.android_case.model.RouteResult
import e.caioluis.android_case.util.CheckPermission
import e.caioluis.android_case.util.Constants.MAIN_ACTIVITY
import e.caioluis.android_case.web.PriceAndRouteContract
import e.caioluis.android_case.web.PriceAndRouteInteractor
import kotlinx.android.synthetic.main.activity_main.*

class MainActivityPresenter(

    private val mainView: MainActivityContract.MainView

) : MainActivityContract.MainPresenter, PriceAndRouteContract {

    private val mainActivity = mainView as Activity

    private var bottomSheet: View = mainActivity.findViewById<View>(R.id.main_bottom_sheet)
    var bSheetBehavior = BottomSheetBehavior.from(bottomSheet)

    private val map = MyMap(mainActivity)
    private val checkPermission = CheckPermission(mainActivity)

    private var initialLocation: LatLng = LatLng(0.0, 0.0)
    private var finalLocation: LatLng = LatLng(0.0, 0.0)
    private var isInitialPoint = true

    private var initialAddress = ""
    private var finalAddress = ""

    private var routeResponse: RouteResponse? = null
    private var priceResponse: PriceResponse? = null

    private var axis = ""
    private var consumption = ""
    private var dieselPrice = ""

    private var interactor = PriceAndRouteInteractor(this)

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

        expandBottomSheet(true)
        map.startMap()
    }

    override fun bottomSheetClicked() {

        expandBottomSheet(true)
    }

    override fun handleStartPointClick() {

        isInitialPoint = true
        setLocationButtonLabel(mainActivity.getString(R.string.label_set_initial_location))
        locationDefiningViewsState(true)
    }

    override fun handleDestinationClick() {

        isInitialPoint = false
        setLocationButtonLabel(mainActivity.getString(R.string.label_set_final_location))
        locationDefiningViewsState(true)
    }

    override fun handleSearchClick() {

        mainView.closeKeyboard()

        val searchString = mainActivity.map_et_searchBar.text.toString()

        if (searchString.isEmpty()) {

            mainView.showToastMessage(mainActivity.getString(R.string.error_type_an_address))
            return
        }

        try {
            val address = map.geoLocate(searchString = searchString)

            val addressLatLng = LatLng(address!!.latitude, address.longitude)
            map.navigateToLocation(addressLatLng)

        } catch (ex: Exception) {
            mainView.showToastMessage(mainActivity.getString(R.string.error_something_went_wrong))
        }
    }

    override fun handleSetLocationClick() {

        if (map.isActualLocationEmpty()) {
            mainView.showToastMessage(mainActivity.getString(R.string.error_no_location))
            return
        }

        try {

            val address = map.geoLocate(map.getActualLatLng())

            if (isInitialPoint) {

                initialAddress = address!!.getAddressLine(0)
                mainActivity.bottom_tv_startPoint.text = initialAddress

                initialLocation = map.getActualLatLng()
                mainView.showToastMessage(mainActivity.getString(R.string.message_initial_location_applied))

            } else {

                finalAddress = address!!.getAddressLine(0)
                mainActivity.bottom_tv_destination.text = finalAddress

                finalLocation = map.getActualLatLng()
                mainView.showToastMessage(mainActivity.getString(R.string.message_final_location_applied))
            }

        } catch (ex: Exception) {
            mainView.showToastMessage(mainActivity.getString(R.string.error_requisition_failed))
        }

        locationDefiningViewsState(false)
    }

    override fun handleCalculateClick() {

        axis = mainActivity.bottom_et_axis.text.toString()
        consumption = mainActivity.bottom_et_consumption.text.toString()
        dieselPrice = mainActivity.bottom_et_dieselPrice.text.toString()

        if (axis.isEmpty()
            || consumption.isEmpty()
            || dieselPrice.isEmpty()
            || initialLocation == LatLng(0.0, 0.0)
            || finalLocation == LatLng(0.0, 0.0)
        ) {
            mainView.showToastMessage(mainActivity.getString(R.string.error_empty_fields))
            return
        }

        if (initialLocation == finalLocation) {
            mainView.showToastMessage(mainActivity.getString(R.string.message_same_initial_and_final_location))
            return
        }

        calculatingRequestViewsState(true)

        interactor.sendRequest(
            createRouteDataObject(consumption, dieselPrice)
        )
    }

    override fun handleSeeHistoryClick() {

        val intent = HistoryActivity.getHistoryStartIntent(mainActivity)
        mainActivity.startActivity(intent)
    }

    private fun setLocationButtonLabel(btnText: String) {
        mainActivity.map_btn_setLocation.text = btnText
    }

    private fun locationDefiningViewsState(defining: Boolean) {

        if (defining) {
            expandBottomSheet(false)
            mainActivity.map_btn_setLocation.isVisible = true
            mainActivity.map_searchBar_layout.isVisible = true

        } else {
            expandBottomSheet(true)
            mainActivity.map_btn_setLocation.isVisible = false
            mainActivity.map_searchBar_layout.isVisible = false
        }
    }

    private fun createRouteDataObject(consumption: String, dieselPrice: String): RouteEnv {

        val initialLocList: MutableList<Double> = mutableListOf()
        val finalLocList: MutableList<Double> = mutableListOf()
        val places: MutableList<Place> = mutableListOf()

        // the locations lists must be on this order for the server
        initialLocList.add(initialLocation.longitude)
        initialLocList.add(initialLocation.latitude)

        finalLocList.add(finalLocation.longitude)
        finalLocList.add(finalLocation.latitude)

        places.add(Place(initialLocList))
        places.add(Place(finalLocList))

        return RouteEnv(
            places,
            consumption.toInt(),
            dieselPrice.toDouble()
        )
    }

    private fun createPriceDataObject(distance: Double): PriceEnv {

        return PriceEnv(
            axis.toInt(),
            distance,
            true
        )
    }

    private fun passParameters() {

        val routeResult = RouteResult(
            initial_address = initialAddress,
            final_address = finalAddress,
            distance = routeResponse!!.distance,
            duration = routeResponse!!.duration,
            toll_count = routeResponse!!.toll_count,
            toll_cost = routeResponse!!.toll_cost,
            fuel_usage = routeResponse!!.fuel_usage,
            fuel_cost = routeResponse!!.fuel_cost,
            total_cost = routeResponse!!.total_cost,
            refrigerated = priceResponse!!.frigorificada,
            general = priceResponse!!.geral,
            granel = priceResponse!!.granel,
            neogranel = priceResponse!!.neogranel,
            hazardous = priceResponse!!.perigosa
        )

        callRouteActivity(routeResult)
    }

    private fun callRouteActivity(result: RouteResult) {

        val mIntent = getRouteStartIntent(mainActivity, result, MAIN_ACTIVITY)
        mainActivity.startActivity(mIntent)
    }

    private fun calculatingRequestViewsState(isRequesting: Boolean) {

        if (isRequesting) {

            expandBottomSheet(false)

            with(mainActivity) {
                map_iv_pin.isVisible = false
                map_progressBar.isVisible = true
                bottom_btn_calculate.isVisible = false
                bottom_btn_history.isVisible = false
            }
            return

        } else {

            with(mainActivity) {
                map_iv_pin.isVisible = false
                map_progressBar.isVisible = false
                bottom_btn_calculate.isVisible = true
                bottom_btn_history.isVisible = true
            }
        }
    }

    override fun routeResponseSuccess(modelResponse: RouteResponse) {

        routeResponse = modelResponse

        if (routeResponse == null) {
            responseFailed()
            return
        }

        interactor.sendRequest(
            priceEnvModel = createPriceDataObject(
                routeResponse!!.distance.toDouble()
            )
        )
    }

    override fun responseFailed() {
        calculatingRequestViewsState(false)
        mainView.showToastMessage(mainActivity.getString(R.string.error_requisition_failed))
    }

    override fun priceResponseSuccess(modelResponse: PriceResponse) {
        priceResponse = modelResponse
        calculatingRequestViewsState(false)
        passParameters()
    }

    private fun expandBottomSheet(boolean: Boolean) {

        if (boolean) {
            bSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        } else {
            bSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }
}