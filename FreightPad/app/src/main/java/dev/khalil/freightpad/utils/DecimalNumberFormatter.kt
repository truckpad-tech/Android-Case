package dev.khalil.freightpad.utils

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import androidx.appcompat.widget.AppCompatEditText
import dev.khalil.freightpad.extensions.formatNumber

class DecimalNumberFormatter(
  private val editText: AppCompatEditText,
  private val currencySymbol: String = ""
) : TextWatcher {

  private val noDigitsRegex = "\\D+".toRegex()
  private var ignoreChanges = false
  private var latestText: String = ""

  override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
    if (ignoreChanges) {
      return
    }
    latestText = s.toString()
  }

  override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
    if (ignoreChanges) {
      return
    }

    var rawNumberInput = s.toString().replace(noDigitsRegex, "")
    editText.filters.forEach {
      if (it is InputFilter.LengthFilter && rawNumberInput.length == it.max) {
        rawNumberInput = latestText.replace(noDigitsRegex, "")
      }
    }

    try {
      val value = rawNumberInput.toDouble() / 100.0
      val formatted = value.formatNumber(currencySymbol)
      allowChangesAfter {
        editText.setText(formatted)
        editText.setSelection(formatted.length)
      }
    } catch (e: Exception) {
    }
  }

  override fun afterTextChanged(s: Editable) {
  }

  private fun allowChangesAfter(block: () -> Unit) {
    ignoreChanges = true
    block()
    ignoreChanges = false
  }
}