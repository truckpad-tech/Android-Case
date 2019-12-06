package com.example.fretecalc.data.remote;

import android.content.Context;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;

public class PlacesService {
    private PlacesClient placesClient;
    private Context context;
    private List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);

    public PlacesService(Context context, String apiKey) {
        this.context = context;
        if (!Places.isInitialized()) {
            Places.initialize(this.context, apiKey);
        }

        this.placesClient = Places.createClient(this.context);
    }

    public Observable<List<AutocompletePrediction>> getPredictions(CharSequence query) {

        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        return Observable.create(emitter -> {
            FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                    .setCountry("br")
                    .setSessionToken(token)
                    .setQuery(query.toString())
                    .build();
            placesClient.findAutocompletePredictions(request)
                    .addOnSuccessListener(response -> {
                        emitter.onNext(response.getAutocompletePredictions());
                        emitter.onComplete();
                    })
                    .addOnFailureListener(e -> {
                        emitter.onError(e);
                    });
        });
    }

    public Observable<Place> getPlaceById(String placeId) {
        return Observable.create(emitter -> {
            FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();
            placesClient.fetchPlace(request).addOnSuccessListener(response -> {
                emitter.onNext(response.getPlace());
            }).addOnFailureListener(e -> {
                emitter.onError(e);
            });

        });
    }
}
