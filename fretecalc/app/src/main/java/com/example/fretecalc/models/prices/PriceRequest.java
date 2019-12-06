
package com.example.fretecalc.models.prices;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PriceRequest {

    @Expose
    @SerializedName("axis")
    private Long axis;
    @Expose
    @SerializedName("distance")
    private Double distance;
    @SerializedName("has_return_shipment")
    private Boolean hasReturnShipment;

    public PriceRequest(Long axis, Double distance, Boolean hasReturnShipment) {
        this.axis = axis;
        this.distance = distance;
        this.hasReturnShipment = hasReturnShipment;
    }

    public PriceRequest() {
    }

    public Long getAxis() {
        return axis;
    }

    public void setAxis(Long axis) {
        this.axis = axis;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Boolean getHasReturnShipment() {
        return hasReturnShipment;
    }

    public void setHasReturnShipment(Boolean hasReturnShipment) {
        this.hasReturnShipment = hasReturnShipment;
    }

}
