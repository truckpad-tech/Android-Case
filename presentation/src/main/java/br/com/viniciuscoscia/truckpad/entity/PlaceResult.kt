package br.com.viniciuscoscia.truckpad.entity

import android.location.Address
import br.com.viniciuscoscia.truckpad.common.SearchResult

data class PlaceResult(
    val searchResult: SearchResult,
    val address: Address? = null,
    val errorMessage: String = "",
    val places: List<String> = listOf()
)