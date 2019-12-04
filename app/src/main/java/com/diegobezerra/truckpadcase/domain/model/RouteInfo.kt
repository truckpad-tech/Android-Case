package com.diegobezerra.truckpadcase.domain.model

import com.google.gson.annotations.SerializedName

data class RouteInfo(
    val cached: Boolean,

    val distance: Float,

    @SerializedName("distance_unit")
    val distanceUnit: String,

    val duration: Int,

    @SerializedName("duration_unit")
    val durationUnit: String,

    @SerializedName("fuel_cost")
    val fuelCost: Float,

    @SerializedName("fuel_cost_unit")
    val fuelCostUnit: String,

    @SerializedName("fuel_usage")
    val fuelUsage: Float,

    @SerializedName("fuel_usage_unit")
    val fuelUsageUnit: String,

    @SerializedName("has_tolls")
    val hasTolls: Boolean,

    val points: List<Point>,

    val provider: String,

    val route: List<List<List<Double>>>,

    @SerializedName("toll_cost")
    val tollCost: Float,

    @SerializedName("toll_cost_unit")
    val tollCostUnit: String,

    @SerializedName("toll_count")
    val tollCount: Int,

    @SerializedName("total_cost")
    val totalCost: Float
)