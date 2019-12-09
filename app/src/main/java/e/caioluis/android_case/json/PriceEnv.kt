package e.caioluis.android_case.json

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PriceEnv(

    @SerializedName("axis")
    val axis: Int,
    @SerializedName("distance")
    val distance: Double,
    @SerializedName("has_return_shipment")
    val has_return_shipment: Boolean

) : Serializable