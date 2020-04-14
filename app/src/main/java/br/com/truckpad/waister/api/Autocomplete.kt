package br.com.truckpad.waister.api

import com.google.gson.annotations.SerializedName

data class Autocomplete(
    @SerializedName("places") val places: List<Place>,
    @SerializedName("provider") val provider: String
)