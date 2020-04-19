package dev.khalil.freightpad.extensions

import com.google.android.gms.maps.model.LatLng
import dev.khalil.freightpad.common.BRAZIL
import dev.khalil.freightpad.common.HOUR_FORMAT
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.GregorianCalendar

fun Int.toKm(measureUnit: String?): String {
  if (measureUnit.isNullOrEmpty()) {
    return "0 KM"
  }

  return when (measureUnit) {
    "meters" -> {
      val format = DecimalFormat.getNumberInstance(BRAZIL).apply {
        this.maximumFractionDigits = 2
        this.minimumFractionDigits = 2
      } as DecimalFormat
      format.decimalFormatSymbols.decimalSeparator = ','

      "${format.format(this / 100.0)} KM"
    }
    else     -> this.toString()
  }
}

fun Int.toKmInDouble(measureUnit: String?): Double {
  return when (measureUnit) {
    "meters" -> this / 1000.0
    else     -> this.toDouble()
  }
}

fun Int.toTime(timeUnit: String?): String {

  return when (timeUnit) {
    null,
    "seconds" -> {
      val cal = GregorianCalendar(0, 0, 0, 0, 0, this)
      val dNow: Date = cal.time
      val ft = SimpleDateFormat(HOUR_FORMAT, BRAZIL)
      ft.format(dNow)
    }
    else      -> this.toString()
  }
}

fun List<Double>.toLatLng(): LatLng? {
  return if (size == 2) {
    LatLng(this.last(), this.first())
  } else {
    null
  }
}
