package com.diegobezerra.truckpadcase.widget

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.EditText
import com.diegobezerra.truckpadcase.ui.main.calculator.CURRENCY_FORMAT_WITHOUT_SIGN

typealias DecimalNumberChangeListener = (Double) -> Unit

class DecimalFormattedEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : EditText(context, attrs, defStyleAttr) {

    val decimalFormat = CURRENCY_FORMAT_WITHOUT_SIGN

    private val textChangerListener = DecimalNumberTextWatcher()
    private var listener: DecimalNumberChangeListener? = null

    init {
        addTextChangedListener(textChangerListener)
    }

    fun setOnDecimalChangeListener(l: DecimalNumberChangeListener) {
        listener = l
    }

    inner class DecimalNumberTextWatcher : TextWatcher {

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
            filters.forEach {
                if (it is InputFilter.LengthFilter && rawNumberInput.length == it.max) {
                    rawNumberInput = latestText.replace(noDigitsRegex, "")
                }
            }

            try {
                val value = rawNumberInput.toDouble() / 100.0
                val formatted = decimalFormat.format(value)
                allowChangesAfter {
                    setText(formatted)
                    setSelection(formatted.length)
                }
                listener?.invoke(value)
            } catch (e: Exception) {
                // No-op
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

}