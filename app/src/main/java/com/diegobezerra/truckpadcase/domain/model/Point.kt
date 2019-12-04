package com.diegobezerra.truckpadcase.domain.model

import com.google.android.gms.maps.model.LatLng

fun List<Double>.toLatLng(): LatLng? {
    return if (size == 2) {
        LatLng(this[1], this[0])
    } else {
      null
    }
}

data class Point(
    val point: List<Double>,
    val provider: String
)