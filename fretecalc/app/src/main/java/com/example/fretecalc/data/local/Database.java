package com.example.fretecalc.data.local;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.fretecalc.models.prices.PriceResponse;
import com.example.fretecalc.models.routes.RouteResponse;
import com.example.fretecalc.models.routes.Search;

@androidx.room.Database(entities = {Search.class, RouteResponse.class, PriceResponse.class}, version = 12, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class Database extends RoomDatabase {

    private static volatile Database INSTANCE;

    public abstract SearchDao searchDao();

    public abstract RouteDao routeDao();

    public abstract PriceDao priceDao();

    public static Database getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (Database.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, Database.class, "fretecalc_db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
