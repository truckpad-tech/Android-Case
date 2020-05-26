package com.example.truckpad

import retrofit2.http.*
import retrofit2.Call

interface RequestService {

    @POST("route")
    fun createRoute(@Body point:Point):Call<TagRoute>

    @POST("all")
    fun createFreight(@Body travel:Travel):Call<Freight>

}
