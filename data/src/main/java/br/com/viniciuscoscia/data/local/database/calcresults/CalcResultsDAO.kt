package br.com.viniciuscoscia.data.local.database.calcresults

import androidx.room.*
import br.com.viniciuscoscia.data.local.model.CalcResultsEntity

@Dao
interface CalcResultsDAO {
    @Query("SELECT * FROM calc_results ORDER BY id DESC")
    suspend fun getCalcResults(): List<CalcResultsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(calcResultsEntity: CalcResultsEntity)

    @Update
    suspend fun update(calcResultsEntity: CalcResultsEntity)

    @Delete
    suspend fun delete(calcResultsEntity: CalcResultsEntity)

    @Query("SELECT * FROM calc_results WHERE id = :id")
    suspend fun findById(id: Int): CalcResultsEntity
}