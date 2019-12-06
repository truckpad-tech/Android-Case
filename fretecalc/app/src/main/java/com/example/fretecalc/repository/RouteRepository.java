package com.example.fretecalc.repository;

import android.content.Context;

import com.example.fretecalc.data.local.Database;
import com.example.fretecalc.data.local.RouteDao;
import com.example.fretecalc.data.local.SearchDao;
import com.example.fretecalc.models.routes.Locality;
import com.example.fretecalc.models.routes.RouteRequest;
import com.example.fretecalc.models.routes.RouteResponse;
import com.example.fretecalc.models.routes.RouteSearch;
import com.example.fretecalc.models.routes.Search;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static com.example.fretecalc.data.remote.RetrofitService.getRoutesService;

public class RouteRepository implements SearchRepository {
    public Observable<RouteResponse> getRoute(List<Locality> localities, Double fuelConsumption, Double fuelPrice) {
        return getRoutesService().postRoute(new RouteRequest(fuelConsumption, fuelPrice, localities));
    }

    public void saveRoute(Context context, Long id, RouteResponse routeResponse) {
        RouteDao routeDao = Database.getDatabase(context).routeDao();
        routeResponse.setRouteId(id);
        routeDao.insertRoute(routeResponse);
    }

    public Observable<List<RouteSearch>> getRecentRoutesSearch(Context context) {
        RouteDao routeDao = Database.getDatabase(context).routeDao();
        return routeDao.findRouteSearchList();
    }

    public Observable<RouteResponse> getRoutesById(Context context, Long id) {
        RouteDao routeDao = Database.getDatabase(context).routeDao();
        return routeDao.routesById(id);
    }

    @Override
    public Single<Long> saveSearch(Context context, Search search) {
        SearchDao searchDao = Database.getDatabase(context).searchDao();
        return Single.fromCallable(() -> searchDao.insertSearch(search)).subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<Search>> getRecentSearches(Context context) {
        SearchDao searchDao = Database.getDatabase(context).searchDao();
        return searchDao.listRecentSearches();
    }

    @Override
    public Observable<Search> getSearchById(Context context, Long id) {
        SearchDao searchDao = Database.getDatabase(context).searchDao();
        return searchDao.searchById(id);
    }

}
