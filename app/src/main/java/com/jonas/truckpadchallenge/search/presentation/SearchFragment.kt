package com.jonas.truckpadchallenge.search.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.StringRes
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
import com.jonas.truckpadchallenge.core.utils.gone
import com.jonas.truckpadchallenge.core.utils.visible
import com.jonas.truckpadchallenge.maps.domain.LocationAddress
import com.jonas.truckpadchallenge.search.domain.PlaceType
import com.jonas.truckpadchallenge.search.domain.PlaceType.DESTINATION
import com.jonas.truckpadchallenge.search.domain.PlaceType.ORIGIN
import com.jonas.truckpadchallenge.search.domain.entities.Location
import com.jonas.truckpadchallenge.search.domain.entities.Points
import com.jonas.truckpadchallenge.search.domain.entities.RouteCalculationInfo
import com.jonas.truckpadchallenge.search.domain.entities.SearchResult
import com.jonas.truckpadchallenge.search.presentation.SearchUiState.Error
import com.jonas.truckpadchallenge.search.presentation.SearchUiState.Loading
import com.jonas.truckpadchallenge.search.presentation.SearchUiState.Success
import kotlinx.android.synthetic.main.fragment_search.calculate_route_button
import kotlinx.android.synthetic.main.fragment_search.consumption_edit_text
import kotlinx.android.synthetic.main.fragment_search.fuel_edit_text
import kotlinx.android.synthetic.main.fragment_search.get_current_location
import kotlinx.android.synthetic.main.fragment_search.search_progress_bar
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

        get_current_location.setOnClickListener { onClickGetCurrentLocation() }
        calculate_route_button.setOnClickListener { onClickCalculateRoute() }
    }

    private fun setupObservers() {
        viewModel.uiState.observe(viewLifecycleOwner, Observer(::updateUI))
        viewModel.userLocation.observe(viewLifecycleOwner, Observer(::currentUserLocation))
    }

    private fun setupAutoCompleteFragment(@IdRes resId: Int, type: PlaceType) {
        val fragment = childFragmentManager.findFragmentById(resId) as AutocompleteSupportFragment

        fragment.apply {
            setPlaceFields(listOf(ID, NAME, ADDRESS, LAT_LNG))
            placeSelectedListener(this, type)
        }
    }

    private fun updateUI(state: SearchUiState) {
        toggleLoading(false)
        when (state) {
            Loading -> toggleLoading(true)
            is Success -> goToResult(state.searchResult)
            is Error -> onError()
        }
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
                    onError()
                }
            }
        )
    }

    private fun onClickGetCurrentLocation() {
        viewModel.getUserLocation()
    }

    private fun currentUserLocation(location: LocationAddress) {
        toggleLoading(false)

        val fragment =
            childFragmentManager.findFragmentById(R.id.origin_place_fragment) as AutocompleteSupportFragment
        fragment.setText(location.address)
        originPlace = Location(location.latLng.latitude, location.latLng.longitude)
    }

    private fun onClickCalculateRoute() {
        if (checkLocationFieldsAreFilled()) {
            viewModel.calculateRoute(getRouteCalculationInfo())
        } else showDialog(
            "Atenção",
            "Você precisa definir sua origem e destino",
            android.R.string.ok
        )
    }

    private fun goToResult(searchResult: SearchResult) {
        Toast.makeText(context, "Deu bom $searchResult", Toast.LENGTH_LONG).show()
    }

    private fun onError() {
        showDialog(
            "Atenção",
            "Ocorreu um erro, por favor, tente novamente",
            android.R.string.ok
        )
    }

    private fun showDialog(title: String, message: String, @StringRes buttonMessage: Int) {
        activity?.let { fragment ->
            AlertDialog.Builder(fragment).apply {
                setTitle(title)
                setMessage(message)
                setPositiveButton(buttonMessage, null)
            }.create().show()
        }
    }

    private fun toggleLoading(isShow: Boolean) {
        if (isShow) search_progress_bar.visible() else search_progress_bar.gone()
    }

    private fun checkLocationFieldsAreFilled() =
        ::originPlace.isInitialized && ::destinationPlace.isInitialized

    private fun getRouteCalculationInfo() =
        RouteCalculationInfo(
            Points(listOf(originPlace.longitude!!, originPlace.latitude!!)),
            Points(listOf(destinationPlace.longitude!!, destinationPlace.latitude!!)),
            consumption_edit_text.text.toString().toDouble(),
            fuel_edit_text.text.toString().toDouble()
        )

    private fun getLocation(place: Place) =
        Location(place.latLng?.latitude, place.latLng?.longitude)
}