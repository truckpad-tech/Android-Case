package com.correia.app_truckpad.activities

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ProgressBar
import co.metalab.asyncawait.async
import com.correia.app_truckpad.R
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


val TIMEOUT = 10*10000

class MainActivity : AppCompatActivity() {

    var address: List<Address>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.INVISIBLE

        btlocalizacao.setOnClickListener{

            progressBar.visibility = View.VISIBLE
            var origemet = eTpoint1.text.toString().trim()
            val destinoet = eTpoint2.text.toString().trim()
            val eixoset = eTeixos.text.toString().trim()
            val consumoet = eTConsumo.text.toString().trim()
            val precoDieselet = eTPrecoDiesel.text.toString().trim()

            if(origemet.isEmpty()){
                eTpoint1.error = "Campo Obrigatório"
                eTpoint1.requestFocus()
                progressBar.visibility = View.INVISIBLE
                return@setOnClickListener
            }

            if(destinoet.isEmpty()){
                eTpoint2.error = "Campo Obrigatório"
                eTpoint2.requestFocus()
                progressBar.visibility = View.INVISIBLE
                return@setOnClickListener
            }

            if(eixoset.isEmpty()){
                eTeixos.error = "Campo Obrigatório"
                eTeixos.requestFocus()
                progressBar.visibility = View.INVISIBLE
                return@setOnClickListener
            }

            if(eixoset.toInt()<2 || eixoset.toInt()>9){
                eTeixos.error = "Valor para nº de eixos incorreto"
                eTeixos.requestFocus()
                progressBar.visibility = View.INVISIBLE
                return@setOnClickListener
            }


            if(consumoet.isEmpty()){
                eTConsumo.error = "Campo Obrigatório"
                eTConsumo.requestFocus()
                progressBar.visibility = View.INVISIBLE
                return@setOnClickListener
            }

            if(precoDieselet.isEmpty()){
                eTPrecoDiesel.error = "Campo Obrigatório"
                eTPrecoDiesel.requestFocus()
                progressBar.visibility = View.INVISIBLE
                return@setOnClickListener
            }

            //Localização latitude e longitude
            val geocodeOrigem: Geocoder = Geocoder(this, Locale.getDefault())
            val localizacaoOrigem = origemet
            address = geocodeOrigem.getFromLocationName(localizacaoOrigem, 5)
            val locationOrigem = (address as MutableList<Address>?)?.get(0)

            val geocodeDestino: Geocoder = Geocoder(this, Locale.getDefault())
            val localizacaoDestino = destinoet
            address = geocodeDestino.getFromLocationName(localizacaoDestino, 5)
            val locationDestino = (address as MutableList<Address>?)?.get(0)



            var intPointOrigem1 = locationOrigem?.latitude
            var intPointOrigem2 = locationOrigem?.longitude
            var intPointDestino1 = locationDestino?.latitude
            var intPointDestino2 = locationDestino?.longitude

            var aux = precoDieselet
            val re = Regex("[^A-Za-z0-9 ]")
            aux = re.replace(aux, ".")

            var intEixo = eixoset.toInt()
            var intconsumo = consumoet.toInt()
            var doubleprecoDiesel = aux.toDouble()

            //JSON
            val json = JSONObject()

            val point1 = JSONArray()
            point1.put(intPointOrigem2)
            point1.put(intPointOrigem1)
            val jsonPoints1 = JSONObject()
            jsonPoints1.put("point", point1)

            val point2 = JSONArray()
            point2.put(intPointDestino2)
            point2.put(intPointDestino1)
            val jsonPoints2 = JSONObject()
            jsonPoints2.put("point", point2)

            val arr = JSONArray()
            arr.put(jsonPoints1)
            arr.put(jsonPoints2)
            json.put("places", arr)


            json.put("fuel_consumption", intconsumo)
            json.put("fuel_price", doubleprecoDiesel)


            val jsonAnttPrice = JSONObject()
            jsonAnttPrice.put("axis", eixoset.toInt())
            jsonAnttPrice.put("has_return_shipment", true)

            //Chamada POST
            val intent = Intent(applicationContext, ResultActivity::class.java)
            async {

                HttpTask({
                    if (it == null) {
                        println("connection error")
                        return@HttpTask
                    }

                    var data = it
                    val jsonObjData = JSONObject(data)
                    var distanciaInt: Int = jsonObjData.getInt("distance")
                    var distanciaDouble: Double = (distanciaInt/1000).toDouble()
                    jsonAnttPrice.put("distance", distanciaDouble)

                    intent.putExtra("origem", locationOrigem?.getAddressLine(0))
                    intent.putExtra("destino", locationDestino?.getAddressLine(0))
                    intent.putExtra("eixo", eixoset)
                    intent.putExtra("distancia", jsonObjData.getInt("distance").toString())
                    intent.putExtra("duracao", jsonObjData.getInt("duration").toString())
                    intent.putExtra("pedagio", jsonObjData.getDouble("toll_cost").toString())
                    intent.putExtra("combustivel_necessario", jsonObjData.getDouble("fuel_usage").toString())
                    intent.putExtra("total_combustivel", jsonObjData.getDouble("fuel_cost").toString())
                    intent.putExtra("total", jsonObjData.getDouble("total_cost").toString())

                    HttpTask({
                        if (it == null) {
                            println("connection error")
                            return@HttpTask
                        }
                        var frete = it

                        val jsonObjFrete = JSONObject(frete)
                        intent.putExtra("frigorificada", jsonObjFrete.getDouble("frigorificada").toString())
                        intent.putExtra("geral", jsonObjFrete.getDouble("geral").toString())
                        intent.putExtra("granel", jsonObjFrete.getDouble("granel").toString())
                        intent.putExtra("neogranel", jsonObjFrete.getDouble("neogranel").toString())
                        intent.putExtra("perigosa", jsonObjFrete.getDouble("perigosa").toString())
                        progressBar.visibility = View.INVISIBLE
                        startActivity(intent)
                    } ).execute("POST", "https://tictac.api.truckpad.io/v1/antt_price/all", jsonAnttPrice.toString())

                } ).execute("POST", "https://geo.api.truckpad.io/v1/route", json.toString())
            }

        }
    }

    class HttpTask(callback: (String?) -> Unit) : AsyncTask<String, Unit, String>()  {

        var callback = callback

        override fun doInBackground(vararg params: String): String? {
            val url = URL(params[1])
            val httpClient = url.openConnection() as HttpURLConnection
            httpClient.setReadTimeout(TIMEOUT)
            httpClient.setConnectTimeout(TIMEOUT)
            httpClient.requestMethod = params[0]

            if (params[0] == "POST") {
                httpClient.instanceFollowRedirects = false
                httpClient.doOutput = true
                httpClient.doInput = true
                httpClient.useCaches = false
                httpClient.setRequestProperty("Content-Type", "application/json; charset=utf-8")
            }
            try {
                if (params[0] == "POST") {
                    httpClient.connect()
                    val os = httpClient.getOutputStream()
                    val writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
                    writer.write(params[2])
                    writer.flush()
                    writer.close()
                    os.close()
                }
                if (httpClient.responseCode == HttpURLConnection.HTTP_OK) {
                    val stream = BufferedInputStream(httpClient.inputStream)
                    val data: String = readStream(inputStream = stream)
                    return data
                } else {
                    println("ERROR ${httpClient.responseCode}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                httpClient.disconnect()
            }

            return null
        }

        fun readStream(inputStream: BufferedInputStream): String {
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            bufferedReader.forEachLine { stringBuilder.append(it) }
            return stringBuilder.toString()
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            callback(result)
        }
    }

    companion object {
        val TAG = "MainActivity"
    }
}

