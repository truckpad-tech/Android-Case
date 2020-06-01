package br.com.meutruck.truckmap.Truck;

public class Truck {
    public int id, eixos;
    public double fuelKm;
    public String name;

    public Truck(int id, int eixos, double fuelKm, String name) {
        this.id = id;
        this.eixos = eixos;
        this.fuelKm = fuelKm;
        this.name = name;
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

    public double getFuelKm() {
        return fuelKm;
    }

    public void setFuelKm(double fuelKm) {
        this.fuelKm = fuelKm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
