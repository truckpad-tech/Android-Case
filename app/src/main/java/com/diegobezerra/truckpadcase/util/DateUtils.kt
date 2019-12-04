package com.diegobezerra.truckpadcase.util

const val MINUTE_IN_SECONDS = 60L
const val HOUR_IN_SECONDS = 60L * MINUTE_IN_SECONDS
const val DAY_IN_SECONDS = 24L * HOUR_IN_SECONDS

fun getFormattedDuration(seconds: Long): String {
    val result = StringBuilder()
    var remaining = seconds
    val append = fun(time: Long, unit: String, unitInSeconds: Long) {
        if (remaining < unitInSeconds) {
            return
        }
        val qty = time / unitInSeconds
        result.append(qty.toString())
        if (qty == 1L) {
            result.append(" $unit")
        } else {
            result.append(" ${unit}s")
        }
        result.append(" ")
        remaining -= qty * unitInSeconds
    }
    append(remaining, "dia", DAY_IN_SECONDS)
    append(remaining, "hora", HOUR_IN_SECONDS)
    return result.toString().trim()
}