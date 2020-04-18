package dev.khalil.freightpad.common

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import java.util.Locale

const val ZERO = 0
val BRAZIL = Locale("pt", "BR")
val BRAZIL_GEO_LOCATION = LatLng(-14.235004, -51.92528)
val BRAZIL_BOUNDS = LatLngBounds(LatLng(-33.69111, -72.89583), LatLng(2.81972, -34.80861))
const val BRAZIL_CURRENCY_SYMBOL = "R$ "
const val LITER_SYMBOL = "L"

const val START_LOCATION_CODE = 1
const val DESTINATION_LOCATION_CODE = 2
const val START_LOCATION = START_LOCATION_CODE
const val DESTINATION_LOCATION = DESTINATION_LOCATION_CODE

const val ADDRESS_KEY = "address"
const val LAT_KEY = "latitude"
const val LONG_KEY = "longitude"

const val MIN_AXIS_VALUE = 2
const val MAX_AXIS_VALUE = 9
const val DEFAULT_AXIS_VALUE = 2

const val HOUR_FORMAT = "HH:mm"