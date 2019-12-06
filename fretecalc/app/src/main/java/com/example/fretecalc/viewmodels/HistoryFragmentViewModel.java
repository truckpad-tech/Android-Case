package com.example.fretecalc.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fretecalc.models.routes.RouteResponse;
import com.example.fretecalc.models.routes.RouteSearch;
import com.example.fretecalc.models.routes.Search;
import com.example.fretecalc.repository.RouteRepository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class HistoryFragmentViewModel extends AndroidViewModel {
    private MutableLiveData<List<RouteSearch>> routeSearchList = new MutableLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();
    private RouteRepository repository = new RouteRepository();
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();

    public HistoryFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<RouteSearch>> getRouteSearchList() {
        return this.routeSearchList;
    }

    public void getList() {
        disposable.add(
                repository.getRecentRoutesSearch(getApplication().getApplicationContext())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable1 -> {
                            loading.setValue(true);
                        })
                        .doAfterTerminate(() -> {
                            loading.setValue(false);
                        })
                        .subscribe(routeSearches -> {
                            routeSearchList.setValue(routeSearches);
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
