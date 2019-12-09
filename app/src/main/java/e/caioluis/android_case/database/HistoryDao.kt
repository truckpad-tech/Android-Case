package e.caioluis.android_case.database

import androidx.room.*
import e.caioluis.android_case.model.RouteResult
import e.caioluis.android_case.util.Constants

@Dao
interface HistoryDao {

    @Insert
    suspend fun insert(routeResult: RouteResult)

    @Update
    suspend fun update(routeResult: RouteResult)

    @Delete
    suspend fun delete(routeResult: RouteResult)

    @Query("DELETE FROM ${Constants.DATABASE_TABLE}")
    suspend fun deleteAllRoutes()

    @Query("SELECT * FROM ${Constants.DATABASE_TABLE}")
    suspend fun getAllRoutes(): List<RouteResult>
}