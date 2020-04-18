package dev.khalil.freightpad.extensions

import android.text.Editable
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

fun Editable?.removeMeasureUnit(): Double {
  val noDigitsRegex = "\\D+".toRegex()
  val rawNumberInput = this.toString().replace(noDigitsRegex, "")
  return rawNumberInput.toDouble() / 100.0
}