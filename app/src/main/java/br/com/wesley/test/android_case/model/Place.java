package br.com.wesley.test.android_case.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Place {

    private String address;
    @SerializedName("area_code")
    private String areaCode;
    private String city;
    private String country;
    @SerializedName("display_name")
    private String displayName;
    private String neighborhood;
    private double[] point;
    @SerializedName("postal_code")
    private String postalCode;
    private String state;
    @SerializedName("state_acronym")
    private String stateAcronym;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public double[] getPoint() {
        return point;
    }

    public void setPoint(double[] point) {
        this.point = point;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateAcronym() {
        return stateAcronym;
    }

    public void setStateAcronym(String stateAcronym) {
        this.stateAcronym = stateAcronym;
    }

    @NonNull
    @Override
    public String toString() {
        return getDisplayName();
    }
}
