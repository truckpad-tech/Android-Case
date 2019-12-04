package com.diegobezerra.truckpadcase.ui.main

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegobezerra.truckpadcase.data.api.geo.RouteBody
import com.diegobezerra.truckpadcase.data.api.tictac.AnttPricesBody
import com.diegobezerra.truckpadcase.data.repository.antt.AnttRepository
import com.diegobezerra.truckpadcase.data.repository.autocomplete.AutocompleteRepository
import com.diegobezerra.truckpadcase.data.repository.history.HistoryRepository
import com.diegobezerra.truckpadcase.data.repository.route.GeoRepository
import com.diegobezerra.truckpadcase.domain.model.AnttPrices
import com.diegobezerra.truckpadcase.domain.model.HistoryEntry
import com.diegobezerra.truckpadcase.domain.model.Place
import com.diegobezerra.truckpadcase.domain.model.RouteInfo
import com.diegobezerra.truckpadcase.util.Event
import com.diegobezerra.truckpadcase.util.Result
import com.diegobezerra.truckpadcase.util.debounce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val autocompleteRepository: AutocompleteRepository,
    private val geoRepository: GeoRepository,
    private val anttRepository: AnttRepository,
    private val historyRepository: HistoryRepository
) : ViewModel() {

    // This is just used to keep track of the current view displayed
    val state = MutableLiveData<UiState>(UiState.SEARCH)

    // Stores the current location (if allowed)
    private var currentLocation: List<Double> = emptyList()

    private val _searchUiModel = MutableLiveData<SearchUiModel>()
    val searchUiModel: LiveData<SearchUiModel>
        get() = _searchUiModel

    private val _resultsUiModel = MutableLiveData<ResultsUiModel>()
    val resultsUiModel: LiveData<ResultsUiModel>
        get() = _resultsUiModel

    private val _calculating = MutableLiveData<Boolean>()
    val calculating: LiveData<Boolean>
        get() = _calculating

    // Autocomplete

    // Used to prevent calling autocomplete API again in case the new search term is the same
    // as the previous one.
    private var latestSearch = ""
    private var justPlaced = false

    private val _autocompleteOrigins = MutableLiveData<List<Place>>()
    val autocompleteOrigins: LiveData<List<Place>>
        get() = _autocompleteOrigins

    private val _autocompleteDestinations = MutableLiveData<List<Place>>()
    val autocompleteDestinations: LiveData<List<Place>>
        get() = _autocompleteDestinations

    // History
    val history: LiveData<List<HistoryEntry>> = historyRepository.getHistory()

    /** Actions */
    private val _toggleSheetAction = MutableLiveData<Event<Unit>>()
    val toggleSheetAction: LiveData<Event<Unit>>
        get() = _toggleSheetAction

    private val _useCurrentLocationAction = MutableLiveData<Event<Unit>>()
    val useCurrentLocationAction: LiveData<Event<Unit>>
        get() = _useCurrentLocationAction

    private val _showRouteAction = MutableLiveData<Event<RouteInfo>>()
    val showRouteAction: LiveData<Event<RouteInfo>>
        get() = _showRouteAction

    private val _clearHistoryAction = MutableLiveData<Event<Unit>>()
    val clearHistoryAction: LiveData<Event<Unit>>
        get() = _clearHistoryAction

    /**
     * Whether or not an error occurred during a calculate cost operation.
     */
    val hasError = ObservableBoolean()


    fun onChooseOrigin(position: Int) {
        getAutocompletePlace(AUTOCOMPLETE_ORIGIN, position)?.also { place ->
            getSearchUiModel().let {
                _searchUiModel.value = it.copy(origin = place)
            }
        }
    }

    fun onChooseDestination(position: Int) {
        getAutocompletePlace(AUTOCOMPLETE_DESTINATION, position)?.also { place ->
            getSearchUiModel().let {
                _searchUiModel.value = it.copy(destination = place)
            }
        }
    }

    fun onChangedOrigin(origin: String) {
        // Prevent calling autocomplete for a place already loaded.
        val index = isPlaceLoaded(AUTOCOMPLETE_ORIGIN, origin)
        if (index > -1) {
            onChooseOrigin(index)
            return
        }
        debouncedGetAutocomplete(AUTOCOMPLETE_ORIGIN, origin)
    }

    fun onChangedDestination(destination: String) {
        // Prevent calling autocomplete for a place already loaded.
        val index = isPlaceLoaded(AUTOCOMPLETE_DESTINATION, destination)
        if (index > -1) {
            onChooseDestination(index)
            return
        }
        debouncedGetAutocomplete(AUTOCOMPLETE_DESTINATION, destination)
    }

    fun onChangeAxis(newAxis: Int) {
        getSearchUiModel().let {
            _searchUiModel.value = it.copy(axis = newAxis)
        }
    }

    fun onChangeFuelConsumption(newFuelConsumption: Float) {
        getSearchUiModel().let {
            _searchUiModel.value = it.copy(fuelConsumption = newFuelConsumption)
        }
    }

    fun onChangeFuelPrice(newPrice: Float) {
        getSearchUiModel().let {
            _searchUiModel.value = it.copy(fuelPrice = newPrice)
        }
    }

    fun onTitleClick() {
        _toggleSheetAction.postValue(Event(Unit))
    }

    fun onUseCurrentLocation() {
        _useCurrentLocationAction.postValue(Event(Unit))
    }

    fun onGetLocation(name: String, location: List<Double>) {
        val place = Place(displayName = name, point = location)
        getSearchUiModel().let {
            _searchUiModel.value = it.copy(origin = place)
        }
        currentLocation = location
    }

    fun onCalculate() {
        getSearchUiModel().let { model ->
            if (model.isValid()) {
                calculateCost(
                    model.origin!!,
                    model.destination!!,
                    model.axis,
                    model.fuelConsumption,
                    model.fuelPrice
                )
            }
        }
    }

    fun onShowRoute() {
        _resultsUiModel.value?.routeInfo?.let {
            _toggleSheetAction.postValue(Event(Unit))
            _showRouteAction.postValue(Event(it))
        }
    }

    fun onTryAgain() {
        hasError.set(false)
        switchTo(UiState.SEARCH)
    }

    fun onHistoryEntryClick(entry: HistoryEntry) {
        showResults(
            ResultsUiModel(
                entry.origin,
                entry.destination,
                entry.axis,
                entry.routeInfo,
                entry.anttPrices
            )
        )
    }

    fun onClearHistoryClick() {
        _clearHistoryAction.postValue(Event(Unit))
    }

    fun onClearHistory() = viewModelScope.launch {
        historyRepository.clear()
    }

    private fun debouncedGetAutocomplete(which: Int, search: String) {
        if (search.length < 3 || search == latestSearch) {
            return
        }
        latestSearch = search
        debounce(viewModelScope, 400L, { latestSearch == search }) {
            getAutoComplete(which, search)
        }
    }

    /**
     * Calls Autocomplete API to retrieve a list of places matching the search term.
     */
    private fun getAutoComplete(which: Int, search: String) = viewModelScope.launch {
        val result = autocompleteRepository.getAutocomplete(search)
        var places: List<Place> = emptyList()
        when (result) {
            is Result.Success -> {
                places = result.data
            }
            is Result.Error -> {
                // Ignored
            }
        }
        when (which) {
            AUTOCOMPLETE_ORIGIN -> {
                _autocompleteOrigins.postValue(places)
            }
            AUTOCOMPLETE_DESTINATION -> {
                _autocompleteDestinations.postValue(places)
            }
        }
    }

    /**
     * Calls Geo API to retrieve route info between the two points, taking into account axis, fuel consumption and
     * fuel price informed by the user.
     */
    private fun calculateCost(
        origin: Place,
        destination: Place,
        axis: Int,
        fuelConsumption: Float,
        fuelPrice: Float
    ) = viewModelScope.launch(Dispatchers.IO) {
        showCalculatingProgress()
        getSearchUiModel().let {
            when (val result = geoRepository.getRouteInfo(it.toRouteBody())) {
                is Result.Success -> {
                    val info = result.data
                    getAnttPrices(axis, info)?.also { prices ->
                        // We are safe to add a new entry to our history and to show results to user
                        pushHistoryEntry(
                            origin,
                            destination,
                            axis,
                            fuelConsumption,
                            fuelPrice,
                            info,
                            prices
                        )
                        showResults(ResultsUiModel(origin, destination, axis, info, prices))
                    }
                }
                is Result.Error -> {
                    showError()
                }
            }
        }
    }

    private fun showCalculatingProgress() {
        switchTo(UiState.RESULTS)
        hasError.set(false)
        _calculating.postValue(true)
    }

    private fun showError() {
        hasError.set(true)
        _calculating.postValue(false)
    }

    private fun showResults(model: ResultsUiModel) {
        hasError.set(false)
        _calculating.postValue(false)
        _resultsUiModel.postValue(model)
        switchTo(UiState.RESULTS)
    }

    private fun switchTo(newState: UiState) {
        state.postValue(newState)
    }

    private fun isPlaceLoaded(id: Int, name: String) =
        getAutocompletePlaces(id).indexOfFirst { it.displayName == name }

    private fun getAutocompletePlaces(id: Int): List<Place> {
        return when (id) {
            AUTOCOMPLETE_ORIGIN -> {
                _autocompleteOrigins.value ?: emptyList()
            }
            AUTOCOMPLETE_DESTINATION -> {
                _autocompleteDestinations.value ?: emptyList()
            }
            else -> emptyList()
        }
    }

    private fun getAutocompletePlace(id: Int, position: Int): Place? {
        if (id < 0 || id >= AUTOCOMPLETE_COUNT) {
            return null
        }
        val places = getAutocompletePlaces(id)
        return if (position >= 0 && position < places.size) {
            places[position]
        } else {
            null
        }
    }

    private suspend fun getAnttPrices(axis: Int, info: RouteInfo): AnttPrices? {
        val body = AnttPricesBody(axis, info.let {
            when (it.distanceUnit) {
                "meters" -> {
                    it.distance / 1000f
                }
                else -> {
                    it.distance
                }
            }
        })
        return when (val result = anttRepository.getPrices(body)) {
            is Result.Success -> result.data
            else -> {
                // We say that we've got an error even if route info request is successful
                hasError.set(true)
                _calculating.postValue(false)
                null
            }
        }
    }

    private fun getSearchUiModel(): SearchUiModel {
        return if (_searchUiModel.value == null) {
            _searchUiModel.value = SearchUiModel()
            _searchUiModel.value!!
        } else {
            _searchUiModel.value!!
        }
    }

    private fun pushHistoryEntry(
        origin: Place,
        destination: Place,
        axis: Int,
        fuelConsumption: Float,
        fuelPrice: Float,
        routeInfo: RouteInfo,
        anttPrices: AnttPrices
    ) = viewModelScope.launch {
        val entry = HistoryEntry(
            origin = origin,
            destination = destination,
            axis = axis,
            fuelConsumption = fuelConsumption,
            fuelPrice = fuelPrice,
            routeInfo = routeInfo,
            anttPrices = anttPrices
        )
        historyRepository.insert(entry)
    }

    companion object {
        const val AUTOCOMPLETE_ORIGIN = 0
        const val AUTOCOMPLETE_DESTINATION = 1
        const val AUTOCOMPLETE_COUNT = 2
    }
}

enum class UiState { SEARCH, RESULTS, HISTORY }

data class SearchUiModel(
    var origin: Place? = null,
    var destination: Place? = null,
    var axis: Int = 2,
    var fuelConsumption: Float = 1.70f,
    var fuelPrice: Float = 3.97f
) {

    companion object {
        const val MIN_AXIS = 2
        const val MAX_AXIS = 9
    }

    fun isValid(): Boolean {
        return origin != null && destination != null && axis >= MIN_AXIS && axis <= MAX_AXIS
    }

    /**
     * NOTE:
     * This requires origin and destination places to not be null
     */
    fun toRouteBody(): RouteBody {
        return RouteBody(
            places = listOf(
                origin!!.toRequestPlace(),
                destination!!.toRequestPlace()
            ),
            fuelConsumption = fuelConsumption,
            fuelPrice = fuelPrice
        )
    }
}

data class ResultsUiModel(
    val origin: Place,
    val destination: Place,
    val axis: Int,
    val routeInfo: RouteInfo,
    val anttPrices: AnttPrices
)