package com.example.truckpad.model;

public class Fretes {

    public String getFrigorificada() {
        return frigorificada;
    }

    public void setFrigorificada(String frigorificada) {
        this.frigorificada = frigorificada;
    }

    public String getGeral() {
        return geral;
    }

    public void setGeral(String geral) {
        this.geral = geral;
    }

    public String getGranel() {
        return granel;
    }

    public void setGranel(String granel) {
        this.granel = granel;
    }

    public String getNeogranel() {
        return neogranel;
    }

    public void setNeogranel(String neogranel) {
        this.neogranel = neogranel;
    }

    public String getPerigosa() {
        return perigosa;
    }

    public void setPerigosa(String perigosa) {
        this.perigosa = perigosa;
    }

    public Fretes(String frigorificada, String geral, String granel, String neogranel, String perigosa) {
        this.frigorificada = frigorificada;
        this.geral = geral;
        this.granel = granel;
        this.neogranel = neogranel;
        this.perigosa = perigosa;
    }

    private String frigorificada;
    private String geral;
    private String granel;
    private String neogranel;
    private String perigosa;
}
