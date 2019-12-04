package com.diegobezerra.truckpadcase.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_table")
data class HistoryEntry(
    @PrimaryKey
    var id: Int? = 0,
    val origin: Place,
    val destination: Place,
    val axis: Int,
    val fuelConsumption: Float,
    val fuelPrice: Float,
    val routeInfo: RouteInfo,
    val anttPrices: AnttPrices,
    val timestamp: Long = System.currentTimeMillis()
) {

    init {
        // Create a id from input values:
        // (origin, destination, axis, fuelConsumption and fuelPrice)
        id = createId()
    }

    private fun createId(): Int {
        var result = origin.hashCode()
        result = 31 * result + destination.hashCode()
        result = 31 * result + axis.hashCode()
        result = 31 * result + fuelConsumption.hashCode()
        result = 31 * result + fuelPrice.hashCode()
        return result
    }
}