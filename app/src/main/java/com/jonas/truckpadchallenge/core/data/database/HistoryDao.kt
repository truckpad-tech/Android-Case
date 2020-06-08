package com.jonas.truckpadchallenge.core.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jonas.truckpadchallenge.search.domain.entities.SearchResult
import io.reactivex.Maybe

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertResult(searchResult: SearchResult)

    @Query("SELECT * FROM search_result")
    fun getAll(): Maybe<List<SearchResult>>
}