package com.jonas.truckpadchallenge.search.data.response

import com.google.gson.annotations.SerializedName

data class AnttResponse(
    @SerializedName("frigorificada") val refrigerated: Double,
    @SerializedName("geral") val general: Double,
    @SerializedName("granel") val bulk: Double,
    @SerializedName("neogranel") val neogranel: Double,
    @SerializedName("perigosa") val dangerous: Double
)