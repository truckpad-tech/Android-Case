package dev.khalil.freightpad.model

import com.google.gson.annotations.SerializedName

data class Place(@SerializedName("display_name") val displayName: String, val point: List<Double>)