package br.com.viniciuscoscia.data.remote.model.route


import com.google.gson.annotations.SerializedName

data class Point(
    @SerializedName("point")
    val point: List<Double> = listOf(),
    @SerializedName("provider")
    val provider: String = ""
)