package com.truckpadcase.calculatefreight.domain.model.remote

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Point (

    @SerializedName("point")
    val point: List<Double>,

    @SerializedName("provider")
    val provider : String
) : Serializable