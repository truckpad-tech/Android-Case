package com.isa.oliveira.truckerapp.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class sendParametros(

    @SerializedName("places")
                           @Expose
    val places: List<point1Parametros?>,
    @SerializedName("fuel_price")
                           @Expose
    val fuel_price: Double?,
    @SerializedName("fuel_consumption")
                           @Expose
    val fuel_consumption: Double?,
    @SerializedName("axis")
                           @Expose
    val eixos: Int?

)