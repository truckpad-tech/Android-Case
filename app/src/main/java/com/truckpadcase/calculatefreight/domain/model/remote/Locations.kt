package com.truckpadcase.calculatefreight.domain.model.remote

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Locations (
    @SerializedName("point")
    val point: List<Double>
) : Serializable
