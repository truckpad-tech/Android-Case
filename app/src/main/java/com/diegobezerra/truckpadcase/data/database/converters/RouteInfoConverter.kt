package com.diegobezerra.truckpadcase.data.database.converters

import androidx.room.TypeConverter
import com.diegobezerra.truckpadcase.domain.model.RouteInfo
import com.google.gson.Gson

class RouteInfoConverter {

    @TypeConverter
    fun jsonToRouteInfo(value: String?): RouteInfo? {
        return Gson().fromJson(value, RouteInfo::class.java)
    }

    @TypeConverter
    fun routeInfoToJson(value: RouteInfo?): String? {
        return Gson().toJson(value)
    }

}