package com.isa.oliveira.truckerapp.Task

import android.app.Activity
import android.os.AsyncTask
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.isa.oliveira.truckerapp.Activity.MainActivity
import com.isa.oliveira.truckerapp.Fragment.DetalhesFragment
import com.isa.oliveira.truckerapp.Model.allDetails
import com.isa.oliveira.truckerapp.Model.getParametros
import com.isa.oliveira.truckerapp.Model.getValues
import com.isa.oliveira.truckerapp.R
import com.isa.oliveira.truckerapp.Utilities.HttpUtils
import com.isa.oliveira.truckerapp.Utilities.Util
import org.json.JSONObject


open class valuesTask(private val Id: String, var eixos: Int, var context: Activity, var auth: FirebaseAuth, var result1: String) : AsyncTask<Void?, Void?, String?>() {


    override fun doInBackground(vararg p0: Void?): String? {

        return try {

            var result = HttpUtils.post1(Util.BASE_URL_PRICE, Id)
            result

        } catch (e: Exception) {
            Log.e("ERROR", e.message)
            null
        }
    }


    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (result != null) {
            try {
                val serializer = Gson()
                var Lista = serializer.fromJson(result, getValues::class.java)
                var Lista2 = serializer.fromJson(result1, getParametros::class.java)
                if (Lista != null) {

                    var detalhe = DetalhesFragment()

                    if (!auth.currentUser!!.uid.isNullOrEmpty()) {
                        val database = FirebaseDatabase.getInstance()
                        val myRef = database.getReference().child(auth.currentUser!!.uid)
                            .child("Rotas").child((0..10000).random().toString())

                        var a = JSONObject(result)
                        var b = JSONObject(result1)
                        var combined = JSONObject()
                        combined.put("", a)
                        combined.put(" ", b)
                        var combined2 =
                            serializer.fromJson(combined.toString(), allDetails::class.java)
                        myRef.setValue(combined2)
                        detalhe.add(result1, result, eixos)
                    }
                    (context as MainActivity)!!.supportFragmentManager.beginTransaction()
                        .replace(R.id.container, detalhe)
                        .addToBackStack("")
                        .commit()

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

}