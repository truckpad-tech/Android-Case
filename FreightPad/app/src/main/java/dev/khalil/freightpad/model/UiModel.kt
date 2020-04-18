package dev.khalil.freightpad.model

import dev.khalil.freightpad.common.BRAZIL_CURRENCY_SYMBOL
import dev.khalil.freightpad.extensions.formatNumber
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
  val route: List<List<List<Double>>>,
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
        fuelPrice.formatNumber(BRAZIL_CURRENCY_SYMBOL),
        places,
        routeResponse.distance.toKm(routeResponse.distanceUnit),
        routeResponse.duration.toTime(routeResponse.durationUnit),
        routeResponse.tollCount.toString(),
        routeResponse.tollCost.formatNumber(BRAZIL_CURRENCY_SYMBOL),
        routeResponse.route,
        routeResponse.fuelUsage.toString(),
        routeResponse.fuelCost.formatNumber(BRAZIL_CURRENCY_SYMBOL),
        routeResponse.totalCost.formatNumber(BRAZIL_CURRENCY_SYMBOL),
        tictacResponse.refrigerated.formatNumber(BRAZIL_CURRENCY_SYMBOL),
        tictacResponse.general.formatNumber(BRAZIL_CURRENCY_SYMBOL),
        tictacResponse.bulk.formatNumber(BRAZIL_CURRENCY_SYMBOL),
        tictacResponse.neoBulk.formatNumber(BRAZIL_CURRENCY_SYMBOL),
        tictacResponse.dangerous.formatNumber(BRAZIL_CURRENCY_SYMBOL)
      )
      /*TODO Format FUELUSAGE number*/
    }
  }
}