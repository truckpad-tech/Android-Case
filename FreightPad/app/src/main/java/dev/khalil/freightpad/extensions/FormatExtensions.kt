package dev.khalil.freightpad.extensions

import android.text.Editable
import dev.khalil.freightpad.common.BRAZIL
import dev.khalil.freightpad.common.BRAZIL_CURRENCY_SYMBOL
import dev.khalil.freightpad.common.LITER_SYMBOL
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

fun Double.formatPrice(currencySymbol: String?): String {
  var symbol = BRAZIL_CURRENCY_SYMBOL
  if (!currencySymbol.isNullOrEmpty()) {
    symbol = currencySymbol
  }
  val format = DecimalFormat.getCurrencyInstance() as DecimalFormat
  format.decimalFormatSymbols = DecimalFormatSymbols(BRAZIL).apply {
    this.currencySymbol = symbol
  }
  return format.format(this)
}

fun Editable?.removeMeasureUnit(): Double {
  val noDigitsRegex = "\\D+".toRegex()
  val rawNumberInput = this.toString().replace(noDigitsRegex, "")
  return rawNumberInput.toDouble() / 100.0
}

fun Double.formatUnit(measureUnit: String?): String {
  var unit = LITER_SYMBOL
  if (!measureUnit.isNullOrEmpty() && measureUnit != "liters") {
    unit = measureUnit
  }
  return "$this $unit".replace(".", ",")
}