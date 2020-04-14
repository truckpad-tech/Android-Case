package br.com.truckpad.waister.api

import com.google.gson.annotations.SerializedName

data class Calculate(
    @SerializedName("points") val points: List<Point> = ArrayList(),
    @SerializedName("distance") val distance: Int = 0,
    @SerializedName("distance_unit") val distance_unit: String = "",
    @SerializedName("duration") val duration: Int = 0,
    @SerializedName("duration_unit") val duration_unit: String = "",
    @SerializedName("has_tolls") val has_tolls: Boolean = false,
    @SerializedName("toll_count") val toll_count: Int = 0,
    @SerializedName("toll_cost") val toll_cost: Double = 0.0,
    @SerializedName("toll_cost_unit") val toll_cost_unit: String = "",
    @SerializedName("route") val route: List<List<List<Double>>>,
    @SerializedName("provider") val provider: String = "",
    @SerializedName("cached") val cached: Boolean = false,
    @SerializedName("fuel_usage") val fuel_usage: Double = 0.0,
    @SerializedName("fuel_usage_unit") val fuel_usage_unit: String = "",
    @SerializedName("fuel_cost") val fuel_cost: Double = 0.0,
    @SerializedName("fuel_cost_unit") val fuel_cost_unit: String = "",
    @SerializedName("total_cost") val total_cost: Double = 0.0
)