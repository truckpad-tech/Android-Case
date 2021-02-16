package br.com.viniciuscoscia.truckpad.common

import com.google.android.material.textfield.TextInputEditText

fun TextInputEditText.textNotBlank() = this.text.isNullOrBlank().not()

fun TextInputEditText.textToInt(): Int {
    return if (textNotBlank()) {
        text.toString().toInt()
    } else {
        0
    }
}

fun TextInputEditText.textToDouble(): Double {
    return if (textNotBlank()) {
        text.toString().toDouble()
    } else {
        0.0
    }
}