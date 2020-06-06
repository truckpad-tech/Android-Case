package com.jonas.truckpadchallenge.search.data.response

import com.google.gson.annotations.SerializedName

data class RouteResponse(
    @SerializedName("points") val points: List<Points>,
    @SerializedName("distance") val distance: Double,
    @SerializedName("distance_unit") val distanceUnit: String,
    @SerializedName("duration") val duration: Int,
    @SerializedName("duration_unit") val durationUnit: String,
    @SerializedName("has_tolls") val hasTolls: Boolean,
    @SerializedName("toll_count") val tollCount: Int,
    @SerializedName("toll_cost") val tollCost: Int,
    @SerializedName("toll_cost_unit") val tollCostUnit: String,
    @SerializedName("route") val route: List<List<List<Double>>>,
    @SerializedName("provider") val provider: String,
    @SerializedName("cached") val cached: Boolean,
    @SerializedName("fuel_usage") val fuelUsage: Double,
    @SerializedName("fuel_usage_unit") val fuelUsageUnit: String,
    @SerializedName("fuel_cost") val fuelCost: Double,
    @SerializedName("fuel_cost_unit") val fuelCostUnit: String,
    @SerializedName("total_cost") val totalCost: Double
)

data class Points(
    @SerializedName("point") val point: List<Double>,
    @SerializedName("provider") val provider: String
)