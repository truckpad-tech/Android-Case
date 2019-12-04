package com.diegobezerra.truckpadcase.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.diegobezerra.truckpadcase.data.database.converters.AnttPricesConverter
import com.diegobezerra.truckpadcase.data.database.converters.PlaceConverter
import com.diegobezerra.truckpadcase.data.database.converters.RouteInfoConverter
import com.diegobezerra.truckpadcase.data.database.dao.HistoryDao
import com.diegobezerra.truckpadcase.domain.model.HistoryEntry

@Database(entities = [HistoryEntry::class], version = 1, exportSchema = false)
@TypeConverters(
    value = [
        PlaceConverter::class,
        RouteInfoConverter::class,
        AnttPricesConverter::class
    ]
)
abstract class AppDatabase : RoomDatabase() {

    companion object {

        private const val DATABASE_NAME = "truckpad-db"

        fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration() // NOTE(diego): This should be used only on debug builds
                .build()
        }
    }

    abstract fun historyDao(): HistoryDao

}