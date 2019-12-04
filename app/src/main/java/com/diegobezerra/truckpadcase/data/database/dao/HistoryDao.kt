package com.diegobezerra.truckpadcase.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.diegobezerra.truckpadcase.domain.model.HistoryEntry

@Dao
interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entry: HistoryEntry)

    @Query("DELETE FROM history_table")
    fun clear()

    @Query("SELECT * FROM history_table ORDER BY timestamp DESC LIMIT 25")
    fun getAll(): LiveData<List<HistoryEntry>>

}