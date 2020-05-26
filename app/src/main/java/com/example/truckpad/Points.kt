package com.example.truckpad

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Points {
    @SerializedName("point")
    @Expose
    var p:List<Double> = listOf(0.0,0.0)

    get() = field

    set(value) {field = value}

    constructor(p: List<Double>) {
        this.p = p
    }



}