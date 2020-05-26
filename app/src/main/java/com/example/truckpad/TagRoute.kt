package com.example.truckpad

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName

class TagRoute {
    @SerializedName("body")
    var points = ArrayList<Point>()
    var distance:Double = 0.0
    var distance_unit:String = "meters"
    var duration:Int = 0
    var duration_unit:String = "seconds"
    var has_tolls:Boolean = false
    var toll_count:Int = 0
    var toll_cost:Double = 0.0
    var toll_cost_unit:String = "R$"
    var route = ArrayList<ArrayList<ArrayList<Double>>>()
    var provider:String = "Maplink"
    var cached:Boolean = true
    var fuel_usage:Double = 0.0
    var fuel_usage_unit:String = "liters"
    var fuel_cost:Double = 0.0
    var fuel_cost_unit:String = "R$"
    var total_cost:Double = 0.0

        get() = field

        set(value) {field = value}

}
