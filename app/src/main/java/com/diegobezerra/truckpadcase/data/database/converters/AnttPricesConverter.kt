package com.diegobezerra.truckpadcase.data.database.converters

import androidx.room.TypeConverter
import com.diegobezerra.truckpadcase.domain.model.AnttPrices
import com.google.gson.Gson

class AnttPricesConverter {

    @TypeConverter
    fun jsonToAnttPrices(value: String?): AnttPrices? {
        return Gson().fromJson(value, AnttPrices::class.java)
    }

    @TypeConverter
    fun anttPricesToJson(value: AnttPrices?): String? {
        return Gson().toJson(value)
    }

}