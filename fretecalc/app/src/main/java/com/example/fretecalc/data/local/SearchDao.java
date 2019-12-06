package com.example.fretecalc.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.fretecalc.models.routes.Search;

import java.util.List;

import io.reactivex.Observable;

@Dao
public interface SearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertSearch(Search search);

    @Query("SELECT * FROM searches ORDER BY id DESC LIMIT 10")
    Observable<List<Search>> listRecentSearches();

    @Query("SELECT * FROM searches where id == :id")
    Observable<Search> searchById(Long id);

}
