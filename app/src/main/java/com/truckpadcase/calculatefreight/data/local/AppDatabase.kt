package com.truckpadcase.calculatefreight.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.truckpadcase.calculatefreight.domain.model.local.FreightData
import com.truckpadcase.calculatefreight.utils.Constants.DATABASE_NAME

@Database(entities = [FreightData::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun routeStorageDao(): HistoryDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {

            return instance
                ?: synchronized(this) {
                    instance
                        ?: Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            DATABASE_NAME
                        ).fallbackToDestructiveMigration().build()
                }
        }
    }

}