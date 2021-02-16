package br.com.viniciuscoscia.data.remote.model.route


import com.google.gson.annotations.SerializedName

data class RequestPlace(
    @SerializedName("point")
    val point: List<Float> = listOf()
)