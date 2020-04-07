package com.truckpadcase.calculatefreight.domain.model.remote

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PriceRequest (

    @SerializedName("axis")
    val axis : Int,

    @SerializedName("distance")
    val distance : Double,

    @SerializedName("has_return_shipment")
    val has_return_shipment : Boolean
) : Serializable
