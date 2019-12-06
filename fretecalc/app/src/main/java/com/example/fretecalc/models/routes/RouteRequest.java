
package com.example.fretecalc.models.routes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RouteRequest implements Parcelable {

    @SerializedName("fuel_consumption")
    private Double fuelConsumption;
    @SerializedName("fuel_price")
    private Double fuelPrice;
    @Expose
    @SerializedName("places")
    private List<Locality> places;

    public RouteRequest(Double fuel_consumption, Double fuel_price, List<Locality> places) {
        this.fuelConsumption = fuel_consumption;
        this.fuelPrice = fuel_price;
        this.places = places;
    }

    public RouteRequest() {
    }

    protected RouteRequest(Parcel in) {
        if (in.readByte() == 0) {
            fuelConsumption = null;
        } else {
            fuelConsumption = in.readDouble();
        }
        if (in.readByte() == 0) {
            fuelPrice = null;
        } else {
            fuelPrice = in.readDouble();
        }
        places = in.createTypedArrayList(Locality.CREATOR);
    }

    public static final Creator<RouteRequest> CREATOR = new Creator<RouteRequest>() {
        @Override
        public RouteRequest createFromParcel(Parcel in) {
            return new RouteRequest(in);
        }

        @Override
        public RouteRequest[] newArray(int size) {
            return new RouteRequest[size];
        }
    };

    public Double getFuelConsumption() {
        return fuelConsumption;
    }

    public void setFuelConsumption(Double fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }

    public Double getFuelPrice() {
        return fuelPrice;
    }

    public void setFuelPrice(Double fuelPrice) {
        this.fuelPrice = fuelPrice;
    }

    public List<Locality> getPlaces() {
        return places;
    }

    public void setPlaces(List<Locality> places) {
        this.places = places;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (fuelConsumption == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(fuelConsumption);
        }
        if (fuelPrice == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(fuelPrice);
        }
        parcel.writeTypedList(places);
    }


}
