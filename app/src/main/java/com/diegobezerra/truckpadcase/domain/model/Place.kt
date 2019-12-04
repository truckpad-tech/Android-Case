package com.diegobezerra.truckpadcase.domain.model

import com.google.gson.annotations.SerializedName

data class Place(
    @SerializedName("display_name")
    val displayName: String,
    val point: List<Double>
) {

    fun toRequestPlace(): RequestPlace {
        return RequestPlace(point)
    }

}

data class RequestPlace(
    val point: List<Double>
)