package com.example.fretecalc.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fretecalc.models.routes.Search;
import com.example.fretecalc.repository.RouteRepository;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

public class RequestActivityViewModel extends AndroidViewModel {
    private RouteRepository routeRepository = new RouteRepository();
    private MutableLiveData<Search> search = new MutableLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();

    public RequestActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Search> getSearch() {
        return this.search;
    }

    public Single<Long> createSearch(Search search) {
        return routeRepository.saveSearch(getApplication().getApplicationContext(), search);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
