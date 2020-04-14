package br.com.truckpad.waister.domain

import android.os.Parcel
import android.os.Parcelable

class SearchPoint(
    val name: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<SearchPoint> {
            override fun createFromParcel(parcel: Parcel) = SearchPoint(parcel)
            override fun newArray(size: Int) = arrayOfNulls<SearchPoint>(size)
        }
    }

    private constructor(parcel: Parcel) : this(
        name = parcel.readString() ?: "",
        latitude = parcel.readDouble(),
        longitude = parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
    }

    override fun describeContents() = 0
}