package com.truckpadcase.calculatefreight.domain.model.remote

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class RouteRequest (

    @SerializedName("places")
    val places : List<Locations>,

    @SerializedName("fuel_consumption")
    val fuel_consumption : Double,

    @SerializedName("fuel_price")
    val fuel_price : Double

) : Serializable