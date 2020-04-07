package com.truckpadcase.calculatefreight.domain.model.remote

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class PriceResponse (
    @SerializedName("frigorificada")
    val refrigerated : Double,

    @SerializedName("geral")
    val general : Double,

    @SerializedName("granel")
    val granel : Double,

    @SerializedName("neogranel")
    val neogranel : Double,

    @SerializedName("perigosa")
    val hazardous : Double
) : Serializable
