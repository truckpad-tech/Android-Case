package br.com.viniciuscoscia.data.entities.pricebycargotype

import com.google.gson.annotations.SerializedName

data class PriceByCargoTypeRequest(
    val axis: Int = 0,
    val distance: Double = 0.0,
    @SerializedName("has_return_shipment")
    val hasReturnShipment: Boolean = false
)