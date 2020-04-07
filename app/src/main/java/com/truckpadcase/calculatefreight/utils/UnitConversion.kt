package com.truckpadcase.calculatefreight.utils

class UnitConversion {
    companion object {
        fun metersToKm(data : String) : String {
            val result = data.toDouble()/1000
            return "${result.toInt()} Km"
        }

        fun secondsToHours(data : String) : String{
            val hrs = data.toInt() / 3600
            return "$hrs Hr(s)"
        }

        fun replacePrices(data : String) : String {
            val value = data.toDouble()/1000
            val dec = data.toDouble() - value
            var str = dec.toString()
            val decResult = str[0] +""+str[1]
            return "R\$:${value.toInt()}.${decResult} + Pedágio"
        }
    }

}