package br.com.wesley.test.android_case.model;

class PlaceRouteResponsePoint {

    private PlaceRoute.PlaceRoutePoint[] points;
    private String provider;

    public PlaceRoute.PlaceRoutePoint[] getPoints() {
        return points;
    }

    public void setPoints(PlaceRoute.PlaceRoutePoint[] points) {
        this.points = points;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
