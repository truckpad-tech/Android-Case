package dev.khalil.freightpad.model

import dev.khalil.freightpad.common.BRAZIL_CURRENCY_SYMBOL
import dev.khalil.freightpad.extensions.formatPrice
import dev.khalil.freightpad.extensions.formatUnit
import dev.khalil.freightpad.extensions.toKm
import dev.khalil.freightpad.extensions.toTime

enum class UiState {
  INFO,
  ROUTE,
  HISTORY
}

data class RouteUiModel(
  val axis: String,
  val fuelConsume: String,
  val fuelPrice: String,
  val points: List<Place>,
  val distance: String,
  val duration: String,
  val tollCount: String,
  val tollCost: String,
  val route: List<List<List<Double>>>?,
  val fuelNeeded: String,
  val fuelTotalCost: String,
  val totalCost: String,
  val refrigerated: String,
  val general: String,
  val bulk: String,
  val neoBulk: String,
  val dangerous: String
) {
  companion object {
    fun toRouteUiModel(
      routeResponse: RouteResponse,
      tictacResponse: TictacResponse,
      startDisplayName: String,
      destinationDisplayName: String,
      axisValue: Int,
      fuelConsume: Double,
      fuelPrice: Double
    ): RouteUiModel {

      val places = listOf(
        Place(startDisplayName, routeResponse.points.first().point),
        Place(destinationDisplayName, routeResponse.points.last().point))

      return RouteUiModel(
        axisValue.toString(),
        fuelConsume.toString(),
        fuelPrice.formatPrice(BRAZIL_CURRENCY_SYMBOL),
        places,
        routeResponse.distance.toKm(routeResponse.distanceUnit),
        routeResponse.duration.toTime(routeResponse.durationUnit),
        routeResponse.tollCount.toString(),
        routeResponse.tollCost.formatPrice(routeResponse.tollCostUnit),
        routeResponse.route,
        routeResponse.fuelUsage.formatUnit(routeResponse.fuelUsageUnit),
        routeResponse.fuelCost.formatPrice(routeResponse.fuelCostUnit),
        routeResponse.totalCost.formatPrice(routeResponse.fuelCostUnit),
        tictacResponse.refrigerated.formatPrice(routeResponse.fuelCostUnit),
        tictacResponse.general.formatPrice(routeResponse.fuelCostUnit),
        tictacResponse.bulk.formatPrice(routeResponse.fuelCostUnit),
        tictacResponse.neoBulk.formatPrice(routeResponse.fuelCostUnit),
        tictacResponse.dangerous.formatPrice(routeResponse.fuelCostUnit)
      )
    }
  }
}