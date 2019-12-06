
package com.example.fretecalc.models.routes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Locality implements Parcelable {

    @Expose
    @SerializedName("point")
    private List<Double> point;

    public Locality() {
    }

    public Locality(List<Double> point) {
        this.point = point;
    }

    protected Locality(Parcel in) {
        this.point = new ArrayList<Double>();
        in.readList(point, Double[].class.getClassLoader());
    }

    public static final Creator<Locality> CREATOR = new Creator<Locality>() {
        @Override
        public Locality createFromParcel(Parcel in) {
            return new Locality(in);
        }

        @Override
        public Locality[] newArray(int size) {
            return new Locality[size];
        }
    };

    public List<Double> getPoint() {
        return point;
    }

    public void setPoint(List<Double> point) {
        this.point = point;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(point);
    }
}
