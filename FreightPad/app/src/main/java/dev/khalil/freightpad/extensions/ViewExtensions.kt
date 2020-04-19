package dev.khalil.freightpad.extensions

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE

fun View.visible() {
  this.visibility = VISIBLE
}

fun View.gone() {
  this.visibility = GONE
}