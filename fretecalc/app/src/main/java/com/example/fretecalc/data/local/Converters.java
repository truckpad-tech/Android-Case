package com.example.fretecalc.data.local;

import androidx.room.TypeConverter;

import com.example.fretecalc.models.routes.Locality;
import com.example.fretecalc.models.routes.Point;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Converters {

    @TypeConverter
    public static List<Locality> toLocalityList(String value) {
        Type listType = new TypeToken<List<Locality>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromLocalityList(List<Locality> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static List<Point> toPointList(String value) {
        Type listType = new TypeToken<List<Point>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromPointList(List<Point> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static List<List<List<Double>>> toRouteList(String value) {
        Type listType = new TypeToken<List<List<List<Double>>>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromRouteList(List<List<List<Double>>> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

}
