package com.jonas.truckpadchallenge.search.data.request

import com.google.gson.annotations.SerializedName

data class AnttCalculation(
    val axis: Int,
    val distance: Double,
    @SerializedName("has_return_shipment") val hasReturnShipment: Boolean
)