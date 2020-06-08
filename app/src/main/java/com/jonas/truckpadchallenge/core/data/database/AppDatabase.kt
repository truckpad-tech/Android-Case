package com.jonas.truckpadchallenge.core.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jonas.truckpadchallenge.core.data.database.converters.DataConverter
import com.jonas.truckpadchallenge.search.domain.entities.SearchResult

@Database(entities = [SearchResult::class], version = 1, exportSchema = false)
@TypeConverters(DataConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getAppDataBase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "calculateRouteDB"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}