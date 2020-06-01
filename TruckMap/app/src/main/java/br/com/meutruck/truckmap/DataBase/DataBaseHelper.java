package br.com.meutruck.truckmap.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Crud.db";
    private static final int DATABASE_VERSION = 1;
    private final String CREATE_TRUCK = "CREATE TABLE Truck (ID INTEGER NOT NULL, NAME TEXT NOT NULL, EIXOS TEXT NOT NULL, FUEL_KM TEXT NOT NULL);";
    private final String CREATE_ROUTE = "CREATE TABLE Routes (ID INTEGER NOT NULL, ORIGIN TEXT NOT NULL, DESTINATION TEXT NOT NULL, EIXOS TEXT NOT NULL, TOLL_COUNT TEXT NOT NULL, DISTANCE TEXT NOT NULL, DURATION TEXT NOT NULL, FUEL_KM TEXT NOT NULL, FUEL_VALUE TEXT NOT NULL, TOLL_COST TEXT NOT NULL, FUEL_USAGE TEXT NOT NULL, FUEL_COST TEXT NOT NULL, ORIGIN_LATITUDE TEXT NOT NULL, ORIGIN_LONGITUDE TEXT NOT NULL, DESTINATION_LATITUDE TEXT NOT NULL, DESTINATION_LONGITUDE TEXT NOT NULL, PRICE_FRIGORIFICADA TEXT NOT NULL, PRICE_GERAL TEXT NOT NULL, PRICE_GRANEL TEXT NOT NULL, PRICE_NEOGRANEL TEXT NOT NULL, PRICE_PERIGOSA TEXT NOT NULL, HAS_TOLLS TEXT NOT NULL, DATE TEXT NOT NULL);";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TRUCK);
        db.execSQL(CREATE_ROUTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (DATABASE_VERSION < 1) {
            db.needUpgrade(newVersion);
        }
    }
}