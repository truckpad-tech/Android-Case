package com.jonas.truckpadchallenge.search.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "search_result")
data class SearchResult(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val originAddress: List<Double>,
    val destinationAddress: List<Double>,
    val distance: Double,
    val distanceUnit: String,
    val duration: Int,
    val durationUnit: String,
    val hasTolls: Boolean,
    val tollCount: Int,
    val tollCost: Int,
    val tollCostUnit: String,
    val route: List<List<List<Double>>>,
    val provider: String,
    val cached: Boolean,
    val fuelUsage: Double,
    val fuelUsageUnit: String,
    val fuelCost: Double,
    val fuelCostUnit: String,
    val totalCost: Double,
    val refrigerated: Double,
    val general: Double,
    val bulk: Double,
    val neogranel: Double,
    val dangerous: Double
) : Serializable