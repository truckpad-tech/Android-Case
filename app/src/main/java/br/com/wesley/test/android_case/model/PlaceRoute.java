package br.com.wesley.test.android_case.model;

import com.google.gson.annotations.SerializedName;

public class PlaceRoute {

    private PlaceRoutePoint[] places;
    @SerializedName("fuel_consumption")
    private double fuelConsumption;
    @SerializedName("fuel_price")
    private double fuelPrice;

    public PlaceRoutePoint[] getPlaces() {
        return places;
    }

    public void setPlaces(PlaceRoutePoint[] places) {
        this.places = places;
    }

    public double getFuelConsumption() {
        return fuelConsumption;
    }

    public void setFuelConsumption(double fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }

    public double getFuelPrice() {
        return fuelPrice;
    }

    public void setFuelPrice(double fuelPrice) {
        this.fuelPrice = fuelPrice;
    }

    public static class PlaceRoutePoint {

        private double[] point;

        public PlaceRoutePoint(double[] point) {
            this.point = point;
        }

        public double[] getPoint() {
            return point;
        }

        public void setPoint(double[] point) {
            this.point = point;
        }
    }
}
