package com.jonas.truckpadchallenge.search.domain.entities

import java.io.Serializable

data class SearchResult(
    val points: List<SearchRoutePoints>,
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
    val totalCost: Double,
    val refrigerated: Double,
    val general: Double,
    val bulk: Double,
    val neogranel: Double,
    val dangerous: Double
) : Serializable

data class SearchRoutePoints(
    val point: List<Double>,
    val provider: String
) : Serializable