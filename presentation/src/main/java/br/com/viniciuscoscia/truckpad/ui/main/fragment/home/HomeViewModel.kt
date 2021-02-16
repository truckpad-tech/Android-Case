package br.com.viniciuscoscia.truckpad.ui.main.fragment.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.viniciuscoscia.truckpad.R
import br.com.viniciuscoscia.truckpad.common.SearchResult
import br.com.viniciuscoscia.truckpad.domain.entities.Place
import br.com.viniciuscoscia.truckpad.domain.entities.RouteCalc
import br.com.viniciuscoscia.truckpad.domain.usecases.GetPricesByCargoTypeUseCase
import br.com.viniciuscoscia.truckpad.domain.usecases.GetRouteCalcsUseCase
import br.com.viniciuscoscia.truckpad.entity.PlaceResult
import br.com.viniciuscoscia.truckpad.entity.RepoResult
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.*
import java.lang.reflect.InvocationTargetException
import java.util.*

class HomeViewModel(
    private val context: Context,
    private val getRouteCalcsUseCase: GetRouteCalcsUseCase,
    private val getPricesByCargoTypeUseCase: GetPricesByCargoTypeUseCase
) : ViewModel() {

    private val geoCoder = Geocoder(context, Locale("pt", "BR"))
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val _originValidator = MutableLiveData<PlaceResult>()
    val originValidator: LiveData<PlaceResult> = _originValidator

    private val _destinyValidator = MutableLiveData<PlaceResult>()
    val destinyValidator: LiveData<PlaceResult> = _destinyValidator

    private val _calcResultsListener = MutableLiveData<RepoResult>()
    val calcResults: LiveData<RepoResult> = _calcResultsListener

    @Throws(InvocationTargetException::class)
    fun searchAddresses(text: String): List<Address> {
        if (text.isBlank()) {
            return emptyList()
        }

        return runBlocking(Dispatchers.IO) {
            geoCoder.getFromLocationName(text, 1)
        }
    }

    fun searchForOriginAddress(text: String?) {
        if (text.isNullOrBlank().not()) {
            executeSearch(text!!, _originValidator)
        }
    }

    fun searchForDestinyAddress(text: String?) {
        if (text.isNullOrBlank().not()) {
            executeSearch(text!!, _destinyValidator)
        }
    }

    fun searchRoutesAndPrices(
        fuelConsumptionKilometersPerLiter: Int,
        fuelPrice: Double,
        origin: Address,
        destiny: Address,
        axis: Int
    ) {
        try {
            GlobalScope.launch(Dispatchers.IO) {
                val originCoordinate = getPlace(origin)
                val destinyCoordinate = getPlace(destiny)

                val routeCalcsParams = GetRouteCalcsUseCase.Params(
                    fuelConsumptionKilometersPerLiter,
                    fuelPrice,
                    listOf(originCoordinate, destinyCoordinate)
                )
                val routeCalc: RouteCalc = getRouteCalcsUseCase.execute(routeCalcsParams)

                val pricesParams = GetPricesByCargoTypeUseCase.Params(
                    axis,
                    routeCalc,
                    true,
                    fuelPrice,
                    originCoordinate,
                    destinyCoordinate
                )
                val results = getPricesByCargoTypeUseCase.execute(pricesParams)

                withContext(Dispatchers.Main) {
                    _calcResultsListener.value = RepoResult(
                        SearchResult.SUCCESS,
                        results
                    )
                }
            }
        } catch (e: Exception) {
            e.message?.run {
                Log.e(this::class.java.toString(), this)
            }
            _calcResultsListener.value = RepoResult(
                SearchResult.ERROR,
                message = context.getString(R.string.server_error)
            )
        }
    }

    private fun executeSearch(text: String, placeResultLiveData: MutableLiveData<PlaceResult>) {
        try {
            val addresses = searchAddresses(text)

            placeResultLiveData.value = if (addresses.isNotEmpty()) {
                PlaceResult(
                    searchResult = SearchResult.SUCCESS,
                    address = addresses[0]
                )
            } else {
                PlaceResult(
                    searchResult = SearchResult.NO_RESULT,
                    errorMessage = context.getString(R.string.place_not_found)
                )
            }
        } catch (e: InvocationTargetException) {
            e.message?.run {
                Log.e(this::class.java.toString(), this)
            }
            placeResultLiveData.value = PlaceResult(
                searchResult = SearchResult.ERROR,
                errorMessage = context.getString(R.string.internal_search_error)
            )
        }
    }

    private fun getPlace(address: Address): Place = with(address) {
        return Place(
            address.getAddressLine(0),
            latitude.toFloat(),
            longitude.toFloat()
        )
    }

    fun validateAxisQuantity(axis: Int) = axis in 2..9

    fun getCurrentLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation.addOnCompleteListener {
            if (it.result != null) {
                val location = it.result!!
                val address = geoCoder.getFromLocation(
                    location.latitude,
                    location.longitude, 1
                )
                _originValidator.value = PlaceResult(SearchResult.SUCCESS, address[0])
            } else {
                _originValidator.value = PlaceResult(
                    SearchResult.ERROR,
                    errorMessage = context.getString(R.string.error_get_current_location)
                )
            }
        }
    }

    fun hasAccessFineLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}

