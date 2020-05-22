package com.example.truckpad
import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.model.*


class Inserir : AppCompatActivity(),OnMapReadyCallback,Contract.ViewMVP{
    private lateinit var Mapa: MapView
    private lateinit var map: GoogleMap
    private lateinit var camera:CameraUpdate
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private var nmarker = 0
    val PERMISSION_ID = 42
    lateinit var presenter:Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inserir)
        presenter = Presenter(this)

        Mapa = findViewById(R.id.Mapa)
        Mapa.onCreate(savedInstanceState)
        Mapa.getMapAsync(this)

        Mapa.getMapAsync(OnMapReadyCallback { mMap ->
            map = mMap

            map.setOnMapClickListener(OnMapClickListener { latLng ->
                AddMarker(latLng)
            })
        })

    }

    override fun getActivity():AppCompatActivity {
        return this
    }

    override fun AddMarker(latLng:LatLng){
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        if(nmarker == 0) {
            map.clear()
            markerOptions.title("Inicio da rota")
            presenter.setOrigin(latLng)
            camera = CameraUpdateFactory.newLatLngZoom(latLng, 15f)
            map.moveCamera(camera)
            map.animateCamera(camera)
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        } else {
            markerOptions.title("Final da Rota")
            presenter.setDest(latLng)
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            Alert()
        }
        if(nmarker < 2) {
            map.addMarker(markerOptions)
            nmarker++
        }
    }

    fun MyLocation(@Suppress("UNUSED_PARAMETER")v: View){
        getLastLocation()
    }

    @SuppressLint("MissingPermission")
    override fun getLastLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        presenter.setOrigin(LatLng(location.latitude,location.longitude))
                        AddMarker(presenter.getOrigin())
                    }
                }
            } else {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            presenter.setOrigin(LatLng(mLastLocation.latitude.dec(),mLastLocation.longitude.dec()))
            AddMarker(presenter.getOrigin())
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
    }


    override fun onMapReady(googleMap: GoogleMap){
    }

    override fun onResume(){
        super.onResume()
        Mapa.onResume()
    }

    fun Clear(v:View){
        map.clear()
        nmarker = 0
    }

    override fun onStart(){
        super.onStart()
        Mapa.onStart()
    }

    override fun onPause(){
        super.onPause()
        Mapa.onPause()
    }

    override fun onStop(){
        super.onStop()
        Mapa.onStop()
    }

    override fun onLowMemory(){
        super.onLowMemory()
        Mapa.onLowMemory()
    }

    override fun onDestroy(){
        super.onDestroy()
        Mapa.onDestroy()
        presenter.Destroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Mapa.onSaveInstanceState(outState)
    }

    override fun DrawPolyLine() {
        lateinit var line:Polyline
        for(i in 0..presenter.getTR().route.size-1){
            for(j in 0..presenter.getTR().route.get(i).size-2)
                line = map.addPolyline(PolylineOptions().add(LatLng(presenter.getTR().route.get(i).get(j).get(1),presenter.getTR().route.get(i).get(j).get(0)), LatLng(presenter.getTR().route.get(i).get(j+1).get(1),presenter.getTR().route.get(i).get(j+1).get(0))).width(5f).color(Color.RED))
        }
    }

    override fun Alert() {
        val L1 = LinearLayout(this)
        L1.setOrientation(LinearLayout.HORIZONTAL)
        L1.setLayoutParams(
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )
        val TA = TextView(this)
        TA.setLayoutParams(
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )
        TA.setTextColor(Color.BLACK)
        TA.setTextSize(18f)
        TA.setText(" Consumo de Combustível: ")
        val ETA = EditText(this)
        ETA.setLayoutParams(
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )
        ETA.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL
        L1.addView(TA)
        L1.addView(ETA)
        val L2 = LinearLayout(this)
        L2.setOrientation(LinearLayout.HORIZONTAL)
        L2.setLayoutParams(
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )
        val TB = TextView(this)
        TB.setLayoutParams(
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )
        TB.setTextColor(Color.BLACK)
        TB.setTextSize(18f)
        TB.setText(" Preço do Combustível:      ")
        val ETB = EditText(this)
        ETB.setLayoutParams(
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )
        ETB.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        L2.addView(TB)
        L2.addView(ETB)
        val L4 = LinearLayout(this)
        L4.setOrientation(LinearLayout.HORIZONTAL)
        L4.setLayoutParams(
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )
        val TC = TextView(this)
        TC.setLayoutParams(
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )
        TC.setTextColor(Color.BLACK)
        TC.setTextSize(18f)
        TC.setText(" Número de Eixos:      ")
        val axis = arrayOf("2","3","4","5","6","7","8","9")
        val adapter = ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,axis)
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        val SP1 = Spinner(this)
        SP1.adapter = adapter
        SP1.setSelection(0)
        SP1.setLayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT))
        SP1.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                presenter.setAxis(parent.getItemAtPosition(position).toString().toInt())
            }

            override fun onNothingSelected(parent: AdapterView<*>){
            }
        }
        L4.addView(TC)
        L4.addView(SP1)
        val L5 = LinearLayout(this)
        L5.setOrientation(LinearLayout.HORIZONTAL)
        L5.setLayoutParams(
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )
        val TD = TextView(this)
        TD.setLayoutParams(
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )
        TD.setTextColor(Color.BLACK)
        TD.setTextSize(18f)
        TD.setText(" Possui retorno de carga:      ")
        val SW = Switch(this)
        SW.setLayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT))
        SW.setOnCheckedChangeListener { buttonView, isChecked ->
           presenter.setHas_return_shipment(isChecked)
        }
        L5.addView(TD)
        L5.addView(SW)
        val L3 = LinearLayout(this)
        L3.setOrientation(LinearLayout.VERTICAL)
        L3.setLayoutParams(
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        )
        L3.addView(L1)
        L3.addView(L2)
        L3.addView(L4)
        L3.addView(L5)
        val alertadd = AlertDialog.Builder(this)
        alertadd.setMessage("Informações sobre o combustível")
        alertadd.setPositiveButton("OK") { dialog, which ->
            presenter.setFuelComsuption(ETA.text.toString().toInt())
            presenter.setFuel_price(ETB.text.toString().toDouble())
            presenter.Rota()
        }
        alertadd.setNegativeButton("Cancelar") { dialog, which ->
            map.clear()
            nmarker = 0
        }
        alertadd.setView(L3)
        alertadd.show()
    }

    override fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }

    fun requestPermissions() {
        ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),PERMISSION_ID)
    }

    override fun Exibe() {
        var TR:TagRoute = presenter.getTR()
        var FR:Freight = presenter.getFR()
        var L = LinearLayout(this)
        L.setOrientation(LinearLayout.VERTICAL)
        L.setLayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT))
        var T1 = TextView(this)
        T1.setLayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT))
        T1.setText("A distância total é "+TR.distance+" "+ TR.distance_unit)
        L.addView(T1)
        var T2 = TextView(this)
        T2.setLayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT))
        T2.setText("A duração do percurso é de aproximadamente "+TR.duration+" "+ TR.duration_unit)
        L.addView(T2)
        var T3 = TextView(this)
        T3.setLayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT))
        if(TR.has_tolls){
            T3.setText("O Trecho possui "+TR.toll_count+" com custo de"+ TR.toll_cost+ " "+ TR.toll_cost_unit)
        } else {
            T3.setText("O Trecho não possui pedágios")
        }
        L.addView(T3)
        var T4 = TextView(this)
        T4.setLayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT))
        T4.setText("O consumo de combustível será aproximadamente "+TR.fuel_usage+" "+ TR.fuel_usage_unit)
        L.addView(T4)
        var T5 = TextView(this)
        T5.setLayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT))
        T5.setText("O valor gasto com combustível será aproximadamente "+TR.fuel_cost+" "+ TR.fuel_cost_unit)
        L.addView(T5)
        var T6 = TextView(this)
        T6.setLayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT))
        T6.setText("Preço por carga frigorificada: "+FR.frigorificada+" R$")
        L.addView(T6)
        var T7 = TextView(this)
        T7.setLayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT))
        T7.setText("Preço por carga geral: "+FR.geral+" R$")
        L.addView(T7)
        var T8 = TextView(this)
        T8.setLayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT))
        T8.setText("Preço por carga à granel: "+FR.granel+" R$")
        L.addView(T8)
        var T9= TextView(this)
        T9.setLayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT))
        T9.setText("Preço por carga à neogranel: "+FR.neogranel+" R$")
        L.addView(T9)
        var T10 = TextView(this)
        T10.setLayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT))
        T10.setText("Preço por carga perigosa: "+FR.perigosa+" R$")
        L.addView(T10)
        val alertadd = AlertDialog.Builder(this)
        alertadd.setMessage("Informações sobre a rota")
        alertadd.setPositiveButton("OK") { dialog, which ->

        }
        alertadd.setNegativeButton("Cancelar") { dialog, which ->
            map.clear()
            nmarker = 0
        }
        alertadd.setView(L)
        alertadd.show()

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }

    fun History(v : View){
        var SV = ScrollView(this)
        var L = LinearLayout(this)
        L.setOrientation(LinearLayout.VERTICAL)
        L.setLayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT))
        var c: Cursor = presenter.getCursor()

        while(c.moveToNext()){
            var L2 = LinearLayout(this)
            L2.setOrientation(LinearLayout.VERTICAL)
            L2.setLayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT))
            var T1 = TextView(this)
            var T2 = TextView(this)
            var T3 = TextView(this)
            var T4 = TextView(this)
            var T5 = TextView(this)
            var T6 = TextView(this)
            T1.setText("Coordenada ponto inicial: ("+c.getDouble(c.getColumnIndex("LAT_INI"))+","+c.getDouble(c.getColumnIndex("LONG_INI"))+")")
            T2.setText("Coordenada ponto final: ("+c.getDouble(c.getColumnIndex("LAT_FIM"))+","+c.getDouble(c.getColumnIndex("LONG_FIM"))+")")
            T3.setText("Consumo de Combustível: "+c.getInt(c.getColumnIndex("FUEL_COMS")))
            T4.setText("Preço do Combustível: "+c.getDouble(c.getColumnIndex("FUEL_PRICE")))
            T5.setText("Número de Eixos: "+c.getInt(c.getColumnIndex("AXIS")))
            if(c.getInt(c.getColumnIndex("RETURN_SHIPMENT")) > 0){
                T6.setText("Possui retorno de carga")
            } else{
                T6.setText("Não possui retorno de carga")
            }
            L2.addView(T1)
            L2.addView(T2)
            L2.addView(T3)
            L2.addView(T4)
            L2.addView(T5)
            L2.addView(T6)
            L.addView(L2)
        }

        SV.addView(L)

        val alertadd = AlertDialog.Builder(this)
        alertadd.setMessage("Histórico")
        alertadd.setPositiveButton("OK") { dialog, which ->

        }
        alertadd.setNegativeButton("Cancelar") { dialog, which ->
        }
        alertadd.setView(SV)
        alertadd.show()
    }

}
