package com.isa.oliveira.truckerapp.Task

import android.app.Activity
import android.os.AsyncTask
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.isa.oliveira.truckerapp.Model.getParametros
import com.isa.oliveira.truckerapp.Utilities.HttpUtils
import com.isa.oliveira.truckerapp.Utilities.Util


open class ListaTask(private val Id: String, var eixos: Int, var context: Activity, var auth: FirebaseAuth) : AsyncTask<Void?, Void?, String?>() {


     override fun doInBackground(vararg p0: Void?): String? {

         return try{

             var result =  HttpUtils.post(Util.BASE_URL_ROUTE, Id)
             println(result)

             result
         }catch (e : Exception){
             Log.e("ERROR", e.message)
             null
         }
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (result != null) {
            try {
                val serializer = Gson()
                var Lista = serializer.fromJson(result, getParametros::class.java)
                if (Lista != null) {

                    val parameters = JsonObject()


                    parameters.addProperty("axis", eixos)
                    parameters.addProperty("distance", Lista.distance)
                    parameters.addProperty("has_return_shipment", true)


                    addValues(parameters.toString(),result)
                    println("para " + parameters)

                } else {
                    Util.showMessage(
                        context!!,
                        "Aviso",
                        "Sem acesso à internet, tente novamente!"
                    )
                }

            } catch (ex: Exception) {
                Util.showMessage(
                    context!!,
                    "Aviso",
                    "Sem acesso à internet, tente novamente!"
                )
            }
        }
    }

    fun addValues(parametros: String?, result1: String) {
        object : valuesTask(parametros!!,  eixos, context!!, auth, result1) {

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                if (result != null) {

                }
            }
        }.execute()
    }
}