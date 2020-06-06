package com.jonas.truckpadchallenge.core.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.jonas.truckpadchallenge.maps.domain.LatLng
import com.jonas.truckpadchallenge.maps.domain.LocationAddress
import io.reactivex.Maybe

open class LocationUtils(context: Context) {

    private val geocode = Geocoder(context)

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }


    @SuppressLint("MissingPermission")
    open fun getCurrentLocation(): Maybe<Location> {
        return Maybe.create { emitter ->
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                emitter.onSuccess(location)
            }
        }
    }

    open fun getLocationByLatLng(location: Location): LocationAddress {
        val result = geocode.getFromLocation(location.latitude, location.longitude, 1)
        return LocationAddress(
            result[0].getAddressLine(0) ?: "",
            LatLng(location.latitude, location.longitude)
        )
    }
}