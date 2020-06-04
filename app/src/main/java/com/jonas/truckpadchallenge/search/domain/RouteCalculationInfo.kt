package com.jonas.truckpadchallenge.search.domain

data class RouteCalculationInfo(
    val origin: Points,
    val destination: Points,
    val fuelConsumption: Double? = 7.50,
    val fuelPrice: Double? = 3.73
)

data class Points(
    val point: List<Double?>
)