package com.jonas.truckpadchallenge.result.domain


data class LocationAddress(
    val address: String,
    val latLng: LatLng
)

data class LatLng(
    val latitude: Double,
    val longitude: Double
)