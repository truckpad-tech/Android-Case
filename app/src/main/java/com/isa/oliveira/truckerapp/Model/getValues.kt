package com.isa.oliveira.truckerapp.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class getValues(

                           @SerializedName("frigorificada")
                           @Expose
                           val frigorificada: Double?,
                           @SerializedName("geral")
                           @Expose
                           val geral: Double?,
                           @SerializedName("granel")
                           @Expose
                           val granel: Double?,
                           @SerializedName("neogranel")
                           @Expose
                           val neogranel: Double?,
                           @SerializedName("perigosa")
                           @Expose
                           val perigosa: Double?
)