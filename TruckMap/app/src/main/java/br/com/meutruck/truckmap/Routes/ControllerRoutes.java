package br.com.meutruck.truckmap.Routes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.style.light.Position;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.meutruck.truckmap.DataBase.DataBaseGateway;
import br.com.meutruck.truckmap.MainActivity;
import br.com.meutruck.truckmap.R;

public class ControllerRoutes{

    private final String TABLE = "Routes";
    public DataBaseGateway gw;

    public ControllerRoutes(Context ctx){
        gw = DataBaseGateway.getInstance(ctx);
    }

    public boolean save(int eixos, int tollCount, double distance, double duration, double fuelKm, double fuelValue, double tollCost, double fuelUsage,
                        double fuelCost, double originPositionLatitude, double originPositionLongitude, double destinationPositionLatitude,
                        double destinationPositionLongitude, double priceFrigorificada, double priceGeral, double priceGranel, double priceNeogranel,
                        double pricePerigosa, String hasTolls, String origin, String destination, String date){
        return save(0, eixos, tollCount, distance, duration, fuelKm, fuelValue, tollCost, fuelUsage, fuelCost, originPositionLatitude, originPositionLongitude,
                destinationPositionLatitude, destinationPositionLongitude, priceFrigorificada, priceGeral, priceGranel, priceNeogranel, pricePerigosa, hasTolls, origin, destination, date);
    }

    public boolean save(int id, int eixos, int tollCount, double distance, double duration, double fuelKm, double fuelValue, double tollCost, double fuelUsage,
                        double fuelCost, double originPositionLatitude, double originPositionLongitude, double destinationPositionLatitude,
                        double destinationPositionLongitude, double priceFrigorificada, double priceGeral, double priceGranel, double priceNeogranel,
                        double pricePerigosa, String hasTolls, String origin, String destination, String date){
        ContentValues cv = new ContentValues();
        cv.put("ID", id);
        cv.put("EIXOS", eixos);
        cv.put("TOLL_COUNT", tollCount);
        cv.put("DISTANCE", distance);
        cv.put("DURATION", duration);
        cv.put("FUEL_KM", fuelKm);
        cv.put("FUEL_VALUE", fuelValue);
        cv.put("TOLL_COST", tollCost);
        cv.put("FUEL_USAGE", fuelUsage);
        cv.put("FUEL_COST", fuelCost);
        cv.put("ORIGIN_LATITUDE", originPositionLatitude);
        cv.put("ORIGIN_LONGITUDE", originPositionLongitude);
        cv.put("DESTINATION_LATITUDE", destinationPositionLatitude);
        cv.put("DESTINATION_LONGITUDE", destinationPositionLongitude);
        cv.put("PRICE_FRIGORIFICADA", priceFrigorificada);
        cv.put("PRICE_GERAL", priceGeral);
        cv.put("PRICE_GRANEL", priceGranel);
        cv.put("PRICE_NEOGRANEL", priceNeogranel);
        cv.put("PRICE_PERIGOSA", pricePerigosa);
        cv.put("HAS_TOLLS", hasTolls);
        cv.put("ORIGIN", origin);
        cv.put("DESTINATION", destination);
        cv.put("DATE", date);
        if(id > 0) {
            return gw.getDatabase().update(TABLE, cv, "ID=?", new String[]{id + ""}) > 0;
        }else {
            return gw.getDatabase().insert(TABLE, null, cv) > 0;
        }
    }

    public List<Route> returnRoute(){
        List<Route> trucks = new ArrayList<>();
        Cursor cursor = gw.getDatabase().rawQuery("SELECT * FROM Routes", null);
        while(cursor.moveToNext()){
            int ID = cursor.getInt(cursor.getColumnIndex("ID"));
            int EIXOS = cursor.getInt(cursor.getColumnIndex("EIXOS"));
            int TOLL_COUNT = cursor.getInt(cursor.getColumnIndex("TOLL_COUNT"));
            double DISTANCE = cursor.getDouble(cursor.getColumnIndex("DISTANCE"));
            double DURATION = cursor.getDouble(cursor.getColumnIndex("DURATION"));
            double FUEL_KM = cursor.getDouble(cursor.getColumnIndex("FUEL_KM"));
            double FUEL_VALUE = cursor.getDouble(cursor.getColumnIndex("FUEL_VALUE"));
            double TOLL_COST = cursor.getDouble(cursor.getColumnIndex("TOLL_COST"));
            double FUEL_USAGE = cursor.getDouble(cursor.getColumnIndex("FUEL_USAGE"));
            double FUEL_COST = cursor.getDouble(cursor.getColumnIndex("FUEL_COST"));
            double ORIGIN_LATITUDE = cursor.getDouble(cursor.getColumnIndex("ORIGIN_LATITUDE"));
            double ORIGIN_LONGITUDE = cursor.getDouble(cursor.getColumnIndex("ORIGIN_LONGITUDE"));
            double DESTINATION_LATITUDE = cursor.getDouble(cursor.getColumnIndex("DESTINATION_LATITUDE"));
            double DESTINATION_LONGITUDE = cursor.getDouble(cursor.getColumnIndex("DESTINATION_LONGITUDE"));
            double PRICE_FRIGORIFICADA = cursor.getDouble(cursor.getColumnIndex("PRICE_FRIGORIFICADA"));
            double PRICE_GERAL = cursor.getDouble(cursor.getColumnIndex("PRICE_GERAL"));
            double PRICE_GRANEL = cursor.getDouble(cursor.getColumnIndex("PRICE_GRANEL"));
            double PRICE_NEOGRANEL = cursor.getDouble(cursor.getColumnIndex("PRICE_NEOGRANEL"));
            double PRICE_PERIGOSA = cursor.getDouble(cursor.getColumnIndex("PRICE_PERIGOSA"));
            String HAS_TOLLS = cursor.getString(cursor.getColumnIndex("HAS_TOLLS"));
            String ORIGIN = cursor.getString(cursor.getColumnIndex("ORIGIN"));
            String DESTINATION = cursor.getString(cursor.getColumnIndex("DESTINATION"));
            String DATE = cursor.getString(cursor.getColumnIndex("DATE"));
            trucks.add(new Route(ID, EIXOS, TOLL_COUNT, DISTANCE, DURATION, FUEL_KM, FUEL_VALUE, TOLL_COST, FUEL_USAGE, FUEL_COST, ORIGIN_LATITUDE, ORIGIN_LONGITUDE, DESTINATION_LATITUDE,
                    DESTINATION_LONGITUDE, PRICE_FRIGORIFICADA, PRICE_GERAL, PRICE_GRANEL, PRICE_NEOGRANEL, PRICE_PERIGOSA, HAS_TOLLS, ORIGIN, DESTINATION, DATE));
        }
        cursor.close();
        return trucks;
    }

    public JsonObject createJson(double fuelKm, double fuelValue, Point originPosition, Point destinationPosition){
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArrayPoints = new JsonArray();
        try{
            JsonObject jsonObjectOrigin = new JsonObject();
            JsonArray jsonArrayOrigin = new JsonArray();;
            jsonArrayOrigin.add(originPosition.longitude());
            jsonArrayOrigin.add(originPosition.latitude());
            jsonObjectOrigin.add("point", jsonArrayOrigin);

            JsonObject jsonObjectDestination = new JsonObject();
            JsonArray jsonArrayDestination = new JsonArray();
            jsonArrayDestination.add(destinationPosition.longitude());
            jsonArrayDestination.add(destinationPosition.latitude());
            jsonObjectDestination.add("point", jsonArrayDestination);

            jsonArrayPoints.add(jsonObjectOrigin);
            jsonArrayPoints.add(jsonObjectDestination);

            jsonObject.add("places", jsonArrayPoints);
            jsonObject.addProperty("fuel_consumption", fuelKm);
            jsonObject.addProperty("fuel_price", fuelValue);

        }catch (Exception e){}
        return jsonObject;
    }

    public JsonObject createJsonPriceANTT(int axis, double distance, boolean has_return_shipment){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("axis", axis);
        jsonObject.addProperty("distance", distance);
        jsonObject.addProperty("has_return_shipment", has_return_shipment);
        return jsonObject;
    }

}
