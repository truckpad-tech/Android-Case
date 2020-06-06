package com.jonas.truckpadchallenge.search.domain.entities

data class RouteResult(
    val points: List<RoutePoints>,
    val distance: Double,
    val distanceUnit: String,
    val duration: Int,
    val durationUnit: String,
    val hasTolls: Boolean,
    val tollCount: Int,
    val tollCost: Int,
    val tollCostUnit: String,
    val route: List<List<List<Double>>>,
    val provider: String,
    val cached: Boolean,
    val fuelUsage: Double,
    val fuelUsageUnit: String,
    val fuelCost: Double,
    val fuelCostUnit: String,
    val totalCost: Double
)

data class RoutePoints(
    val point: List<Double>,
    val provider: String
)