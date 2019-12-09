package e.caioluis.android_case.json

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RouteEnv(

    @SerializedName("places")
    var places: List<Place>,
    @SerializedName("fuel_consumption")
    val fuel_consumption: Int,
    @SerializedName("fuel_price")
    val fuel_price: Double
) : Serializable

data class Place(
    @SerializedName("point")
    val point: List<Double>
) : Serializable