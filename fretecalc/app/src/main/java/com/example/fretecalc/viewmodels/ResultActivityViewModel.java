package com.example.fretecalc.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fretecalc.models.prices.PriceResponse;
import com.example.fretecalc.models.routes.RouteResponse;
import com.example.fretecalc.models.routes.Search;
import com.example.fretecalc.repository.PriceRepository;
import com.example.fretecalc.repository.RouteRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ResultActivityViewModel extends AndroidViewModel {
    private CompositeDisposable disposable = new CompositeDisposable();
    private RouteRepository routeRepository = new RouteRepository();
    private PriceRepository priceRepository = new PriceRepository();
    private MutableLiveData<RouteResponse> routeResponse = new MutableLiveData<>();
    private MutableLiveData<PriceResponse> priceResponse = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private MutableLiveData<Search> search = new MutableLiveData<>();

    public ResultActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<RouteResponse> getRouteResponse() {
        return this.routeResponse;
    }

    public LiveData<PriceResponse> getPriceResponse() {
        return this.priceResponse;
    }

    public LiveData<Boolean> getLoading() {
        return this.loading;
    }

    public LiveData<String> getError() {
        return this.error;
    }

    public LiveData<Search> getSearchData() {
        return this.search;
    }

    public void searchById(Long id) {
        disposable.add(
                routeRepository.getSearchById(getApplication().getApplicationContext(), id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(search1 -> {
                            search.setValue(search1);
                        }, throwable -> {
                            error.setValue(throwable.getMessage());
                        })
        );
    }

    public void getRoute(Long id) {
        disposable.add(
                routeRepository.getSearchById(getApplication().getApplicationContext(), id)
                        .flatMap(search1 -> {
                            return routeRepository.getRoute(search1.getRequest().getPlaces(), search1.getRequest().getFuelConsumption(), search1.getRequest().getFuelPrice());
                        }, (search1, routeResponse1) -> {
                            return new Pair<>(routeResponse1, priceRepository.getPrices(search1.getAxis(), routeResponse1.getDistance() / 1000, false).blockingFirst());
                        })
                        .map(pair -> {
                            saveRouteOnDatabase(id, pair.first);
                            savePriceOnDatabase(id, pair.second);
                            return pair;
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable1 -> {
                            loading.setValue(true);
                        })
                        .doOnTerminate(() -> {
                            loading.setValue(false);
                        })
                        .subscribe(pair -> {
                            routeResponse.setValue(pair.first);
                            priceResponse.setValue(pair.second);
                            loading.setValue(false);
                        }, throwable -> {
                            error.setValue(throwable.getMessage());
                            Log.i("ROUTESAVE", "getRoute: " + throwable.getMessage());
                        })

        );
    }

    public void getLocalRoute(Long id) {
        disposable.add(
                routeRepository.getSearchById(getApplication().getApplicationContext(), id)
                        .flatMap(search1 -> {
                            return routeRepository.getRoutesById(getApplication().getApplicationContext(), id);
                        }, (search1, routeResponse1) -> {
                            return new Pair<>(routeResponse1, priceRepository.getPricesById(getApplication().getApplicationContext(), id).blockingFirst());
                        })
                        .map(pair -> {
                            saveRouteOnDatabase(id, pair.first);
                            savePriceOnDatabase(id, pair.second);
                            return pair;
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable1 -> {
                            loading.setValue(true);
                        })
                        .doOnTerminate(() -> {
                            loading.setValue(false);
                        })
                        .subscribe(pair -> {
                            routeResponse.setValue(pair.first);
                            priceResponse.setValue(pair.second);
                            loading.setValue(false);
                        }, throwable -> {
                            error.setValue(throwable.getMessage());
                        })
        );
    }

    private void saveRouteOnDatabase(Long id, RouteResponse response) {
        routeRepository.saveRoute(getApplication().getApplicationContext(), id, response);
    }

    private void savePriceOnDatabase(Long id, PriceResponse response) {
        priceRepository.savePrices(getApplication().getApplicationContext(), id, response);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
