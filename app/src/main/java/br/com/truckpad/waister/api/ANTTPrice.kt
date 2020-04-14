package br.com.truckpad.waister.api

import com.google.gson.annotations.SerializedName

data class ANTTPrice(
    @SerializedName("frigorificada") val refrigerated: Double,
    @SerializedName("geral") val general: Double,
    @SerializedName("granel") val bulk: Double,
    @SerializedName("neogranel") val neo_bulk: Double,
    @SerializedName("perigosa") val dangerous: Double
)