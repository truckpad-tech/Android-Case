package com.isa.oliveira.truckerapp.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class getParametros(

    @SerializedName("cached")
                           @Expose
                           val cached: Boolean?,
    @SerializedName("fuel_usage")
                           @Expose
                           val fuel_usage: Double?,
    @SerializedName("fuel_usage_unit")
                           @Expose
                           val fuel_usage_unit: String?,
    @SerializedName("fuel_cost")
                           @Expose
                           val fuel_cost: Double?,
    @SerializedName("fuel_cost_unit")
                           @Expose
                           val fuel_cost_unit: String?,
    @SerializedName("total_cost")
                           @Expose
                           val total_cost: Double?,
    @SerializedName("points")
                           @Expose
                           val points: List<point1Parametros?>?,

    @SerializedName("distance")
                           @Expose
                           val distance: Int = 0,
    @SerializedName("distance_unit")
                           @Expose
                           val distance_unit: String?,
    @SerializedName("duration")
                           @Expose
                           val duration: String?,
    @SerializedName("duration_unit")
                           @Expose
                           val duration_unit: String?,
    @SerializedName("has_tolls")
                           @Expose
                           val has_tolls: Boolean?,
    @SerializedName("toll_count")
                           @Expose
                           val toll_count: Int?,
    @SerializedName("toll_cost")
                           @Expose
                           val toll_cost: Int?,
    @SerializedName("toll_cost_unit")
                           @Expose
                           val toll_cost_unit: String?,
    @SerializedName("route")
                           @Expose
                           val route: List<Double>?,
    @SerializedName("provider")
                           @Expose
                           val provider: String?
)