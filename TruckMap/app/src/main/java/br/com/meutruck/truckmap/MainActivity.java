package br.com.meutruck.truckmap;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.com.meutruck.truckmap.Routes.ControllerRoutes;
import br.com.meutruck.truckmap.Truck.ControllerTruck;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements  OnMapReadyCallback, LocationEngineListener, PermissionsListener,
        MapboxMap.OnMapClickListener, View.OnClickListener{

    private TextView textProgress, textOrigin, textDestination, textDistance, textTime, labelDistance, labelTime, textCancel, textDetal;
    private EditText editLocation, editDestination, editEixos, editFuelKm, editValueFuel;
    private LinearLayout layoutNewRoute, layoutRoute, layoutButtonRoute, layoutInformation, layoutDetal, layoutCancel;
    private ImageView imageCloseNewRoute, imageMyLocation;
    private FloatingActionButton fabMain, fabTruck, fabHistoric;

    private Float translationY = 50f;
    private Boolean isMenuOpen = false;
    private OvershootInterpolator interpolator = new OvershootInterpolator();

    private MapView mapView;
    private MapboxMap map;
    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;
    private LocationLayerPlugin locationLayerPlugin;
    private Location originLocation;
    private Point originPosition, destinationPosition;
    private Marker destinationMarker;
    private NavigationMapRoute navigationMapRoute;

    private String locationOrigin, locationDestination;
    private int id, eixos, tollCount;
    private double distance, time, fuelKm, fuelValue, tollCost, fuelUsage, fuelCost, originPositionLatitude, originPositionLongitude,
            destinationPositionLatitude, destinationPositionLongitude, priceFrigorificada, priceGeral, priceGranel, priceNeogranel, pricePerigosa;
    private boolean hasTolls;
    private String tollCostUnit, fuelCostUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_main);
        declarations();

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        layoutButtonRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchRoute();
            }
        });

        imageCloseNewRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishNewRoute();
            }
        });

        layoutNewRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewRoute();
            }
        });

        layoutDetal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPageDetalRoute();
            }
        });

        layoutCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishInformationDetal();
            }
        });

        imageMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editLocation.setText(locationOrigin);
            }
        });


        initFabMenu();

    }

    private void searchRoute(){
        String location = editLocation.getText().toString();
        String destination = editDestination.getText().toString();
        String eixos = editEixos.getText().toString();
        String fuelKm = editFuelKm.getText().toString();
        String valueFuel = editValueFuel.getText().toString();

        if(location.length() > 0 && destination.length() > 0 && eixos.length() > 0 && fuelKm.length() > 0 && valueFuel.length() > 0){
            if(Integer.parseInt(eixos) > 1 && Integer.parseInt(eixos) < 10 ){
                hideKeyboard(MainActivity.this, editValueFuel);
                searchCity(location, destination, Integer.parseInt(eixos), Double.parseDouble(fuelKm), Double.parseDouble(valueFuel));
                finishNewRoute();
                layoutNewRoute.setEnabled(false);
            }else {
                dialogMessage("O número de eixos deve ser entre 2 e 9!");
            }
        }else{
            dialogMessage("Todos os campos são obrigatórios!");
        }
    }

    private void initFabMenu(){
        fabTruck.setAlpha(0f);
        fabHistoric.setAlpha(0f);

        fabTruck.setTranslationX(0f);
        fabHistoric.setTranslationX(0f);

        fabMain.setOnClickListener(this);
        fabTruck.setOnClickListener(this);
        fabHistoric.setOnClickListener(this);
    }

    private void openMenuFab(){
        isMenuOpen = !isMenuOpen;
        fabMain.animate().setInterpolator(interpolator).rotation(45f).setDuration(300).start();

        fabTruck.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabHistoric.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
    }

    private void closemenuFab(){
        isMenuOpen = !isMenuOpen;
        fabMain.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();

        fabTruck.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabHistoric.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.fabMain:
                if(isMenuOpen){
                    closemenuFab();
                }else{
                    openMenuFab();
                }
                break;
            case R.id.fabTruck:
                intent.setClass(MainActivity.this, ActivityNewTruck.class);
                startActivity(intent);
                     closemenuFab();
                break;
            case R.id.fabHistoric:
                intent.setClass(MainActivity.this, ActivityHistoric.class);
                startActivity(intent);
                   closemenuFab();
                break;
        }
    }

    public void searchCity(String location, String city, int eixos, double fuelKm, double fuelValue){
        city = city.replace(" ", "%20");
        String TOKEN = getString(R.string.access_token_google);
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address="+city+"&key=" + TOKEN;
      findViewById(R.id.layout_progress).setVisibility(View.VISIBLE);
        Ion.with(this)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            JsonArray jsonArray = result.get("results").getAsJsonArray();
                            if(jsonArray.size() > 0){
                                JsonObject jsonobject = jsonArray.get(0).getAsJsonObject();
                                JsonObject objGeometry = jsonobject.get("geometry").getAsJsonObject();
                                JsonObject objLocation = objGeometry.getAsJsonObject("location");
                                String Latitude = (objLocation.get("lat").getAsString());
                                String Longitude = (objLocation.get("lng").getAsString());

                                JsonArray jsonArrayCity = jsonobject.get("address_components").getAsJsonArray();

                                String city = "";
                                String estate = "";
                                for(int i = 0; i < jsonArrayCity.size(); i++){
                                    JsonObject jsonObject = jsonArrayCity.get(i).getAsJsonObject();
                                    JsonArray json = jsonObject.get("types").getAsJsonArray();
                                    if (String.valueOf(json.get(0).getAsString()).equals("administrative_area_level_2")){
                                        city = jsonObject.get("long_name").getAsString();
                                    }
                                    if (String.valueOf(json.get(0).getAsString()).equals("administrative_area_level_1")){
                                        estate = jsonObject.get("short_name").getAsString();
                                   }
                                }
                                locationDestination = city + ", " + estate;
                                destinationPosition = Point.fromLngLat(Double.valueOf(Longitude), Double.valueOf(Latitude));

                                searchOrigin(location, eixos, fuelKm, fuelValue);

                            }else{
                                layoutNewRoute.setEnabled(true);
                                dialogMessage("O destino da rota informada não existe!");
                                findViewById(R.id.layout_progress).setVisibility(View.INVISIBLE);
                            }

                            } catch (Exception erro) {
                            layoutNewRoute.setEnabled(true);
                            dialogMessage("Falha ao buscar dados de destino. Tente Novamente!");
                            findViewById(R.id.layout_progress).setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    public void searchOrigin(String location, int eixos, double fuelKm, double fuelValue){
        location = location.replace(" ", "%20");
        String TOKEN = getString(R.string.access_token_google);
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address="+location+"&key=" + TOKEN;
        findViewById(R.id.layout_progress).setVisibility(View.VISIBLE);
        Ion.with(this)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            JsonArray jsonArray = result.get("results").getAsJsonArray();
                            if(jsonArray.size() > 0){
                                JsonObject jsonobject = jsonArray.get(0).getAsJsonObject();
                                JsonObject objGeometry = jsonobject.get("geometry").getAsJsonObject();
                                JsonObject objLocation = objGeometry.getAsJsonObject("location");
                                String Latitude = (objLocation.get("lat").getAsString());
                                String Longitude = (objLocation.get("lng").getAsString());

                                JsonArray jsonArrayCity = jsonobject.get("address_components").getAsJsonArray();
                                String city = "";
                                String estate = "";
                                for(int i = 0; i < jsonArrayCity.size(); i++){
                                    JsonObject jsonObject = jsonArrayCity.get(i).getAsJsonObject();
                                    JsonArray json = jsonObject.get("types").getAsJsonArray();
                                    if (String.valueOf(json.get(0).getAsString()).equals("administrative_area_level_2")){
                                        city = jsonObject.get("long_name").getAsString();
                                    }
                                    if (String.valueOf(json.get(0).getAsString()).equals("administrative_area_level_1")){
                                        estate = jsonObject.get("short_name").getAsString();
                                    }
                                }
                                locationOrigin = city + ", " + estate;
                                originPosition = Point.fromLngLat(Double.valueOf(Longitude), Double.valueOf(Latitude));
                                getInformation(eixos, fuelKm, fuelValue);

                            }else{
                                layoutNewRoute.setEnabled(true);
                                dialogMessage("A origem da rota informada não existe!");
                                findViewById(R.id.layout_progress).setVisibility(View.INVISIBLE);
                            }
                        } catch (Exception erro) {
                            layoutNewRoute.setEnabled(true);
                            dialogMessage("Falha ao buscar dados de origem. Tente novamente");
                            findViewById(R.id.layout_progress).setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    public void getInformation(int eixos, double fuelKm, double fuelValue){
        ControllerRoutes controllerRoutes = new ControllerRoutes(this);

        String url = "https://geo.api.truckpad.io/v1/route";
        findViewById(R.id.layout_progress).setVisibility(View.VISIBLE);
        Ion.with(MainActivity.this)
                .load(url)
                .setJsonObjectBody(controllerRoutes.createJson(fuelKm, fuelValue, originPosition, destinationPosition))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            JsonArray jsonArray = result.get("points").getAsJsonArray();
                            double distance = result.get("distance").getAsInt(); //meters
                            double duration = result.get("duration").getAsInt(); //seconds
                            boolean hasTolls = result.get("has_tolls").getAsBoolean();
                            int tollCount = result.get("toll_count").getAsInt();
                            double tollCost = result.get("toll_cost").getAsDouble();
                            String tollCostUnit = result.get("toll_cost_unit").getAsString();
                            double fuelUsage = result.get("fuel_usage").getAsDouble();
                            double fuelCost = result.get("fuel_cost").getAsDouble();
                            String fuelCostUnit = result.get("fuel_cost").getAsString();

                            textOrigin.setText(locationOrigin);
                            textDestination.setText(locationDestination);
                            double Km = distance/1000;
                            double Time = duration/60;
                            Time = Time/60;
                            DecimalFormat decimalFormat = new DecimalFormat("0.0");
                            textDistance.setText(String.valueOf(decimalFormat.format(Km)) + "Km");
                            textTime.setText(String.valueOf(decimalFormat.format(Time)) + " Hr");

                           route(1, eixos, tollCount, distance, Time, fuelKm, fuelValue, tollCost, fuelUsage, fuelCost, Double.valueOf(originPosition.latitude()),
                                    Double.valueOf(originPosition.latitude()), Double.valueOf(destinationPosition.latitude()), Double.valueOf(destinationPosition.longitude()),
                                    hasTolls, tollCostUnit, fuelCostUnit);
                            searchPriceANTT();
                        } catch (Exception exception) {
                            layoutNewRoute.setEnabled(true);
                            dialogMessage("Falha ao buscar dados darota. Tente novamente!");
                            findViewById(R.id.layout_progress).setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    public void searchLocation(String latitude, String longitude){

        String TOKEN = getString(R.string.access_token_google);
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + latitude + "," + longitude+ "&key=" + TOKEN;
        Ion.with(this)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            JsonArray jsonArray = result.get("results").getAsJsonArray();
                            JsonObject jsonobject = jsonArray.get(0).getAsJsonObject();
                            locationOrigin = jsonobject.get("formatted_address").getAsString();
                            editLocation.setText(locationOrigin);
                        } catch (Exception erro) {
                            Toast.makeText(getBaseContext(), "Falha ao buscar endereço de origem", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void searchPriceANTT(){
        ControllerRoutes controllerRoutes = new ControllerRoutes(this);
        String url = "https://tictac.api.truckpad.io/v1/antt_price/all";
        findViewById(R.id.layout_progress).setVisibility(View.VISIBLE);
        Ion.with(MainActivity.this)
                .load(url)
                .setJsonObjectBody(controllerRoutes.createJsonPriceANTT(eixos, (distance/1000), true))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            priceFrigorificada = result.get("frigorificada").getAsDouble();
                            priceGeral = result.get("geral").getAsDouble();
                            priceGranel = result.get("granel").getAsDouble();
                            priceNeogranel = result.get("neogranel").getAsDouble();
                            pricePerigosa = result.get("perigosa").getAsDouble();
                            getRoute(originPosition, destinationPosition);
                        } catch (Exception exception) {
                            layoutNewRoute.setEnabled(true);
                            dialogMessage("Falha ao buscar tabela ANTT. Tente novamente!");
                            findViewById(R.id.layout_progress).setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        map = mapboxMap;
        map.addOnMapClickListener(this);
        enableLocation();
    }

    private void enableLocation(){
        if(PermissionsManager.areLocationPermissionsGranted(this)){
            initializeLocationEngine();
            initializeLocationLayer();
        }else{
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @SuppressWarnings("MissingPermission")
    private void initializeLocationEngine(){
        locationEngine = new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.activate();

        Location lastLocation = locationEngine.getLastLocation();
        if(lastLocation != null){
            originLocation = lastLocation;
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), 15 ));
            searchLocation(String.valueOf(lastLocation.getLatitude()), String.valueOf(lastLocation.getLongitude()));

        }else{
            locationEngine.addLocationEngineListener(this);
        }
    }

    @SuppressWarnings("MissingPermission")
    private  void initializeLocationLayer(){
        locationLayerPlugin = new LocationLayerPlugin(mapView, map, locationEngine);
        locationLayerPlugin.setLocationLayerEnabled(true);
        locationLayerPlugin.setCameraMode(CameraMode.TRACKING);
        locationLayerPlugin.setRenderMode(RenderMode.NORMAL);
    }

    private void setCameraPosition(Location location){
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15 ));
    }

    private void setCameraNewPosition(Location location){
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15 ));
    }

    private void setCameraDestinationPosition(Point destination){
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(destination.latitude(), destination.longitude()), 11 ));
    }

    @Override
    public void onMapClick(@NonNull LatLng point) {
        if(destinationMarker != null){
            map.removeMarker(destinationMarker);
        }
    }

    private  void getRoute(Point origin, Point destination){
        NavigationRoute.builder()
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        if(response.body() == null){
                            Toast.makeText(MainActivity.this, "Não existe rota para o destino informado!", Toast.LENGTH_LONG).show();
                            return;
                        }else if(response.body().routes().size() == 0){
                            Toast.makeText(MainActivity.this, "Sem rota no momento!", Toast.LENGTH_LONG).show();
                            return;
                        }

                        DirectionsRoute currentRoute = response.body().routes().get(0);

                        if (navigationMapRoute != null){
                            navigationMapRoute.removeRoute();
                        }else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, map);
                        }
                        navigationMapRoute.addRoute(currentRoute);
                        setCameraDestinationPosition(destination);
                        saveRoute();
                        showInforationLayout();
                        findViewById(R.id.layout_progress).setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                        layoutNewRoute.setEnabled(true);
                        dialogMessage("Ocorreu um erro inesperado ao buscar a rota. tente novamente!");
                        findViewById(R.id.layout_progress).setVisibility(View.INVISIBLE);
                    }
                });
    }


    private void insertTruck(){
        try{
            ControllerTruck controller = new ControllerTruck(this);
            editEixos.setText(String.valueOf(controller.returnTruck().get(0).getEixos()));
            editFuelKm.setText(String.valueOf(controller.returnTruck().get(0).getFuelKm()));
        }catch(Exception e){

        }
    }

    private void route(int _id, int _eixos, int _tollCount, double _distance, double _time, double _fuelKm, double _fuelValue,
                      double _tollCost, double _fuelUsage, double _fuelCost, double _originPositionLatitude,
                      double _originPositionLongitude, double _destinationPositionLatitude, double _destinationPositionLongitude,
                      boolean _hasTolls, String _tollCostUnit, String _fuelCostUnit){
        id = _id;
        eixos = _eixos;
        tollCount = _tollCount;
        distance = _distance;
        time = _time;
        fuelKm = _fuelKm;
        fuelValue = _fuelValue;
        tollCost = _tollCost;
        fuelUsage = _fuelUsage;
        tollCount = _tollCount;
        originPositionLatitude = _originPositionLatitude;
        originPositionLongitude =  _originPositionLongitude;
        destinationPositionLatitude =  _destinationPositionLatitude;
        destinationPositionLongitude =  _destinationPositionLongitude;
        hasTolls = _hasTolls;
        fuelCost = _fuelCost;
        fuelCostUnit = _fuelCostUnit;
    }

    private void showPageDetalRoute(){
        Intent it = new Intent(MainActivity.this, ActivityDetalRoute.class);
        it.putExtra("id", id);
        it.putExtra("eixos", eixos);
        it.putExtra("tollCount", tollCount);
        it.putExtra("km", distance);
        it.putExtra("time", time);
        it.putExtra("fuelKm", fuelKm);
        it.putExtra("fuelValue", fuelValue);
        it.putExtra("tollCost", tollCost);
        it.putExtra("fuelUsage", fuelUsage);
        it.putExtra("tollCount", tollCount);
        it.putExtra("originPositionLatitude", originPositionLatitude);
        it.putExtra("originPositionLongitude", originPositionLongitude);
        it.putExtra("destinationPositionLatitude", destinationPositionLatitude);
        it.putExtra("destinationPositionLongitude", destinationPositionLongitude);
        it.putExtra("hasTolls", hasTolls);
        it.putExtra("tollCostUnit", tollCostUnit);
        it.putExtra("fuelCost", fuelCost);
        it.putExtra("locationOrigin", locationOrigin);
        it.putExtra("locationDestination", locationDestination);
        it.putExtra("priceFrigorificada", priceFrigorificada);
        it.putExtra("priceGeral", priceGeral);
        it.putExtra("priceGranel", priceGranel);
        it.putExtra("priceNeogranel", priceNeogranel);
        it.putExtra("pricePerigosa", pricePerigosa);
        it.putExtra("page", "Detalhes da rota");
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(it);
    }

    private void saveRoute(){
        SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String dateFormted = formataData.format(date);

        ControllerRoutes controllerRoutes = new ControllerRoutes(this);
        controllerRoutes.save(eixos, tollCount, distance, time, fuelKm, fuelValue, tollCost, fuelUsage, fuelCost, originPositionLatitude,
                originPositionLongitude, destinationPositionLatitude, destinationPositionLongitude, priceFrigorificada, priceGeral, priceGranel, priceNeogranel, pricePerigosa, String.valueOf(hasTolls),
                locationOrigin, locationDestination, dateFormted);
    }

    public void dialogMessage(String Title){
        Dialog dialog;
        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_message);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        LinearLayout layoutDialogClose = dialog.findViewById(R.id.layoutDialogClose);

        Typeface fontBold = Typeface.createFromAsset(getBaseContext().getAssets(),"Spartan-Bold.ttf");
        Typeface fontMedium = Typeface.createFromAsset(getBaseContext().getAssets(),"Spartan-Medium.ttf");

        TextView textTitleDialog = dialog.findViewById(R.id.textTitleDialog);
        textTitleDialog.setText(Title);
        textTitleDialog.setTypeface(fontBold);

        layoutDialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    public void declarations(){

        fabMain =  findViewById(R.id.fabMain);
        fabTruck = findViewById(R.id.fabTruck);
        fabHistoric = findViewById(R.id.fabHistoric);

        layoutDetal = findViewById(R.id.layoutDetal);

        textOrigin = findViewById(R.id.textOrigin);
        textDestination = findViewById(R.id.textDestination);
        textDistance = findViewById(R.id.textDistance);
        textTime = findViewById(R.id.textTime);
        layoutCancel = findViewById(R.id.layoutCancel);
        layoutDetal = findViewById(R.id.layoutDetal);
        labelDistance = findViewById(R.id.labelDistance);
        labelTime = findViewById(R.id.labelTime);
        layoutInformation = findViewById(R.id.layoutInformation);
        textCancel = findViewById(R.id.textCancel);
        textDetal = findViewById(R.id.textDetal);

        imageMyLocation = findViewById(R.id.imageMyLocation);
        editLocation = findViewById(R.id.editLocation);
        editDestination = findViewById(R.id.editDestination);
        editEixos = findViewById(R.id.editEixos);
        editFuelKm = findViewById(R.id.editFuelKm);
        editValueFuel = findViewById(R.id.editValueFuel);
        layoutButtonRoute = findViewById(R.id.layoutButtonRoute);

        imageCloseNewRoute = findViewById(R.id.imageCloseNewRoute);

        layoutRoute = findViewById(R.id.layoutRoute);
        layoutNewRoute = findViewById(R.id.layoutNewRoute);

        textProgress = findViewById(R.id.textProgress);
        mapView = findViewById(R.id.map);

        Typeface fontBold = Typeface.createFromAsset(getBaseContext().getAssets(),"Spartan-Bold.ttf");
        Typeface fontMedium = Typeface.createFromAsset(getBaseContext().getAssets(),"Spartan-Medium.ttf");
        Typeface fontThin = Typeface.createFromAsset(getBaseContext().getAssets(),"Spartan-Thin.ttf");

        textProgress.setTypeface(fontBold);
        textOrigin.setTypeface(fontBold);
        textDestination.setTypeface(fontBold);
        textDistance.setTypeface(fontBold);
        textTime.setTypeface(fontBold);
        labelDistance.setTypeface(fontThin);
        labelTime.setTypeface(fontThin);

        textCancel.setTypeface(fontThin);
        textDetal.setTypeface(fontThin);
    }

    public static void hideKeyboard(Context context, View editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void showInforationLayout(){
        ViewGroup.LayoutParams params = layoutInformation.getLayoutParams();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutInformation.setLayoutParams(params);
        layoutNewRoute.setEnabled(false);
    }

    private void finishInformationDetal(){
        ViewGroup.LayoutParams params = layoutInformation.getLayoutParams();
        params.height =  1;
        layoutInformation.setLayoutParams(params);
        layoutNewRoute.setEnabled(true);

        if (navigationMapRoute != null){
            navigationMapRoute.removeRoute();
            setCameraNewPosition(originLocation);
        }
    }

    private void showNewRoute(){
        ViewGroup.LayoutParams params = layoutRoute.getLayoutParams();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutRoute.setLayoutParams(params);
        insertTruck();
    }

    private void finishNewRoute(){
        ViewGroup.LayoutParams params = layoutRoute.getLayoutParams();
        params.height = 0;
        layoutRoute.setLayoutParams(params);
    }

    @Override
    public void onBackPressed(){

        ViewGroup.LayoutParams paramsNewRoute = layoutRoute.getLayoutParams();
        ViewGroup.LayoutParams paramsInformation = layoutInformation.getLayoutParams();

        if(paramsInformation.height != ViewGroup.LayoutParams.MATCH_PARENT && paramsNewRoute.height != ViewGroup.LayoutParams.MATCH_PARENT){
            finish();
        }

        if(paramsNewRoute.height == ViewGroup.LayoutParams.MATCH_PARENT){
            finishNewRoute();
        }

        if(paramsInformation.height == ViewGroup.LayoutParams.MATCH_PARENT){
            finishInformationDetal();
        }
    }

    @Override
    @SuppressWarnings("MissingPermission")
    public void onConnected() {
        locationEngine.requestLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null){
            originLocation = location;
            setCameraPosition(location);
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if(granted){
            enableLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    @SuppressWarnings("MissingPermission")
    protected void onStart() {
        super.onStart();
        if(locationEngine != null){
            locationEngine.requestLocationUpdates();
        }
        if(locationLayerPlugin != null){
            locationLayerPlugin.onStart();
        }
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(locationEngine != null){
            locationEngine.removeLocationUpdates();
        }
        if (locationLayerPlugin != null){
            locationLayerPlugin.onStop();
        }
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(locationEngine != null){
            locationEngine.deactivate();
        }
        mapView.onDestroy();
    }

}