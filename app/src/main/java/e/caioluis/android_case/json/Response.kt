package e.caioluis.android_case.json

import com.google.gson.annotations.SerializedName

data class Response(

    @SerializedName("points")
    val points: List<Points>,
    @SerializedName("distance")
    val distance: Int,
    @SerializedName("distance_unit")
    val distance_unit: String,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("duration_unit")
    val duration_unit: String,
    @SerializedName("has_tolls")
    val has_tolls: Boolean,
    @SerializedName("toll_count")
    val toll_count: Int,
    @SerializedName("toll_cost")
    val toll_cost: Int,
    @SerializedName("toll_cost_unit")
    val toll_cost_unit: String,
    @SerializedName("route")
    val route: List<List<List<Double>>>,
    @SerializedName("provider")
    val provider: String,
    @SerializedName("cached")
    val cached: Boolean,
    @SerializedName("fuel_usage")
    val fuel_usage: Double,
    @SerializedName("fuel_usage_unit")
    val fuel_usage_unit: String,
    @SerializedName("fuel_cost")
    val fuel_cost: Double,
    @SerializedName("fuel_cost_unit")
    val fuel_cost_unit: String,
    @SerializedName("total_cost")
    val total_cost: Double
)

data class Points(

    @SerializedName("point")
    val point: List<Double>,
    @SerializedName("provider")
    val provider: String
)