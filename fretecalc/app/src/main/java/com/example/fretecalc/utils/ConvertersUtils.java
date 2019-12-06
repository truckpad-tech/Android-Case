package com.example.fretecalc.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ConvertersUtils {

    public static Double distanceUnits(String unit, Double value) {
        if (unit != null && unit.equals("meters")) {
            return value / 1000;
        } else {
            return value;
        }
    }

    public static String timeUnits(String unit, Long value) {
        if (unit.equals("seconds")) {
            return String.format(Locale.getDefault(), "%dh%02d",
                    TimeUnit.SECONDS.toHours(value),
                    TimeUnit.SECONDS.toMinutes(value) % TimeUnit.HOURS.toMinutes(1));
        } else if (unit.equals("minutes")) {
            return String.format(Locale.getDefault(), "%dh%02d",
                    TimeUnit.MINUTES.toHours(value),
                    TimeUnit.MINUTES.toMinutes(value) % TimeUnit.HOURS.toMinutes(1));
        } else if (unit.equals("milliseconds")) {
            return String.format(Locale.getDefault(), "%dh%02d",
                    TimeUnit.MILLISECONDS.toHours(value),
                    TimeUnit.MILLISECONDS.toMinutes(value) % TimeUnit.HOURS.toMinutes(1));
        }
        return "";
    }

    public static BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}
