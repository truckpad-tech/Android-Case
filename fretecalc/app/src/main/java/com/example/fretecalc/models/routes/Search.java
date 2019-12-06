package com.example.fretecalc.models.routes;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "searches", indices = @Index(value = "places", unique = true))
public class Search implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private Long id;
    @Embedded
    private RouteRequest request;
    private Long axis;
    private String originName;
    private String destinationName;

    @Ignore
    public Search(RouteRequest request, Long axis, String originName, String destinationName) {
        this.request = request;
        this.axis = axis;
        this.originName = originName;
        this.destinationName = destinationName;
    }

    public Search() {
    }

    protected Search(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        request = in.readParcelable(RouteRequest.class.getClassLoader());
        if (in.readByte() == 0) {
            axis = null;
        } else {
            axis = in.readLong();
        }
        originName = in.readString();
        destinationName = in.readString();
    }

    public static final Creator<Search> CREATOR = new Creator<Search>() {
        @Override
        public Search createFromParcel(Parcel in) {
            return new Search(in);
        }

        @Override
        public Search[] newArray(int size) {
            return new Search[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RouteRequest getRequest() {
        return request;
    }

    public void setRequest(RouteRequest request) {
        this.request = request;
    }

    public Long getAxis() {
        return axis;
    }

    public void setAxis(Long axis) {
        this.axis = axis;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(id);
        }
        parcel.writeParcelable(request, i);
        if (axis == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(axis);
        }
        parcel.writeString(originName);
        parcel.writeString(destinationName);
    }
}
