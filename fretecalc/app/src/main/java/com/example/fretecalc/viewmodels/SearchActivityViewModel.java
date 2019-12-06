package com.example.fretecalc.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fretecalc.data.remote.PlacesService;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.fretecalc.utils.ApisConstants.GOOGLE_API_KEY;

public class SearchActivityViewModel extends AndroidViewModel {
    private MutableLiveData<List<AutocompletePrediction>> predictionList = new MutableLiveData<>();
    private MutableLiveData<Place> place = new MutableLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();
    private PlacesService placesService = new PlacesService(getApplication().getApplicationContext(), GOOGLE_API_KEY);
    private MutableLiveData<String> error = new MutableLiveData<>();

    public SearchActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<AutocompletePrediction>> getPredictionList() {
        return this.predictionList;
    }

    public LiveData<String> getError() {
        return this.error;
    }

    public LiveData<Place> getPlace() {
        return this.place;
    }

    public void getPredictions(CharSequence query) {
        disposable.add(
                placesService.getPredictions(query)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(autocompletePredictions -> {
                            predictionList.setValue(autocompletePredictions);
                        }, throwable -> {
                            error.setValue(throwable.getMessage());
                        })
        );
    }

    public void getPlaceById(String placeId) {
        disposable.add(
                placesService.getPlaceById(placeId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(place1 -> {
                            place.setValue(place1);
                        }, throwable -> {
                            error.setValue(throwable.getMessage());
                        })
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
