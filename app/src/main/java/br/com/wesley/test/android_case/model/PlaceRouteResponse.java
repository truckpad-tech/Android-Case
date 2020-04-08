package br.com.wesley.test.android_case.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaceRouteResponse {

    private PlaceRouteResponsePoint[] points;
    private long distance;
    @SerializedName("distance_unit")
    private String distanceUnit;
    private long duration;
    @SerializedName("duration_unit")
    private String durationUnit;
    @SerializedName("has_tolls")
    private boolean hasTolls;
    @SerializedName("toll_count")
    private int tollCount;
    @SerializedName("toll_cost")
    private double tollCost;
    @SerializedName("toll_cost_unit")
    private String tollCostUnit;
    private List<List<List<Double>>> route;
    private String provider;
    private boolean cached;
    @SerializedName("fuel_usage")
    private double fuelUsage;
    @SerializedName("fuel_usage_unit")
    private String fuelUsageUnit;
    @SerializedName("fuel_cost")
    private double fuelCost;
    @SerializedName("fuel_cost_unit")
    private String fuelCostUnit;
    @SerializedName("total_cost")
    private double totalCost;

    public PlaceRouteResponsePoint[] getPoints() {
        return points;
    }

    public void setPoints(PlaceRouteResponsePoint[] points) {
        this.points = points;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public String getDistanceUnit() {
        return distanceUnit;
    }

    public void setDistanceUnit(String distanceUnit) {
        this.distanceUnit = distanceUnit;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(String durationUnit) {
        this.durationUnit = durationUnit;
    }

    public boolean isHasTolls() {
        return hasTolls;
    }

    public void setHasTolls(boolean hasTolls) {
        this.hasTolls = hasTolls;
    }

    public int getTollCount() {
        return tollCount;
    }

    public void setTollCount(int tollCount) {
        this.tollCount = tollCount;
    }

    public double getTollCost() {
        return tollCost;
    }

    public void setTollCost(double tollCost) {
        this.tollCost = tollCost;
    }

    public String getTollCostUnit() {
        return tollCostUnit;
    }

    public void setTollCostUnit(String tollCostUnit) {
        this.tollCostUnit = tollCostUnit;
    }

    public List<List<List<Double>>> getRoute() {
        return route;
    }

    public void setRoute(List<List<List<Double>>> route) {
        this.route = route;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public boolean isCached() {
        return cached;
    }

    public void setCached(boolean cached) {
        this.cached = cached;
    }

    public double getFuelUsage() {
        return fuelUsage;
    }

    public void setFuelUsage(double fuelUsage) {
        this.fuelUsage = fuelUsage;
    }

    public String getFuelUsageUnit() {
        return fuelUsageUnit;
    }

    public void setFuelUsageUnit(String fuelUsageUnit) {
        this.fuelUsageUnit = fuelUsageUnit;
    }

    public double getFuelCost() {
        return fuelCost;
    }

    public void setFuelCost(double fuelCost) {
        this.fuelCost = fuelCost;
    }

    public String getFuelCostUnit() {
        return fuelCostUnit;
    }

    public void setFuelCostUnit(String fuelCostUnit) {
        this.fuelCostUnit = fuelCostUnit;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
}
