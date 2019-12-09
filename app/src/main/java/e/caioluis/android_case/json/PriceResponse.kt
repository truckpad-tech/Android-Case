package e.caioluis.android_case.json

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PriceResponse(

    @SerializedName("frigorificada")
    val frigorificada: Double,
    @SerializedName("geral")
    val geral: Double,
    @SerializedName("granel")
    val granel: Double,
    @SerializedName("neogranel")
    val neogranel: Double,
    @SerializedName("perigosa")
    val perigosa: Double

) : Serializable