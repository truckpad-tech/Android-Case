package com.fernandacadena.truckpad_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.Response.Listener
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_details.*
import org.json.JSONArray
import org.json.JSONObject

class DetailsActivity() : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val localizacaoInicial = intent.extras?.get("localizacaoInicial")
        val localizacaoFinal = intent.extras?.get("localizacaoFinal")

        calcularRotaButton.setOnClickListener {
            var numeroTrechos = numeroTrechosInput.text
            var valorCombustivel = valorCombustivelInput.text
            var consumoMedio = consumoMedioInput.text

            if(numeroTrechos != null && valorCombustivel != null && consumoMedio != null) {
                getRouteDetails(
                    localizacaoInicial as LatLng?,
                    localizacaoFinal as LatLng?,
                    numeroTrechos,
                    valorCombustivel,
                    consumoMedio
                )
            }
            else {
                if(numeroTrechos == null)
                    numeroTrechosInput.error = "Número de trechos inválido"
                if(valorCombustivel == null)
                    valorCombustivelInput.error = "Valor inválido"
                if(consumoMedio == null)
                    consumoMedioInput.error = "Número de trechos inválido"
            }
        }
    }

    private fun getRouteDetails(
        localizacaoInicial: LatLng?,
        localizacaoFinal: LatLng?,
        numeroTrechos: Editable,
        valorCombustivel: Editable,
        consumoMedio: Editable
    ) {
        val apiUrl = "https://geo.api.truckpad.io/v1/route"
        val jsonObject = JSONObject(
            " {\"places\":[{ \"point\":[${localizacaoInicial?.longitude},${localizacaoInicial?.latitude}] }," +
                    "{ \"point\":[${localizacaoFinal?.longitude},${localizacaoFinal?.latitude}] }]," +
                    "\"fuel_consumption\": $consumoMedio," +
                    "\"fuel_price\": $valorCombustivel }"
        )

        val jsonRequest = object : JsonObjectRequest(
            Request.Method.POST,
            apiUrl,
            jsonObject,
            Listener { response ->
                val distance = response.getInt("distance")

                calculateCosts(distance, numeroTrechos)
            },
            Response.ErrorListener { response ->
                Log.e("Error", response.cause.toString())
            }) {}

        Volley.newRequestQueue(this).add(jsonRequest)

    }

    private fun calculateCosts(distance: Int, numeroTrechos: Editable) {
        val apiUrl = "https://tictac.api.truckpad.io/v1/antt_price/all"
        val jsonObject = JSONObject(
            " {\"axis\":$numeroTrechos,\"distance\":$distance,\"has_return_shipment\":true}"
        )

        val jsonRequest = object : JsonObjectRequest(
            Request.Method.POST,
            apiUrl,
            jsonObject,
            Listener { response ->
                val geral = response.getDouble("geral")
                resultTextView.text = geral.toString()
            },
            Response.ErrorListener { response ->
                Log.d("Erro", response.cause.toString())
            }) {}

        Volley.newRequestQueue(this).add(jsonRequest);
    }
}
