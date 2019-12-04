package com.diegobezerra.truckpadcase.data.database.converters

import androidx.room.TypeConverter
import com.diegobezerra.truckpadcase.domain.model.Place
import com.google.gson.Gson

class PlaceConverter {

    @TypeConverter
    fun jsonToPlace(value: String?): Place? {
        return Gson().fromJson(value, Place::class.java)
    }

    @TypeConverter
    fun placeToJson(place: Place?): String? {
        return Gson().toJson(place)
    }

}