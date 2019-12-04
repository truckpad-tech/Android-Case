package e.caioluis.android_case.json

data class TransmissionEnv(

    var places: List<Places>,
    val fuel_consumption: Int,
    val fuel_price: Double
)

data class Places(

    val point: List<Double>
)