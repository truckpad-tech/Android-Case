package br.com.meutruck.truckmap.Routes;

import com.google.gson.JsonObject;

import org.json.JSONObject;

public class Route {
    public int id, eixos, tollCount;
    public double distance, duration, fuelKm, fuelValue, tollCost, fuelUsage, fuelCost, originPositionLatitude, originPositionLongitude,
            destinationPositionLatitude, destinationPositionLongitude, priceFrigorificada, priceGeral, priceGranel, priceNeogranel, pricePerigosa;
    public String hasTolls;
    public String origin, destination, date;

    public Route(int id, int eixos, int tollCount, double distance, double duration, double fuelKm, double fuelValue, double tollCost, double fuelUsage, double fuelCost, double originPositionLatitude, double originPositionLongitude, double destinationPositionLatitude, double destinationPositionLongitude, double priceFrigorificada, double priceGeral, double priceGranel, double priceNeogranel, double pricePerigosa, String hasTolls, String origin, String destination, String date) {
        this.id = id;
        this.eixos = eixos;
        this.tollCount = tollCount;
        this.distance = distance;
        this.duration = duration;
        this.fuelKm = fuelKm;
        this.fuelValue = fuelValue;
        this.tollCost = tollCost;
        this.fuelUsage = fuelUsage;
        this.fuelCost = fuelCost;
        this.originPositionLatitude = originPositionLatitude;
        this.originPositionLongitude = originPositionLongitude;
        this.destinationPositionLatitude = destinationPositionLatitude;
        this.destinationPositionLongitude = destinationPositionLongitude;
        this.priceFrigorificada = priceFrigorificada;
        this.priceGeral = priceGeral;
        this.priceGranel = priceGranel;
        this.priceNeogranel = priceNeogranel;
        this.pricePerigosa = pricePerigosa;
        this.hasTolls = hasTolls;
        this.origin = origin;
        this.destination = destination;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEixos() {
        return eixos;
    }

    public void setEixos(int eixos) {
        this.eixos = eixos;
    }

    public int getTollCount() {
        return tollCount;
    }

    public void setTollCount(int tollCount) {
        this.tollCount = tollCount;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getFuelKm() {
        return fuelKm;
    }

    public void setFuelKm(double fuelKm) {
        this.fuelKm = fuelKm;
    }

    public double getFuelValue() {
        return fuelValue;
    }

    public void setFuelValue(double fuelValue) {
        this.fuelValue = fuelValue;
    }

    public double getTollCost() {
        return tollCost;
    }

    public void setTollCost(double tollCost) {
        this.tollCost = tollCost;
    }

    public double getFuelUsage() {
        return fuelUsage;
    }

    public void setFuelUsage(double fuelUsage) {
        this.fuelUsage = fuelUsage;
    }

    public double getFuelCost() {
        return fuelCost;
    }

    public void setFuelCost(double fuelCost) {
        this.fuelCost = fuelCost;
    }

    public double getOriginPositionLatitude() {
        return originPositionLatitude;
    }

    public void setOriginPositionLatitude(double originPositionLatitude) {
        this.originPositionLatitude = originPositionLatitude;
    }

    public double getOriginPositionLongitude() {
        return originPositionLongitude;
    }

    public void setOriginPositionLongitude(double originPositionLongitude) {
        this.originPositionLongitude = originPositionLongitude;
    }

    public double getDestinationPositionLatitude() {
        return destinationPositionLatitude;
    }

    public void setDestinationPositionLatitude(double destinationPositionLatitude) {
        this.destinationPositionLatitude = destinationPositionLatitude;
    }

    public double getDestinationPositionLongitude() {
        return destinationPositionLongitude;
    }

    public void setDestinationPositionLongitude(double destinationPositionLongitude) {
        this.destinationPositionLongitude = destinationPositionLongitude;
    }

    public double getPriceFrigorificada() {
        return priceFrigorificada;
    }

    public void setPriceFrigorificada(double priceFrigorificada) {
        this.priceFrigorificada = priceFrigorificada;
    }

    public double getPriceGeral() {
        return priceGeral;
    }

    public void setPriceGeral(double priceGeral) {
        this.priceGeral = priceGeral;
    }

    public double getPriceGranel() {
        return priceGranel;
    }

    public void setPriceGranel(double priceGranel) {
        this.priceGranel = priceGranel;
    }

    public double getPriceNeogranel() {
        return priceNeogranel;
    }

    public void setPriceNeogranel(double priceNeogranel) {
        this.priceNeogranel = priceNeogranel;
    }

    public double getPricePerigosa() {
        return pricePerigosa;
    }

    public void setPricePerigosa(double pricePerigosa) {
        this.pricePerigosa = pricePerigosa;
    }

    public String getHasTolls() {
        return hasTolls;
    }

    public void setHasTolls(String hasTolls) {
        this.hasTolls = hasTolls;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
