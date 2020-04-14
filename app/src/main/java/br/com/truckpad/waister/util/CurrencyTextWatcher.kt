package br.com.truckpad.waister.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.text.DecimalFormat

class CurrencyTextWatcher(
    private val editText: EditText,
    private val prefix: String = ""
) : TextWatcher {

    private var current = ""

    override fun onTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    @Synchronized
    override fun afterTextChanged(s: Editable?) {
        val target = s?.toString() ?: ""

        if (target != current) {
            editText.removeTextChangedListener(this)

            val numbers = target.replace("[^\\d]".toRegex(), "")
            val parsed = numbers.toDouble()
            var formatted = DecimalFormat("###,###.00").format(parsed / 100)

            if (parsed < 100)
                formatted = "0$formatted"

            if (prefix.isNotEmpty())
                formatted = "$prefix $formatted"

            current = formatted
            editText.setText(formatted)
            editText.setSelection(formatted.length)

            editText.addTextChangedListener(this)
        }
    }

}