
package com.example.fretecalc.models.prices;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;

@Entity(tableName = "prices")
public class PriceResponse {

    @PrimaryKey
    private Long priceId;
    @Expose
    private Double frigorificada;
    @Expose
    private Double geral;
    @Expose
    private Double granel;
    @Expose
    private Double neogranel;
    @Expose
    private Double perigosa;

    public PriceResponse() {
    }

    public Long getPriceId() {
        return priceId;
    }

    public void setPriceId(Long priceId) {
        this.priceId = priceId;
    }

    public Double getFrigorificada() {
        return frigorificada;
    }

    public void setFrigorificada(Double frigorificada) {
        this.frigorificada = frigorificada;
    }

    public Double getGeral() {
        return geral;
    }

    public void setGeral(Double geral) {
        this.geral = geral;
    }

    public Double getGranel() {
        return granel;
    }

    public void setGranel(Double granel) {
        this.granel = granel;
    }

    public Double getNeogranel() {
        return neogranel;
    }

    public void setNeogranel(Double neogranel) {
        this.neogranel = neogranel;
    }

    public Double getPerigosa() {
        return perigosa;
    }

    public void setPerigosa(Double perigosa) {
        this.perigosa = perigosa;
    }

}
