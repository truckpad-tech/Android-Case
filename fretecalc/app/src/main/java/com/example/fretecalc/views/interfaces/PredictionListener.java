package com.example.fretecalc.views.interfaces;

import com.google.android.libraries.places.api.model.AutocompletePrediction;

public interface PredictionListener {
    void onPredictionClick(AutocompletePrediction prediction);
}
