package dev.khalil.freightpad.extensions

import dev.khalil.freightpad.common.BRAZIL
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

fun Double.formatNumber(currencySymbol: String): String {
  val format = DecimalFormat.getCurrencyInstance() as DecimalFormat
  format.decimalFormatSymbols = DecimalFormatSymbols(BRAZIL).apply {
    this.currencySymbol = currencySymbol
  }
  return format.format(this)
}