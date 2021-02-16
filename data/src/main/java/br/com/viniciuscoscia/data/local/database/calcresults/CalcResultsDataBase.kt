package br.com.viniciuscoscia.data.local.database.calcresults

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.viniciuscoscia.data.local.model.CalcResultsEntity

@Database(version = 1, entities = [CalcResultsEntity::class])
abstract class CalcResultsDataBase : RoomDatabase() {

    abstract fun calcResultsDAO(): CalcResultsDAO

    companion object {
        private const val CALC_RESULTS_DATABASE_FILE_NAME = "calbresults.db"

        fun createDataBase(context: Context): CalcResultsDAO {
            return Room
                .databaseBuilder(
                    context, CalcResultsDataBase::class.java,
                    CALC_RESULTS_DATABASE_FILE_NAME
                )
                .build()
                .calcResultsDAO()
        }
    }
}