package com.example.truckpad

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Travel {
    @SerializedName("axis")
    @Expose
    var axis:Int = 0
    @SerializedName("distance")
    @Expose
    var distance:Double = 0.0
    @SerializedName("has_return_shipment")
    @Expose
    var has_return_shipment:Boolean = false



    get() = field

    set(value) {field = value}

    constructor(axis: Int, distance: Double, has_return_shipment: Boolean) {
        this.axis = axis
        this.distance = distance
        this.has_return_shipment = has_return_shipment
    }
}