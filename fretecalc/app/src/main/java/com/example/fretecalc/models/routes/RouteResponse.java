
package com.example.fretecalc.models.routes;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "routes")
public class RouteResponse {

    @PrimaryKey
    private Long routeId;
    @Expose
    private Boolean cached;
    @Expose
    private Double distance;
    @SerializedName("distance_unit")
    private String distanceUnit;
    @Expose
    private Long duration;
    @SerializedName("duration_unit")
    private String durationUnit;
    @SerializedName("fuel_cost")
    private Double fuelCost;
    @SerializedName("fuel_cost_unit")
    private String fuelCostUnit;
    @SerializedName("fuel_usage")
    private Double fuelUsage;
    @SerializedName("fuel_usage_unit")
    private String fuelUsageUnit;
    @SerializedName("has_tolls")
    private Boolean hasTolls;
    @Expose
    private List<Point> points;
    @Expose
    private String provider;
    @Expose
    private List<List<List<Double>>> route;
    @SerializedName("toll_cost")
    private Double tollCost;
    @SerializedName("toll_cost_unit")
    private String tollCostUnit;
    @SerializedName("toll_count")
    private Long tollCount;
    @SerializedName("total_cost")
    private Double totalCost;

    @Ignore
    public RouteResponse(Boolean cached, Double distance, String distanceUnit, Long duration, String durationUnit, Double fuelCost, String fuelCostUnit, Double fuelUsage, String fuelUsageUnit, Boolean hasTolls, List<Point> points, String provider, List<List<List<Double>>> route, Double tollCost, String tollCostUnit, Long tollCount, Double totalCost) {
        this.cached = cached;
        this.distance = distance;
        this.distanceUnit = distanceUnit;
        this.duration = duration;
        this.durationUnit = durationUnit;
        this.fuelCost = fuelCost;
        this.fuelCostUnit = fuelCostUnit;
        this.fuelUsage = fuelUsage;
        this.fuelUsageUnit = fuelUsageUnit;
        this.hasTolls = hasTolls;
        this.points = points;
        this.provider = provider;
        this.route = route;
        this.tollCost = tollCost;
        this.tollCostUnit = tollCostUnit;
        this.tollCount = tollCount;
        this.totalCost = totalCost;
    }

    public RouteResponse() {
    }

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public Boolean getCached() {
        return cached;
    }

    public void setCached(Boolean cached) {
        this.cached = cached;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getDistanceUnit() {
        return distanceUnit;
    }

    public void setDistanceUnit(String distanceUnit) {
        this.distanceUnit = distanceUnit;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(String durationUnit) {
        this.durationUnit = durationUnit;
    }

    public Double getFuelCost() {
        return fuelCost;
    }

    public void setFuelCost(Double fuelCost) {
        this.fuelCost = fuelCost;
    }

    public String getFuelCostUnit() {
        return fuelCostUnit;
    }

    public void setFuelCostUnit(String fuelCostUnit) {
        this.fuelCostUnit = fuelCostUnit;
    }

    public Double getFuelUsage() {
        return fuelUsage;
    }

    public void setFuelUsage(Double fuelUsage) {
        this.fuelUsage = fuelUsage;
    }

    public String getFuelUsageUnit() {
        return fuelUsageUnit;
    }

    public void setFuelUsageUnit(String fuelUsageUnit) {
        this.fuelUsageUnit = fuelUsageUnit;
    }

    public Boolean getHasTolls() {
        return hasTolls;
    }

    public void setHasTolls(Boolean hasTolls) {
        this.hasTolls = hasTolls;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public List<List<List<Double>>> getRoute() {
        return route;
    }

    public void setRoute(List<List<List<Double>>> route) {
        this.route = route;
    }

    public Double getTollCost() {
        return tollCost;
    }

    public void setTollCost(Double tollCost) {
        this.tollCost = tollCost;
    }

    public String getTollCostUnit() {
        return tollCostUnit;
    }

    public void setTollCostUnit(String tollCostUnit) {
        this.tollCostUnit = tollCostUnit;
    }

    public Long getTollCount() {
        return tollCount;
    }

    public void setTollCount(Long tollCount) {
        this.tollCount = tollCount;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

}
