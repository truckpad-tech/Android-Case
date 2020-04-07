package com.truckpadcase.calculatefreight.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


public class Converters {

    @TypeConverter
    fun toRouteList(value: String?): List<List<List<Double?>?>?>? {
        val listType: Type = object :
            TypeToken<List<List<List<Double?>?>?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromRouteList(list: List<List<List<Double?>?>?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

}