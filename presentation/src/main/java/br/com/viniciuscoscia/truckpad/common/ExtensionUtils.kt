package br.com.viniciuscoscia.truckpad.common

import java.text.SimpleDateFormat
import java.util.*

fun Date.toBrazilianDateTimeFormat(): String {
    val date = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")).format(this)
    val hour = SimpleDateFormat("HH:mm", Locale("pt", "BR")).format(this)
    return "$date\n$hour"
}