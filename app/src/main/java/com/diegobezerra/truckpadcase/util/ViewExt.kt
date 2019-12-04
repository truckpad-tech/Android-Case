package com.diegobezerra.truckpadcase.util

import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Filterable
import android.widget.ListAdapter
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import java.lang.Exception

@BindingAdapter("goneUnless")
fun goneUnless(view: View, visible: Boolean) {
    view.isVisible = visible
}

fun <T> AutoCompleteTextView.setAdapterAndShowDropdownIfNotEmpty(adapter: T) where T : ListAdapter, T : Filterable {
    setAdapter(adapter)
    if (!adapter.isEmpty && !isPopupShowing) {
        // This can cause exceptions on rotation.
        try {
            showDropDown()
        } catch (ignored: Exception) {

        }
    }
}