package br.com.viniciuscoscia.truckpad.domain.entities

data class RouteCalc(
    val distance: Int,
    val distanceUnit: String,
    val placeOfOrigin: String,
    val placeOfDestiny: String,
    val duration: Int,
    val durationUnit: String,
    val tollCost: Double,
    val tollCostUnit: String,
    val necessaryFuel: Double,
    val necessaryFuelUnit: String,
    val totalFuelCost: Double,
    val totalFuelCostUnit: String,
    val totalCost: Double
)