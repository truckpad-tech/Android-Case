package br.com.viniciuscoscia.data.remote.model.route


import com.google.gson.annotations.SerializedName

data class RouteResponse(
    @SerializedName("points")
    val points: List<Point> = listOf(),
    @SerializedName("distance")
    val distance: Int = 0,
    @SerializedName("distance_unit")
    val distanceUnit: String = "",
    @SerializedName("duration")
    val duration: Int = 0,
    @SerializedName("duration_unit")
    val durationUnit: String = "",
    @SerializedName("has_tolls")
    val hasTolls: Boolean = false,
    @SerializedName("toll_count")
    val tollCount: Int = 0,
    @SerializedName("toll_cost")
    val tollCost: Double = 0.0,
    @SerializedName("toll_cost_unit")
    val tollCostUnit: String = "",
    @SerializedName("route")
    val route: List<List<List<Double>>> = listOf(),
    @SerializedName("provider")
    val provider: String = "",
    @SerializedName("cached")
    val cached: Boolean = false,
    @SerializedName("fuel_usage")
    val fuelUsage: Double = 0.0,
    @SerializedName("fuel_usage_unit")
    val fuelUsageUnit: String = "",
    @SerializedName("fuel_cost")
    val fuelCost: Double = 0.0,
    @SerializedName("fuel_cost_unit")
    val fuelCostUnit: String = "",
    @SerializedName("total_cost")
    val totalCost: Double = 0.0
)