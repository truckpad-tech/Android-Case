package com.jonas.truckpadchallenge.core.data.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

typealias routeList = List<List<List<Double?>?>?>?
typealias addressList = List<Double>?

class DataConverter {
    @TypeConverter
    fun routeToJson(list: routeList): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun jsonToRoute(value: String?): routeList {
        val listType: Type = object : TypeToken<List<List<List<Double?>?>?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun addressToJson(list: addressList): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun jsonToAddress(value: String?): addressList {
        val listType: Type = object : TypeToken<List<Double>?>() {}.type
        return Gson().fromJson(value, listType)
    }
}