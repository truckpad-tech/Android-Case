package com.jonas.truckpadchallenge.search.data.request

import com.google.gson.annotations.SerializedName

data class RouteCalculation(
    @SerializedName("places") val places: List<Places>,
    @SerializedName("fuel_consumption") val fuelConsumption: Double,
    @SerializedName("fuel_price") val fuelPrice: Double
)

data class Places(
    @SerializedName("point") val point: List<Double>
)