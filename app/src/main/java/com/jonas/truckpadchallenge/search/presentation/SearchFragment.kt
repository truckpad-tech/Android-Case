package com.jonas.truckpadchallenge.search.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.Place.Field.ADDRESS
import com.google.android.libraries.places.api.model.Place.Field.ID
import com.google.android.libraries.places.api.model.Place.Field.LAT_LNG
import com.google.android.libraries.places.api.model.Place.Field.NAME
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.jonas.truckpadchallenge.R
import com.jonas.truckpadchallenge.search.domain.Location
import com.jonas.truckpadchallenge.search.domain.PlaceType
import com.jonas.truckpadchallenge.search.domain.PlaceType.DESTINATION
import com.jonas.truckpadchallenge.search.domain.PlaceType.ORIGIN
import com.jonas.truckpadchallenge.search.domain.Points
import com.jonas.truckpadchallenge.search.domain.RouteCalculationInfo
import kotlinx.android.synthetic.main.fragment_search.calculate_route_button
import kotlinx.android.synthetic.main.fragment_search.consumption_edit_text
import kotlinx.android.synthetic.main.fragment_search.fuel_edit_text
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModel()

    lateinit var originPlace: Location
    lateinit var destinationPlace: Location

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_search, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupObservers()

        setupAutoCompleteFragment(R.id.origin_place_fragment, ORIGIN)
        setupAutoCompleteFragment(R.id.destination_place_fragment, DESTINATION)

        calculate_route_button.setOnClickListener { onClickCalculateRoute() }
    }

    private fun setupObservers() {
        viewModel.uiState.observe(viewLifecycleOwner, Observer(::updateUI))
    }

    private fun setupAutoCompleteFragment(@IdRes resId: Int, type: PlaceType) {
        val fragment = childFragmentManager.findFragmentById(resId) as AutocompleteSupportFragment

        fragment.apply {
            setPlaceFields(listOf(ID, NAME, ADDRESS, LAT_LNG))
            placeSelectedListener(this, type)
        }
    }

    private fun updateUI(state: SearchUiState) {

    }

    private fun placeSelectedListener(fragment: AutocompleteSupportFragment, type: PlaceType) {
        fragment.setOnPlaceSelectedListener(
            object : PlaceSelectionListener {
                override fun onPlaceSelected(place: Place) {
                    when (type) {
                        ORIGIN -> originPlace = getLocation(place)
                        DESTINATION -> destinationPlace = getLocation(place)
                    }
                }

                override fun onError(status: Status) {
                    Log.i("AppDoJonasDeuRuim", "An error occurred: ${status.status}")
                }
            }
        )
    }

    private fun onClickCalculateRoute() {
        if (checkLocationFieldsAreFilled()) viewModel.calculateRoute(getRouteCalculationInfo())
        else showDialog()
    }

    private fun showDialog() {
        activity?.let { fragment ->
            AlertDialog.Builder(fragment).apply {
                setTitle("Atenção")
                setMessage("Você deve inserir sua origem e destino")
                setPositiveButton(android.R.string.ok, null)
            }.create().show()
        }
    }

    private fun checkLocationFieldsAreFilled() =
        ::originPlace.isInitialized && ::destinationPlace.isInitialized

    private fun getRouteCalculationInfo() = RouteCalculationInfo(
        Points(listOf(originPlace.longitude, originPlace.latitude)),
        Points(listOf(destinationPlace.longitude, destinationPlace.latitude)),
        consumption_edit_text.text.toString().toDouble(),
        fuel_edit_text.text.toString().toDouble()
    )

    private fun getLocation(place: Place) = Location(place.latLng?.latitude, place.latLng?.latitude)
}