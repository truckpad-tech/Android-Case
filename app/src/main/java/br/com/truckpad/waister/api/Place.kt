package br.com.truckpad.waister.api

import com.google.gson.annotations.SerializedName

data class Place(
    @SerializedName("address") val address: String,
    @SerializedName("area_code") val area_code: Int,
    @SerializedName("city") val city: String,
    @SerializedName("country") val country: String,
    @SerializedName("display_name") val display_name: String,
    @SerializedName("neighborhood") val neighborhood: String,
    @SerializedName("number") val number: String,
    @SerializedName("point") val point: List<Double>,
    @SerializedName("postal_code") val postal_code: String,
    @SerializedName("state") val state: String,
    @SerializedName("state_acronym") val state_acronym: String
)