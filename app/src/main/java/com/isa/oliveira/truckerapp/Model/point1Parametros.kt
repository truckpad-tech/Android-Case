package com.isa.oliveira.truckerapp.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class point1Parametros(

                           @SerializedName("point")
                           @Expose
                           val point: List<Double>

)