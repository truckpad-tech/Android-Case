package dev.khalil.freightpad.model

import com.google.gson.annotations.SerializedName

data class SearchLocationResponse(
  val places: List<Place>,
  val provider: String
)

data class RouteResponse(
  @SerializedName("points")
  val points: List<Point>,
  @SerializedName("distance")
  val distance: Int,
  @SerializedName("distance_unit")
  val distanceUnit: String?,
  @SerializedName("duration")
  val duration: Int,
  @SerializedName("duration_unit")
  val durationUnit: String?,
  @SerializedName("has_tolls")
  val hasTolls: Boolean,
  @SerializedName("toll_count")
  val tollCount: Int,
  @SerializedName("toll_cost")
  val tollCost: Double,
  @SerializedName("toll_cost_unit")
  val tollCostUnit: String?,
  @SerializedName("route")
  val route: List<List<List<Double>>>?,
  @SerializedName("provider")
  val provider: String?,
  @SerializedName("cached")
  val cached: Boolean,
  @SerializedName("fuel_usage")
  val fuelUsage: Double,
  @SerializedName("fuel_usage_unit")
  val fuelUsageUnit: String?,
  @SerializedName("fuel_cost")
  val fuelCost: Double,
  @SerializedName("fuel_cost_unit")
  val fuelCostUnit: String?,
  @SerializedName("total_cost")
  val totalCost: Double
)

data class Point(
  @SerializedName("point")
  val point: List<Double>,
  @SerializedName("provider")
  val provider: String?
)

data class TictacResponse(
  @SerializedName("frigorificada")
  val refrigerated: Double,
  @SerializedName("geral")
  val general: Double,
  @SerializedName("granel")
  val bulk: Double,
  @SerializedName("neogranel")
  val neoBulk: Double,
  @SerializedName("perigosa")
  val dangerous: Double
)


