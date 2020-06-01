package br.com.meutruck.truckmap.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseGateway {

    private static DataBaseGateway gw;
    private SQLiteDatabase db;

    private DataBaseGateway(Context ctx){
        DataBaseHelper helper = new DataBaseHelper(ctx);
        db = helper.getWritableDatabase();
    }

    public static DataBaseGateway getInstance(Context ctx){
        if(gw == null)
            gw = new DataBaseGateway(ctx);
        return gw;
    }

    public SQLiteDatabase getDatabase(){
        return this.db;
    }
}

