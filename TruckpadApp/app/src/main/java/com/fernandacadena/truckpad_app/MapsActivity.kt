package com.fernandacadena.truckpad_app

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.maps.android.PolyUtil
import kotlinx.android.synthetic.main.activity_maps.*
import org.json.JSONObject

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private var googleMap: GoogleMap? = null
    var locInicial : LatLng = LatLng(0.0,0.0)
    var locFinal : LatLng = LatLng(0.0,0.0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        Places.initialize(getApplicationContext(), resources.getString(R.string.API_KEY))
        val placesClient = Places.createClient(this)

        val localizacaoInicial =
            supportFragmentManager.findFragmentById(R.id.localizacaoInicial) as AutocompleteSupportFragment?

        localizacaoInicial!!.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))

        localizacaoInicial.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                if(place != null){
                    locInicial = place.latLng!!
                    googleMap!!.clear()
                    googleMap!!.addMarker(locInicial?.let { MarkerOptions().position(it) })
                    googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(locInicial, 14.5f))
                }
            }

            override fun onError(status: Status) {
                Toast.makeText(applicationContext,""+status.toString(), Toast.LENGTH_LONG).show();
            }
        })

        val localizacaoFinal =
            supportFragmentManager.findFragmentById(R.id.localizacaoFinal) as AutocompleteSupportFragment?

        localizacaoFinal!!.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))

        localizacaoFinal.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                if(place != null) {
                    locFinal = place.latLng!!
                    if(locInicial!=LatLng(0.0,0.0))
                        updateMap(googleMap, locInicial, locFinal)
                    else
                        updateMap(googleMap, null, locFinal)
                }
            }

            override fun onError(status: Status) {
                Toast.makeText(applicationContext,""+status.toString(), Toast.LENGTH_LONG).show();
            }
        })

        floatingActionButton.setOnClickListener { view ->
            if(locInicial != LatLng(0.0,0.0) && locFinal != LatLng(0.0,0.0)){
                val intent = Intent(view.context, DetailsActivity::class.java);
                intent.putExtra("localizacaoInicial", locInicial)
                intent.putExtra("localizacaoFinal", locFinal)
                startActivity(intent);
            }
        }

    }

    fun updateMap(googleMap: GoogleMap?, locInicial: LatLng?, locFinal: LatLng?) {
        this.googleMap = googleMap
        val latLngOrigin = locInicial
        val latLngDestination = locFinal
        val latlngBuilder = LatLngBounds.Builder()
        latlngBuilder.include(latLngOrigin)
        latlngBuilder.include(latLngDestination)
        this.googleMap!!.clear()
        this.googleMap!!.addMarker(latLngOrigin?.let { MarkerOptions().position(it) })
        this.googleMap!!.addMarker(latLngDestination?.let { MarkerOptions().position(it) })
        this.googleMap!!.moveCamera(CameraUpdateFactory.newLatLngBounds(latlngBuilder.build(),100))

        if(locInicial!=LatLng(0.0,0.0) && locFinal!=LatLng(0.0,0.0)){
            val path: MutableList<List<LatLng>> = ArrayList()
            val urlDirections = "https://maps.googleapis.com/maps/api/directions/json?origin=" +
                    locInicial!!.latitude + "," + locInicial!!.longitude +
                    "&destination=" +
                    locFinal!!.latitude + "," + locFinal!!.longitude +
                    "&key="+ resources.getString(R.string.API_KEY)
            val directionsRequest = object : StringRequest(Request.Method.GET, urlDirections, Response.Listener<String> {
                    response ->
                val jsonResponse = JSONObject(response)
                // Get routes
                val routes = jsonResponse.getJSONArray("routes")
                val legs = routes.getJSONObject(0).getJSONArray("legs")
                val steps = legs.getJSONObject(0).getJSONArray("steps")
                for (i in 0 until steps.length()) {
                    val points = steps.getJSONObject(i).getJSONObject("polyline").getString("points")
                    path.add(PolyUtil.decode(points))
                }
                for (i in 0 until path.size) {
                    this.googleMap!!.addPolyline(PolylineOptions().addAll(path[i]).color(Color.RED))
                }
            }, Response.ErrorListener {
                    _ ->
            }){}
            val requestQueue = Volley.newRequestQueue(this)
            requestQueue.add(directionsRequest)
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap

        val latLngOrigin = LatLng(-23.533773, -46.625290)
        this.googleMap!!.addMarker(MarkerOptions().position(latLngOrigin))
        this.googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOrigin, 14.5f))
    }
}