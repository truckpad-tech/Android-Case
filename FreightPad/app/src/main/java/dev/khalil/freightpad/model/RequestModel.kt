package dev.khalil.freightpad.model

import com.google.gson.annotations.SerializedName

data class RouteRequest(
  @SerializedName("places")
  val places: List<PlaceRequest>,
  @SerializedName("fuel_consumption")
  val fuelConsumption: Double,
  @SerializedName("fuel_price")
  val fuelPrice: Double
)

data class PlaceRequest(
  @SerializedName("point")
  val point: List<Double>
)

data class TictacRequest(
  @SerializedName("axis")
  val axis: Int,
  @SerializedName("distance")
  val distance: Double,
  @SerializedName("has_return_shipment")
  val hasReturnShipment: Boolean
)

