package com.truckpadcase.calculatefreight.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.truckpadcase.calculatefreight.domain.model.local.FreightData


@Dao
interface HistoryDao {
     @Query("SELECT * FROM FreightData")
     fun getAll(): LiveData<List<FreightData>>

     @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertAll( freightData: FreightData) : Long

    @Query("SELECT * FROM FreightData WHERE uid = :id")
    fun geRouteStorage(id: String): LiveData<FreightData>

    @Delete
     fun delete(freightData: FreightData)

}