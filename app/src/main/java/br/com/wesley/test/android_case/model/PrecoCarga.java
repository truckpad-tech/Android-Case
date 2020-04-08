package br.com.wesley.test.android_case.model;

import com.google.gson.annotations.SerializedName;

public class PrecoCarga {

    private int axis;
    private double distance;
    @SerializedName("has_return_shipment")
    private boolean hasReturnShipment;

    public int getAxis() {
        return axis;
    }

    public void setAxis(int axis) {
        this.axis = axis;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public boolean isHasReturnShipment() {
        return hasReturnShipment;
    }

    public void setHasReturnShipment(boolean hasReturnShipment) {
        this.hasReturnShipment = hasReturnShipment;
    }
}
