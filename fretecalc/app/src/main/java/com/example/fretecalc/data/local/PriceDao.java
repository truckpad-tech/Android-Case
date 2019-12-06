package com.example.fretecalc.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.fretecalc.models.prices.PriceResponse;

import io.reactivex.Observable;

@Dao
public interface PriceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPrice(PriceResponse routeResponse);

    @Query("SELECT * FROM prices where priceId == :id")
    Observable<PriceResponse> pricesById(Long id);
}
