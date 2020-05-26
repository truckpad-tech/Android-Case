package com.example.truckpad

import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.LatLng

interface Contract {
    interface ViewMVP{
        fun Alert()
        fun DrawPolyLine()
        fun AddMarker(latLng: LatLng)
        fun getLastLocation()
        fun checkPermissions():Boolean
        fun isLocationEnabled():Boolean
        fun getActivity():AppCompatActivity
        fun Exibe()

    }
    interface Presenter {
        fun Rota()
        fun Freights()
        fun Destroy()
        fun getCursor():Cursor
    }
}