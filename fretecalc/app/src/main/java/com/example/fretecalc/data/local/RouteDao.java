package com.example.fretecalc.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.fretecalc.models.routes.RouteResponse;
import com.example.fretecalc.models.routes.RouteSearch;

import java.util.List;

import io.reactivex.Observable;

@Dao
public interface RouteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRoute(RouteResponse routeResponse);

    @Query("SELECT routes.*, searches.* FROM routes INNER JOIN searches ON routes.routeId = searches.id ORDER BY routeId DESC LIMIT 10")
    Observable<List<RouteSearch>> findRouteSearchList();

    @Query("SELECT * FROM routes where routeId == :id")
    Observable<RouteResponse> routesById(Long id);
}
