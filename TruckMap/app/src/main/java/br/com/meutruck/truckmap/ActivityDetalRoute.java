package br.com.meutruck.truckmap;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

import br.com.meutruck.truckmap.Routes.Route;

public class ActivityDetalRoute extends AppCompatActivity {

    private LinearLayout layoutBack;
    private TextView textPage, textOrigin, textDestination, labelDistance, textDistance, labelTime, textTime, labelTruck, textEixos, textFuelUsage,
            labelDiesel, textFuel, labelValueFuel, textFuelValue, labelToll, textToll, labelCostToll, textTollValue, labelTabelANTT,
            labelFrigorifico, textFrigorifico, labelGeral, textGeral, labelGranel, textGranel, labelNeogranel, textNeogranel,
            labelPerigosa, textPerigosa, textBack;

    private String tollCostUnit, fuelCostUnit, locationOrigin, locationDestination, page;
    private int id, eixos, tollCount;
    private double distance, time, fuelKm, fuelValue, tollCost, fuelUsage, fuelCost, originPositionLatitude, originPositionLongitude, destinationPositionLatitude, destinationPositionLongitude, priceFrigorificada, priceGeral, priceGranel, priceNeogranel, pricePerigosa;
    private boolean hasTolls;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detal_route);
        showDetalRoute();
        declaration();

        layoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void showDetalRoute(){
        Intent it = getIntent();
        Bundle params = it.getExtras();
        id = params.getInt("id");
        eixos = params.getInt("eixos");
        tollCount  = params.getInt("tollCount");
        distance =params.getDouble("km");
        time = params.getDouble("time");
        fuelKm = params.getDouble("fuelKm");
        fuelValue = params.getDouble("fuelValue");
        tollCost = params.getDouble("tollCost");
        fuelUsage = params.getDouble("fuelUsage");
        tollCount = params.getInt("tollCount");
        originPositionLatitude = params.getDouble("originPositionLatitude");
        originPositionLongitude = params.getDouble("originPositionLongitude");
        destinationPositionLatitude = params.getDouble("destinationPositionLatitude");
        destinationPositionLongitude = params.getDouble("destinationPositionLongitude");
        hasTolls = params.getBoolean("hasTolls");
        fuelCost = params.getDouble("fuelCost");
        locationOrigin = params.getString("locationOrigin");
        locationDestination = params.getString("locationDestination");
        priceFrigorificada = params.getDouble("priceFrigorificada");
        priceGeral = params.getDouble("priceGeral");
        priceGranel = params.getDouble("priceGranel");
        priceNeogranel = params.getDouble("priceNeogranel");
        pricePerigosa = params.getDouble("pricePerigosa");
        page = params.getString("page");

    }

    private void declaration(){
        layoutBack = findViewById(R.id.layoutBack);

        textPage = findViewById(R.id.textPage);
        textOrigin = findViewById(R.id.textOrigin);
        textDestination = findViewById(R.id.textDestination);
        labelDistance = findViewById(R.id.labelDistance);
        textDistance = findViewById(R.id.textDistance);
        labelTime = findViewById(R.id.labelTime);
        textTime = findViewById(R.id.textTime);
        labelTruck = findViewById(R.id.labelTruck);
        textEixos = findViewById(R.id.textEixos);
        textFuelUsage = findViewById(R.id.textFuelUsage);
        labelDiesel = findViewById(R.id.labelDiesel);
        textFuel = findViewById(R.id.textFuel);
        labelValueFuel = findViewById(R.id.labelValueFuel);
        textFuelValue = findViewById(R.id.textFuelValue);
        labelToll = findViewById(R.id.labelToll);
        textToll = findViewById(R.id.textToll);
        labelCostToll = findViewById(R.id.labelCostToll);
        textTollValue = findViewById(R.id.textTollValue);
        labelTabelANTT = findViewById(R.id.labelTabelANTT);
        labelFrigorifico = findViewById(R.id.labelFrigorifico);
        textFrigorifico = findViewById(R.id.textFrigorifico);
        labelGeral = findViewById(R.id.labelGeral);
        textGeral = findViewById(R.id.textGeral);
        labelGranel = findViewById(R.id.labelGranel);
        textGranel = findViewById(R.id.textGranel);
        labelNeogranel = findViewById(R.id.labelNeogranel);
        textNeogranel = findViewById(R.id.textNeogranel);
        labelPerigosa = findViewById(R.id.labelPerigosa);
        textPerigosa = findViewById(R.id.textPerigosa);


        Typeface fontBold = Typeface.createFromAsset(getBaseContext().getAssets(),"Spartan-Bold.ttf");
        Typeface fontMedium = Typeface.createFromAsset(getBaseContext().getAssets(),"Spartan-Medium.ttf");
        Typeface fontThin = Typeface.createFromAsset(getBaseContext().getAssets(),"Spartan-Thin.ttf");

        textPage.setTypeface(fontBold);
        textOrigin.setTypeface(fontBold);
        textDestination.setTypeface(fontBold);
        labelDistance.setTypeface(fontMedium);
        textDistance.setTypeface(fontBold);
        labelTime.setTypeface(fontMedium);
        textTime.setTypeface(fontBold);
        labelTruck.setTypeface(fontMedium);
        textEixos.setTypeface(fontBold);
        textFuelUsage.setTypeface(fontBold);
        labelDiesel.setTypeface(fontMedium);
        textFuel.setTypeface(fontBold);
        labelValueFuel.setTypeface(fontMedium);
        textFuelValue.setTypeface(fontBold);
        labelToll.setTypeface(fontMedium);
        textToll.setTypeface(fontBold);
        labelCostToll.setTypeface(fontMedium);
        textTollValue.setTypeface(fontBold);
        labelTabelANTT.setTypeface(fontMedium);
        labelFrigorifico.setTypeface(fontMedium);
        textFrigorifico.setTypeface(fontBold);
        labelGeral.setTypeface(fontMedium);
        textGeral.setTypeface(fontBold);
        labelGranel.setTypeface(fontMedium);
        textGranel.setTypeface(fontBold);
        labelNeogranel.setTypeface(fontMedium);
        textNeogranel.setTypeface(fontBold);
        labelPerigosa.setTypeface(fontMedium);
        textPerigosa.setTypeface(fontBold);


        DecimalFormat decimalFormat = new DecimalFormat("0.0");

        textPage.setText(page);
        textOrigin.setText(locationOrigin);
        textDestination.setText(locationDestination);
        textDistance.setText(String.valueOf(decimalFormat.format(distance/1000)) + " Km");
        textTime.setText(String.valueOf(decimalFormat.format(time)) + " Hr");
        textEixos.setText(String.valueOf(eixos) + " eixos");
        labelDiesel.setText("Diesel - R$ " + String.valueOf(fuelValue));
        textFuelUsage.setText(String.valueOf(fuelKm) + " Km/L");
        textFuel.setText(String.valueOf(fuelUsage) + " L");
        textFuelValue.setText("R$ " + String.valueOf(fuelCost));
        textToll.setText(String.valueOf(tollCount));
        textTollValue.setText("R$ " + String.valueOf(tollCost));

        textFrigorifico.setText("R$ " + String.valueOf(priceFrigorificada));
        textGeral.setText("R$ " + String.valueOf(priceGeral));
        textGranel.setText("R$ " + String.valueOf(priceGranel));
        textNeogranel.setText("R$ " + String.valueOf(priceNeogranel));
        textPerigosa.setText("R$ " + String.valueOf(pricePerigosa));

    }
}
