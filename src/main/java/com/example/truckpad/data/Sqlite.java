package com.example.truckpad.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.truckpad.R;
import com.example.truckpad.model.Rota;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Sqlite extends SQLiteOpenHelper {

    private static final String NOME_DATABASE = "Truckpad.db";
    private static final int VERSAO_DATABASE = 1;
    private static final String Table_Rota = "CREATE TABLE IF NOT EXISTS ROTA( "
            + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "LATITUDE_ORIG TEXT, "
            + "LONGITUDE_ORIG TEXT, "
            + "LATITUDE_DEST TEXT, "
            + "LONGITUDE_DEST TEXT, "
            + "DISTANCIA CURRENCY, "
            + "COMBUSTIVEL_USADO CURRENCY, "
            + "COMBUSTIVEL_UNIDADE TEXT, "
            + "FUEL_COST CURRENCY, "
            + "TOTAL_COST CURRENCY, "
            + "CIDORIGEM TEXT, "
            + "CIDDESTINO TEXT "
            +");";


    public Sqlite(Context context){
        super(context, NOME_DATABASE, null, VERSAO_DATABASE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(Table_Rota);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS ROTA");
        db.execSQL(Table_Rota);
    }


    public void addRota(Rota rota) throws ParseException{

        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("LATITUDE_ORIG",rota.getLatitude_orig());
        values.put("LONGITUDE_ORIG",rota.getLongitude_orig());
        values.put("LATITUDE_DEST",rota.getLatitude_dest());
        values.put("LONGITUDE_DEST",rota.getLongitude_dest());
        values.put("DISTANCIA",rota.getDistancia());
        values.put("COMBUSTIVEL_USADO",rota.getCombustivel_usado());
        values.put("COMBUSTIVEL_UNIDADE",rota.getCombustivel_unidade());
        values.put("FUEL_COST",rota.getFuel_cost());
        values.put("TOTAL_COST",rota.getTotal_cost());
        values.put("CIDORIGEM",rota.getCidorigem());
        values.put("CIDDESTINO",rota.getCiddestino());

        db.insert("ROTA",null,values);
        db.close();
    }

    public ArrayList<Rota> gethistorico(){

        String countQuery = " SELECT LATITUDE_ORIG,LONGITUDE_ORIG," +
                " LATITUDE_DEST,LONGITUDE_DEST,DISTANCIA," +
                " COMBUSTIVEL_USADO,COMBUSTIVEL_UNIDADE," +
                " FUEL_COST,TOTAL_COST,CIDORIGEM,CIDDESTINO FROM ROTA " ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        ArrayList<Rota> list = new ArrayList<Rota>();
        if(cursor.moveToFirst()){
            do{
                Rota rota = new Rota(cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getDouble(5),
                        cursor.getString(6),
                        cursor.getDouble(7),
                        cursor.getDouble(8),
                        cursor.getString(9),
                        cursor.getString(10));
                list.add(rota);

            }while(cursor.moveToNext());
        }

        return list;
    }

}
