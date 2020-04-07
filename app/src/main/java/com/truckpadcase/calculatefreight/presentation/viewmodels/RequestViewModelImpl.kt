package com.truckpadcase.calculatefreight.presentation.viewmodels

import android.app.Application
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.truckpadcase.calculatefreight.data.network.ResultWrapper
import com.truckpadcase.calculatefreight.data.repository.FreightCalculateRepositoryImpl
import com.truckpadcase.calculatefreight.data.repository.RouteAndPriceServiceRepositoryImpl
import com.truckpadcase.calculatefreight.domain.model.local.FreightData
import com.truckpadcase.calculatefreight.domain.model.remote.*
import com.truckpadcase.calculatefreight.presentation.viewmodels.contratcs.RequestViewModel
import com.truckpadcase.calculatefreight.presentation.views.ResultSearchActivity
import com.truckpadcase.calculatefreight.utils.Constants.SEARCH_RESULT_ID
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class RequestViewModelImpl (application: Application) : AndroidViewModel(application),
    RequestViewModel {
    /*-- Context --*/
    private val context = application

    /*-- Repositories --*/
    private var freightCalculateRepositoryImpl: FreightCalculateRepositoryImpl = FreightCalculateRepositoryImpl()
    private var routeAndPriceServiceRepository : RouteAndPriceServiceRepositoryImpl = RouteAndPriceServiceRepositoryImpl()

    private lateinit var localityLits: List<Locations>

    val showProgress = MutableLiveData<Boolean>()
    val showError = MutableLiveData<String>()

    /*-- Data binding Fields --*/
    val originCity = MutableLiveData<String>()
    val destinyCity = MutableLiveData<String>()
    val axes = MutableLiveData<String>()
    val averageFuel = MutableLiveData<String>()
    val dieselPrice = MutableLiveData<String>()

    private var routeResponseData = MutableLiveData<RouteResponse>()
    private var priceResponseData = MutableLiveData<PriceResponse>()

    private var routeRequestSuccess : Boolean = false

    var job: Job = Job()

    override fun submitData() {

        showProgress.value = true
        if (checkInputs() && convertAddress()){

            job = GlobalScope.launch{

                val routeResponse = async { routeAndPriceServiceRepository.getRoute(
                    RouteRequest(localityLits, averageFuel.value!!.toDouble(), dieselPrice.value!!.toDouble())
                ) }.await()
                async {  when (routeResponse) {
                    is ResultWrapper.NetworkError -> showNetworkError()
                    is ResultWrapper.GenericError -> showGenericError(routeResponse)
                    is ResultWrapper.Success<RouteResponse?> -> routeResponse.value?.let { showRouteSuccess(it) }
                } }.await()

                if(routeRequestSuccess){
                    val priceResponse = async { routeAndPriceServiceRepository.getPrice(
                        PriceRequest(axes.value!!.toInt(), routeResponseData.value!!.distance, true)
                    ) }.await()
                    async {
                        when (priceResponse) {
                            is ResultWrapper.NetworkError -> showNetworkError()
                            is ResultWrapper.GenericError -> showGenericError(priceResponse)
                            is ResultWrapper.Success<PriceResponse?> -> priceResponse.value?.let { showPriceSuccess(it) }
                        }
                    }.await()
                }
            }
        }
    }


    private fun convertAddress(): Boolean {

        val latLngOrigin : LatLng? = getCoordinate(originCity.value.toString() )
        val latLngDestiny : LatLng? = getCoordinate(destinyCity.value.toString() )
        return if (latLngOrigin == null || latLngDestiny == null ){
            false
        } else{
            localityLits = listOf(Locations(listOf( latLngOrigin!!.longitude, latLngOrigin!!.latitude)), Locations(
                listOf(latLngDestiny!!.longitude, latLngDestiny!!.latitude) ))
            true
        }
    }

    private fun checkInputs() : Boolean {

        if ( originCity.value.isNullOrBlank()){
            showError.postValue("Endereço origem inválido")
            return false
        }else if (destinyCity.value.isNullOrBlank()){
            showError.postValue("Endereço destino inválido")
            return false
        }else if (axes.value.isNullOrBlank()){
            showError.postValue("Eixos inválidos")
            return false
        }else if (averageFuel.value.isNullOrBlank()) {
            showError.postValue("Consumo médio de combustivel inválido")
            return false
        }else if (dieselPrice.value.isNullOrBlank() ){
            showError.postValue("Preço do diesel invalido")
            return false
        }
        val axesNumber = axes.value!!.toInt()
        if (axesNumber > 10 || axesNumber <= 1 ){
            showError.postValue("O número de eixos devem ser entre 2 e 9")
            return false
        }
        return true
    }

     private fun getCoordinate(query : String) : LatLng?{

        val location : Geocoder = Geocoder(context)
        val list : MutableList<Address> =  location.getFromLocationName(query, 5)
        if ( list.isNullOrEmpty() ){
            showError.value = "Address not found"
            return null
        }
         return LatLng(list[0].latitude, list[0].longitude)
    }

    fun foundMyLocation() {

        val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation.addOnSuccessListener() { location ->

            if (location != null) {
                var lastLocation = location
                val locationGeo : Geocoder = Geocoder(context)
                val address : MutableList<Address> = locationGeo.getFromLocation(location.latitude, location.longitude, 1)
                originCity.value = address[0].getAddressLine(0)
            }
        }
    }

    private fun showRouteSuccess(response : RouteResponse) {
        routeRequestSuccess = true
        routeResponseData.postValue(response)
    }

    private fun showPriceSuccess(response: PriceResponse) {
        priceResponseData.postValue(response)

        val id : Long = freightCalculateRepositoryImpl.saveFreightCalculateInfo(context,
            FreightData(0, originCity.value.toString(), destinyCity.value.toString(), routeResponseData.value!!.distance,
                routeResponseData.value!!.route, axes.value!!.toInt(), routeResponseData.value!!.duration, routeResponseData.value!!.toll_count,
                routeResponseData.value!!.toll_cost, routeResponseData.value!!.fuel_usage, routeResponseData.value!!.fuel_cost,
                routeResponseData.value!!.total_cost, priceResponseData.value!!.refrigerated.toDouble(), priceResponseData.value!!.general,
                priceResponseData.value!!.granel, priceResponseData.value!!.neogranel, priceResponseData.value!!.hazardous)
        )


        val intent = ResultSearchActivity.getStartIntent(context )
        intent.putExtra(SEARCH_RESULT_ID, id.toLong())
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        showProgress.postValue(false)
        context.startActivity(intent)
    }

    private fun showGenericError(routeResponse: ResultWrapper<RouteResponse?>) {
        showError.postValue("Connection error")
        routeRequestSuccess = false
    }

    private fun showNetworkError() {
        showError.postValue("Generic error")
        routeRequestSuccess = false
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}