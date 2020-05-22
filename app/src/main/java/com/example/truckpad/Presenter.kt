package com.example.truckpad

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.location.Location
import android.os.Looper
import android.provider.Settings
import android.util.Log
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Presenter:Contract.Presenter {
    private var view:Contract.ViewMVP?
    private lateinit var origin:LatLng
    private lateinit var dest:LatLng
    private var fuel_comsuption:Int = 0
    private var fuel_price:Double = 0.0
    private lateinit var TG:TagRoute
    private var has_return_shipment:Boolean = false
    private var axis:Int = 0
    private lateinit var  freight:Freight
    private lateinit var db:DataBase
    private var ctx:Context

    constructor(view: Contract.ViewMVP) {
        this.view = view
        ctx = view.getActivity()
        db = DataBase(this.ctx)
    }


    override fun Rota() {
        lateinit var request:RequestService
        var c = ContentValues()

        val interceptor = HttpLoggingInterceptor()

        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit: Retrofit = Retrofit.Builder().baseUrl("https://geo.api.truckpad.io/v1/").client(client).addConverterFactory(GsonConverterFactory.create()).build()

        request  = retrofit.create(RequestService::class.java)

        c.put("LAT_INI",this.origin.latitude)
        c.put("LONG_INI",this.origin.longitude)
        c.put("LAT_FIM",this.dest.latitude)
        c.put("LONG_FIM",this.dest.longitude)
        c.put("FUEL_COMS",this.fuel_comsuption)
        c.put("FUEL_PRICE",this.fuel_price)
        c.put("AXIS",this.axis)
        if(has_return_shipment) {
            c.put("RETURN_SHIPMENT", 1)
        }else {
            c.put("RETURN_SHIPMENT",0)
        }

        db.Insert("INFORMATION",c)

        var points:List<Points> = listOf(Points(listOf(origin.longitude,origin.latitude)),Points(listOf(dest.longitude,dest.latitude)))

        var point = Point(points,fuel_comsuption,fuel_price)

        var call: Call<TagRoute> = request.createRoute(point)

        call.enqueue(object : Callback<TagRoute> {
            override fun onFailure(call: Call<TagRoute>, t: Throwable) {
                Log.i("NETWORK_STATUS",t.message)
            }

            override fun onResponse(call: Call<TagRoute>, response: Response<TagRoute>) {

                Log.i("NETWORK_STATUS",""+response.code())

                if(!response.isSuccessful){
                    return
                }

                response?.body()?.let {
                    TG = it
                    view?.let{
                        it.DrawPolyLine()
                    }
                    Freights()
                }
            }
        })

    }

    fun ChamaExibe() {
        this.view?.Exibe()
    }

    override fun Freights() {
        lateinit var request:RequestService

        val interceptor = HttpLoggingInterceptor()

        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit: Retrofit = Retrofit.Builder().baseUrl("https://tictac.api.truckpad.io/v1/antt_price/").client(client).addConverterFactory(GsonConverterFactory.create()).build()

        request  = retrofit.create(RequestService::class.java)

        var travel = Travel(axis,TG.distance,has_return_shipment)

        var call: Call<Freight> = request.createFreight(travel)

        call.enqueue(object : Callback<Freight> {
            override fun onFailure(call: Call<Freight>, t: Throwable) {
                Log.i("NETWORK_STATUS",t.message)
            }

            override fun onResponse(call: Call<Freight>, response: Response<Freight>) {

                Log.i("NETWORK_STATUS",""+response.code())

                if(!response.isSuccessful){
                    return
                }

                response?.body()?.let {
                    freight = it
                    ChamaExibe()
                }

            }
        })
    }

    override fun Destroy() {
        this.view?.let { this.view = null }
    }

    fun setOrigin(x: LatLng){
        this.origin = x
    }

    fun getOrigin(): LatLng{
        return this.origin
    }

    override fun getCursor():Cursor{
        val search = arrayOf("LAT_INI","LONG_INI","LAT_FIM","LONG_FIM","FUEL_COMS","FUEL_PRICE","AXIS","RETURN_SHIPMENT")
        return this.db.Search(search,"","")
    }

    fun setDest(x: LatLng){
        this.dest = x
    }

    fun getDest(): LatLng{
        return this.dest
    }

    fun setFuel_price(x: Double){
        this.fuel_price = x
    }


    fun setFuelComsuption(x: Int){
        this.fuel_comsuption = x
    }

    fun getFR():Freight{
        return this.freight
    }

    fun setHas_return_shipment(x: Boolean){
        this.has_return_shipment = x
    }

    fun setAxis(x: Int){
        this.axis = x
    }

    fun setFreight(x: Freight){
        this.freight = x
    }

    fun getTR():TagRoute{
        return TG
    }

}