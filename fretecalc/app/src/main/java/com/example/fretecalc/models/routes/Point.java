
package com.example.fretecalc.models.routes;

import com.google.gson.annotations.Expose;

import java.util.List;

public class Point {

    @Expose
    private List<Double> point;
    @Expose
    private String provider;

    public List<Double> getPoint() {
        return point;
    }

    public void setPoint(List<Double> point) {
        this.point = point;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
