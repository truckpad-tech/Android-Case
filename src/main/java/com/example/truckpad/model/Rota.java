package com.example.truckpad.model;

import java.io.Serializable;

public class Rota implements Serializable {

    public String getLatitude_orig() {
        return latitude_orig;
    }

    public void setLatitude_orig(String latitude_orig) {
        this.latitude_orig = latitude_orig;
    }

    public String getLongitude_orig() {
        return longitude_orig;
    }

    public void setLongitude_orig(String longitude_orig) {
        this.longitude_orig = longitude_orig;
    }

    public String getLatitude_dest() {
        return latitude_dest;
    }

    public void setLatitude_dest(String latitude_dest) {
        this.latitude_dest = latitude_dest;
    }

    public String getLongitude_dest() {
        return longitude_dest;
    }

    public void setLongitude_dest(String longitude_dest) {
        this.longitude_dest = longitude_dest;
    }

    public String getDistancia() {
        return distancia;
    }

    public void setDistancia(String distancia) {
        this.distancia = distancia;
    }

    public double getCombustivel_usado() {
        return combustivel_usado;
    }

    public void setCombustivel_usado(double combustivel_usado) {
        this.combustivel_usado = combustivel_usado;
    }

    public String getCombustivel_unidade() {
        return combustivel_unidade;
    }

    public void setCombustivel_unidade(String combustivel_unidade) {
        this.combustivel_unidade = combustivel_unidade;
    }

    public double getFuel_cost() {
        return fuel_cost;
    }

    public void setFuel_cost(double fuel_cost) {
        this.fuel_cost = fuel_cost;
    }

    public double getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(double total_cost) {
        this.total_cost = total_cost;
    }

    public String getCidorigem() {
        return cidorigem;
    }

    public void setCidorigem(String cidorigem) {
        this.cidorigem = cidorigem;
    }

    public String getCiddestino() {
        return ciddestino;
    }

    public void setCiddestino(String ciddestino) {
        this.ciddestino = ciddestino;
    }

    public Rota(String latitude_orig, String longitude_orig, String latitude_dest,
                String longitude_dest, String distancia, double combustivel_usado,
                String combustivel_unidade, double fuel_cost, double total_cost,
                String cidorigem, String ciddestino) {
        this.latitude_orig = latitude_orig;
        this.longitude_orig = longitude_orig;
        this.latitude_dest = latitude_dest;
        this.longitude_dest = longitude_dest;
        this.distancia = distancia;
        this.combustivel_usado = combustivel_usado;
        this.combustivel_unidade = combustivel_unidade;
        this.fuel_cost = fuel_cost;
        this.total_cost = total_cost;
        this.cidorigem = cidorigem;
        this.ciddestino = ciddestino;
    }

    private String latitude_orig;
    private String longitude_orig;
    private String latitude_dest;
    private String longitude_dest;
    private String distancia;
    private double combustivel_usado;
    private String combustivel_unidade;
    private double fuel_cost;
    private double total_cost;
    private String cidorigem;
    private String ciddestino;





}
