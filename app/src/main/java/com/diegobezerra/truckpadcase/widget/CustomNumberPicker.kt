package com.diegobezerra.truckpadcase.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import androidx.core.content.res.getIntOrThrow
import com.diegobezerra.truckpadcase.R
import kotlinx.android.synthetic.main.layout_custom_number_picker.view.*

typealias NumberChangeListener = (Int) -> Unit

class CustomNumberPicker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var number: Int = 0
        set(value) {
            field = value.coerceIn(min, max).also {
                number_text.text = it.toString()
                listener?.invoke(it)
            }
        }

    var min: Int = 0

    var max: Int = 0

    private var listener: NumberChangeListener? = null

    init {
        inflate(context, R.layout.layout_custom_number_picker, this)
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL

        context.obtainStyledAttributes(attrs, R.styleable.CustomNumberPicker, defStyleAttr, 0)
            .apply {
                min = getIntOrThrow(R.styleable.CustomNumberPicker_cnpMin)
                max = getIntOrThrow(R.styleable.CustomNumberPicker_cnpMax)
                number = getIntOrThrow(R.styleable.CustomNumberPicker_cnpNumber)
                recycle()
            }

        button_minus.setOnClickListener {
            number -= 1
        }

        button_plus.setOnClickListener {
            number += 1
        }
    }

    fun setOnNumberChangeListener(l: NumberChangeListener) {
        listener = l
    }

}