package br.com.viniciuscoscia.data.remote.model.pricebycargotype


import com.google.gson.annotations.SerializedName

data class PriceByCargoTypeResponse(
    @SerializedName("frigorificada")
    val frigorificada: Double = 0.0,
    @SerializedName("geral")
    val geral: Double = 0.0,
    @SerializedName("granel")
    val granel: Double = 0.0,
    @SerializedName("neogranel")
    val neogranel: Double = 0.0,
    @SerializedName("perigosa")
    val perigosa: Double = 0.0
)