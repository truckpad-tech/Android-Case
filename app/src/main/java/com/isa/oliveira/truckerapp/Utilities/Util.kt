package com.isa.oliveira.truckerapp.Utilities

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast

object Util {


    const val BASE_URL_ROUTE = "https://geo.api.truckpad.io/v1/route"

    const val BASE_URL_PRICE = "https://tictac.api.truckpad.io/v1/antt_price/all"


    fun showToast(context: Context, message: String?) {
        val defaultMessage = "Ocorreu um erro. Por favor, tente novamente mais tarde."
        val errorMsg = message ?: defaultMessage
        Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
    }

    fun showMessage(context: Context, title: String, message: String) {
        val dlgAlert = AlertDialog.Builder(context)
        dlgAlert.setMessage(message)
        dlgAlert.setTitle(title)
        dlgAlert.setPositiveButton("OK", null)
        dlgAlert.setCancelable(false)
        dlgAlert.create().show()
    }



}

