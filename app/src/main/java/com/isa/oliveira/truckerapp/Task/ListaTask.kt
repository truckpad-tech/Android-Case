package com.isa.oliveira.truckerapp.Task

import android.os.AsyncTask
import android.util.Log
import com.isa.oliveira.truckerapp.Utilities.HttpUtils
import com.isa.oliveira.truckerapp.Utilities.Util


open class ListaTask(private val Id: String) :
    AsyncTask<Void?, Void?, String?>() {
     override fun doInBackground(vararg p0: Void?): String? {
        return try {
            HttpUtils.post(
                Util.BASE_URL_ROUTE, Id)
        } catch (e: Exception) {
            Log.e("ERROR", e.message)
            null
        }
    }


}