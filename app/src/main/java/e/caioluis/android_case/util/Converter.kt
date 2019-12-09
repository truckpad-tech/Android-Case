package e.caioluis.android_case.util

import e.caioluis.android_case.util.Constants.METERS_TO_KM
import java.text.NumberFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun Int.intToString(): String {

    return try {
        this.toString()

    } catch (ex: Exception) {

        "null"
    }
}

fun Double.doubleToString(): String {

    return try {
        this.toString()

    } catch (ex: Exception) {

        "null"
    }
}

fun Int.convertMetersToKm(): String {

    return try {

        var value = this.toDouble()

        value /= METERS_TO_KM

        "$value Km"

    } catch (ex: Exception) {

        "null"
    }
}

fun Double.formatLiters(): String {

    return try {

        this.doubleToString() + " L"

    } catch (ex: Exception) {
        "null"
    }
}

fun Int.secondsToHours(): String {

    return try {

        val milliseconds = this * 1000.toLong()

        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds).toInt() % 24

        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds).toInt() % 60

        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds).toInt() % 60

        String.format("%dh %02dm% 02ds", hours, minutes, seconds)

    } catch (ex: Exception) {
        "null"
    }
}

fun Double?.toBRLCurrency(): String {

    return try {
        val value = this

        val numberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        return numberFormat.format(value ?: 0.0).replace("R$","R$ ")

    } catch (ex: Exception) {

        "null"
    }
}




