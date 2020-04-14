package br.com.truckpad.waister.util

import java.text.NumberFormat
import java.util.*

fun Int.formatDistance(unit: String): String {
    val number = if (unit == "meters") this / 1000 else this
    return NumberFormat.getInstance().format(number) + " KM"
}

fun Int.formatTime(unit: String): String {
    val number = if (unit == "seconds") this / 60 / 60 else this
    return NumberFormat.getInstance().format(number) + " horas"
}

fun Double.formatFuel(): String {
    return NumberFormat.getInstance().format(this) + " L"
}

fun Double.formatPrice(): String {
    val locale = Locale("pt", "BR")
    return NumberFormat.getCurrencyInstance(locale).format(this)
}