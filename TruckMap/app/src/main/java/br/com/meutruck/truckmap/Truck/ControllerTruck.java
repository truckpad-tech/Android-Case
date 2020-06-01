package br.com.meutruck.truckmap.Truck;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import br.com.meutruck.truckmap.DataBase.DataBaseGateway;

public class ControllerTruck {

    private final String TABLE = "Truck";
    public DataBaseGateway gw;

    public ControllerTruck(Context ctx){
        gw = DataBaseGateway.getInstance(ctx);
    }

    public boolean save(String name, String eixos, String fuelKm){
        return save(0, name, eixos, fuelKm);
    }

    public boolean save(int id, String name, String eixos, String fuelKm){
        ContentValues cv = new ContentValues();
        cv.put("ID", 1);
        cv.put("NAME", name);
        cv.put("EIXOS", eixos);
        cv.put("FUEL_KM", fuelKm);
        if(id > 0) {
            return gw.getDatabase().update(TABLE, cv, "ID=?", new String[]{id + ""}) > 0;
        }else {
            return gw.getDatabase().insert(TABLE, null, cv) > 0;
        }
    }

    public boolean exclude(int id){
        return gw.getDatabase().delete(TABLE, "ID=?", new String[]{ id + "" }) > 0;
    }

    public List<Truck> returnTruck(){
        List<Truck> trucks = new ArrayList<>();
        Cursor cursor = gw.getDatabase().rawQuery("SELECT * FROM Truck", null);
        while(cursor.moveToNext()){
            int ID = cursor.getInt(cursor.getColumnIndex("ID"));
            String NAME = cursor.getString(cursor.getColumnIndex("NAME"));
            int EIXOS = cursor.getInt(cursor.getColumnIndex("EIXOS"));
            Double FUEL_KM = cursor.getDouble(cursor.getColumnIndex("FUEL_KM"));
            trucks.add(new Truck(ID, EIXOS, FUEL_KM, NAME));
        }
        cursor.close();
        return trucks;
    }
}
