package com.example.truckpad

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Point {

    @SerializedName("places")
    @Expose
    var places:List<Points>

    @SerializedName("fuel_consumption")
    @Expose
    var fuel_comsumption:Int = 0

    @SerializedName("fuel_price")
    @Expose
    var fuel_price:Double = 0.0

    get() = field

    set(value) {field = value}

    constructor(places: List<Points>, fuel_comsumption: Int, fuel_price: Double) {
        this.places = places
        this.fuel_comsumption = fuel_comsumption
        this.fuel_price = fuel_price
    }
}