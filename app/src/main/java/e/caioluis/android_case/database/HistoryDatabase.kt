package e.caioluis.android_case.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import e.caioluis.android_case.model.RouteResult
import e.caioluis.android_case.util.Constants

@Database(entities = [RouteResult::class], version = 1, exportSchema = false)

abstract class HistoryDatabase : RoomDatabase() {

    abstract fun historyDao(): HistoryDao

    companion object {

        @Volatile
        private var instance: HistoryDatabase? = null

        fun getInstance(context: Context): HistoryDatabase {

            return instance
                ?: synchronized(this) {

                    instance
                        ?: Room.databaseBuilder(
                            context.applicationContext,
                            HistoryDatabase::class.java,
                            Constants.DATABASE_TABLE
                        ).fallbackToDestructiveMigration().build()
                }
        }
    }
}