package com.example.truckpad.model;

import java.io.Serializable;

public class Cidade implements Serializable {

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    private String uf;
    private String latitude;
    private String longitude;
    private String cep;
    private String nome;
    private String bairro;
    private String logradouro;

    public Cidade(String uf, String latitude, String longitude, String cep, String nome, String bairro, String logradouro) {
        this.uf = uf;
        this.latitude = latitude;
        this.longitude = longitude;
        this.cep = cep;
        this.nome = nome;
        this.bairro = bairro;
        this.logradouro = logradouro;
    }
}
