package com.truckpadcase.calculatefreight.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.activity_request.*

object Constants {

    fun Activity.hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(destination_city_edt_txt.windowToken, 0);
    }

    /*-- UTILS --*/
    const val LOCATION_PERMISSION_REQUEST_CODE = 1


    /*-- DATABASE --*/
    const val DATABASE_NAME = "Freight-Data-Base"


    /*-- API SERVICE --*/
    const val BASE_ROUTE_URL = "https://geo.api.truckpad.io/"
    const val PRICE_ROUTE_URL = "https://tictac.api.truckpad.io/"


    /*-- SPLASH SCREEN  --*/
    const val SPLASH_TIME_OUT: Long = 3000 // 1 sec


}