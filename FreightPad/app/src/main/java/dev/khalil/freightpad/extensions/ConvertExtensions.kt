package dev.khalil.freightpad.extensions

import dev.khalil.freightpad.common.BRAZIL
import dev.khalil.freightpad.common.HOUR_FORMAT
import java.text.SimpleDateFormat
import java.util.Date
import java.util.GregorianCalendar

fun Int.toKm(measureUnit: String): String {
  return when (measureUnit) {
    "meters" -> "${(this / 100.0)} KM"
    else     -> this.toString()
  }
}

fun Int.toKm(): Double {
  return this / 100.0
}

fun Int.toTime(timeUnit: String): String {
  return when (timeUnit) {
    "seconds" -> {
      val cal = GregorianCalendar(0, 0, 0, 0, 0, this)
      val dNow: Date = cal.time
      val ft = SimpleDateFormat(HOUR_FORMAT, BRAZIL)
      ft.format(dNow)
    }
    else      -> this.toString()
  }
}

