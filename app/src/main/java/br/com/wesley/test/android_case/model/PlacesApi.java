package br.com.wesley.test.android_case.model;

import java.util.List;

import br.com.wesley.test.android_case.model.Place;

public class PlacesApi {

    private List<Place> places;
    private String provider;

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
