package e.caioluis.android_case.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import e.caioluis.android_case.util.Constants
import java.io.Serializable

@Entity(tableName = Constants.DATABASE_TABLE)
class RouteResult(
    val initial_address: String,
    val final_address: String,
    val distance: Int,
    val duration: Int,
    val toll_count: Int,
    val toll_cost: Int,
    val fuel_usage: Double,
    val fuel_cost: Double,
    val total_cost: Double,
    val refrigerated: Double,
    val general: Double,
    val granel: Double,
    val neogranel: Double,
    val hazardous: Double
) : Serializable {

    @PrimaryKey(autoGenerate = true)
    var routeId : Int = 0
}