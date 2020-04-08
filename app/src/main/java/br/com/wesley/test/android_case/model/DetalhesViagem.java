package br.com.wesley.test.android_case.model;

import java.io.Serializable;

public class DetalhesViagem implements Serializable {

    private String origem;
    private String destino;
    private int eixos;
    private double distancia;
    private long duracao;
    private double pedagio;
    private double consumoCombustivel;
    private double totalCombustivel;
    private double total;

    private double valorFreteGeral;
    private double valorFreteGranel;
    private double valorFreteNeogranel;
    private double valorFreteFrigorificada;
    private double valorFretePerigosa;

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public int getEixos() {
        return eixos;
    }

    public void setEixos(int eixos) {
        this.eixos = eixos;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public long getDuracao() {
        return duracao;
    }

    public void setDuracao(long duracao) {
        this.duracao = duracao;
    }

    public double getPedagio() {
        return pedagio;
    }

    public void setPedagio(double pedagio) {
        this.pedagio = pedagio;
    }

    public double getConsumoCombustivel() {
        return consumoCombustivel;
    }

    public void setConsumoCombustivel(double consumoCombustivel) {
        this.consumoCombustivel = consumoCombustivel;
    }

    public double getTotalCombustivel() {
        return totalCombustivel;
    }

    public void setTotalCombustivel(double totalCombustivel) {
        this.totalCombustivel = totalCombustivel;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getValorFreteGeral() {
        return valorFreteGeral;
    }

    public void setValorFreteGeral(double valorFreteGeral) {
        this.valorFreteGeral = valorFreteGeral;
    }

    public double getValorFreteGranel() {
        return valorFreteGranel;
    }

    public void setValorFreteGranel(double valorFreteGranel) {
        this.valorFreteGranel = valorFreteGranel;
    }

    public double getValorFreteNeogranel() {
        return valorFreteNeogranel;
    }

    public void setValorFreteNeogranel(double valorFreteNeogranel) {
        this.valorFreteNeogranel = valorFreteNeogranel;
    }

    public double getValorFreteFrigorificada() {
        return valorFreteFrigorificada;
    }

    public void setValorFreteFrigorificada(double valorFreteFrigorificada) {
        this.valorFreteFrigorificada = valorFreteFrigorificada;
    }

    public double getValorFretePerigosa() {
        return valorFretePerigosa;
    }

    public void setValorFretePerigosa(double valorFretePerigosa) {
        this.valorFretePerigosa = valorFretePerigosa;
    }
}
