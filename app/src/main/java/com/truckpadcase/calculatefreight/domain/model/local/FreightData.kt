package com.truckpadcase.calculatefreight.domain.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class FreightData (
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    var initial_address: String,
    var final_address: String,
    var distance: Double,
    val route: List<List<List<Double>>> = emptyList(),
    var axes: Int,
    var duration: Int,
    var toll_count: Int,
    var toll_cost: Int,
    var fuel_usage: Double,
    var fuel_cost: Double,
    var total_cost: Double,
    var refrigerated: Double,
    var general: Double,
    var granel: Double,
    var neogranel: Double,
    var hazardous: Double
)  : Serializable {

}