package com.diegobezerra.truckpadcase.ui.main.calculator

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.diegobezerra.truckpadcase.R
import com.diegobezerra.truckpadcase.util.getFormattedDuration
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*


val BRAZIL = Locale("pt", "BR")

val CURRENCY_FORMAT by lazy {
    val format = DecimalFormat.getCurrencyInstance() as DecimalFormat
    format.decimalFormatSymbols = DecimalFormatSymbols(BRAZIL).apply {
        currencySymbol = "R$ "
    }
    format
}

val CURRENCY_FORMAT_WITHOUT_SIGN by lazy {
    val format = DecimalFormat.getCurrencyInstance() as DecimalFormat
    format.decimalFormatSymbols = DecimalFormatSymbols(BRAZIL).apply {
        currencySymbol = ""
    }
    format
}

val LITERS_FORMAT by lazy {
    DecimalFormat("###,###0.00 'L'")
}

val KILOMETERS_FORMAT by lazy {
    DecimalFormat("###,###.### 'Km'")
}


@BindingAdapter("android:text", "textWhenEmpty")
fun setText(
    textView: TextView,
    text: String?,
    textWhenEmpty: String?
) {
    if (text.isNullOrEmpty()) {
        if (textWhenEmpty.isNullOrEmpty()) {
            textView.text = "-"
        } else {
            textView.text = textWhenEmpty
        }
    } else {
        textView.text = text
    }
}

@BindingAdapter("number", "hasTolls", requireAll = true)
fun setAnttText(
    textView: TextView,
    number: Number?,
    hasTolls: Boolean = false
) {
    setText(
        textView,
        number,
        "R$",
        "R$",
        if (hasTolls) textView.resources.getString(R.string.tools_suffix) else null
    )
}

@BindingAdapter("number", "unit", "defaultUnit", "suffix", requireAll = false)
fun setText(
    textView: TextView,
    number: Number?,
    unit: String?,
    defaultUnit: String?,
    suffix: String?
) {
    var usedUnit: String? = defaultUnit
    if (unit != null) {
        usedUnit = unit
    }
    var formatter: NumberFormat? = null
    when (usedUnit) {
        "seconds" -> {
            if (number == null || number.toInt() == 0) {
                textView.text = "-"
            } else {
                textView.text = getFormattedDuration(number.toLong())
            }
        }
        "liters" -> {
            formatter = LITERS_FORMAT
        }
        "meters" -> {
            formatter = KILOMETERS_FORMAT
        }
        "R$" -> {
            formatter = CURRENCY_FORMAT
        }
    }
    formatter?.let {
        var formatted = it.format(number)
        if (suffix != null) {
            formatted += " $suffix"
        }
        textView.text = formatted
    }
}

@BindingAdapter("decimal", "withSign", requireAll = false)
fun setText(
    textView: TextView,
    value: Float?,
    withSign: Boolean
) {
    textView.text = if (withSign) {
        CURRENCY_FORMAT.format(value)
    } else {
        CURRENCY_FORMAT_WITHOUT_SIGN.format(value)
    }
}