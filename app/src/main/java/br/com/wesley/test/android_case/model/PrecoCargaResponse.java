package br.com.wesley.test.android_case.model;

public class PrecoCargaResponse {

    private double frigorificada;
    private double geral;
    private double granel;
    private double neogranel;
    private double perigosa;

    public double getFrigorificada() {
        return frigorificada;
    }

    public void setFrigorificada(double frigorificada) {
        this.frigorificada = frigorificada;
    }

    public double getGeral() {
        return geral;
    }

    public void setGeral(double geral) {
        this.geral = geral;
    }

    public double getGranel() {
        return granel;
    }

    public void setGranel(double granel) {
        this.granel = granel;
    }

    public double getNeogranel() {
        return neogranel;
    }

    public void setNeogranel(double neogranel) {
        this.neogranel = neogranel;
    }

    public double getPerigosa() {
        return perigosa;
    }

    public void setPerigosa(double perigosa) {
        this.perigosa = perigosa;
    }
}
