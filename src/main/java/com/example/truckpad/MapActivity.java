package com.example.truckpad;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.truckpad.model.Cidade;
import com.example.truckpad.model.Cidade_destino;
import com.example.truckpad.view.Adapter_cidade;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: map carregando");
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            init();
        }
    }

    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    //widgets
    private EditText mSearchText;

    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Button button_search;
    private List<Address> list;
    private Address address;
    private Adapter_cidade adapter_cidade;
    private ListView lvcidade;
    private String tipocid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mSearchText = findViewById(R.id.input_search);
        button_search = findViewById(R.id.button_search);
        lvcidade = findViewById(R.id.listv_cidade);

        Bundle bundle = getIntent().getExtras();
        tipocid = bundle.getString("cid");
        getLocationPermission();

    }

    private void init(){
        Log.d(TAG, "init: metodos");

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    //metodo de localização
                    geoLocate();
                }

                return false;
            }
        });

        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                int tipo;
                if (!(address == null)) {

                    //se a cidade pesquisada é origem ou destino
                    if (tipocid.equals("orig")) {
                        tipo = 1;
                        intent.putExtra("cid", newcidade(address));
                    }else {
                        tipo = 2;
                        intent.putExtra("cid", newcidade_dest(address));
                    }
                    setResult(tipo,intent);
                    finish();
                }
            }
        });
    }

    private void geoLocate(){
        Log.d(TAG, "geoLocate: localizando");

        String searchString = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(MapActivity.this);
        list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage() );
        }

        //se o list tiver conteudo
        if(list.size() > 0){
            if (!(address == null)){
                address = null;
            }
            address = list.get(0);
            List<Cidade> listcidade = new ArrayList<Cidade>();
            listcidade.add(newcidade(address));
            adapter_cidade = new Adapter_cidade(MapActivity.this,listcidade);
            lvcidade.setAdapter(adapter_cidade);
            button_search.setEnabled(true);

            //move a camera do maps
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()),
                    DEFAULT_ZOOM);
            Log.d(TAG, "geoLocate: Localizado com sucesso: " + address.toString());

        }
    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: localização atual");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: atualizada a localização!");
                            Location currentLocation = (Location) task.getResult();
                            list = new ArrayList<>();
                            Geocoder geocoder = new Geocoder(MapActivity.this);
                            try{
                                list = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(),1);
                                if(list.size() > 0){
                                    address = list.get(0);
                                    List<Cidade> listcidade = new ArrayList<Cidade>();
                                    listcidade.add(newcidade(address));
                                    //resultado da pesquisa para exibiçao simples em um  listview
                                    adapter_cidade = new Adapter_cidade(MapActivity.this,listcidade);
                                    lvcidade.setAdapter(adapter_cidade);
                                }
                            }catch (IOException e){
                                Log.e(TAG, "geoLocate: IOException: " + e.getMessage() );
                            }
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM);

                        }else{
                            Log.d(TAG, "onComplete: atualização de local falhou");
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    private void moveCamera(LatLng latLng, float zoom){
        Log.d(TAG, "moveCamera: movendo a camera: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void initMap(){
        Log.d(TAG, "initMap: carrega o mapa");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);
        getDeviceLocation();


    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: permissões");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    public Cidade newcidade(Address address){

        Cidade cidade = new Cidade(address.getAdminArea(),
                String.valueOf(address.getLatitude()),
                String.valueOf(address.getLongitude()),
                address.getPostalCode(),
                address.getSubAdminArea(),
                address.getSubLocality(),
                address.getThoroughfare());
        return cidade;
    }
    public Cidade_destino newcidade_dest(Address address){

        Cidade_destino cidade_destino = new Cidade_destino(address.getAdminArea(),
                String.valueOf(address.getLatitude()),
                String.valueOf(address.getLongitude()),
                address.getPostalCode(),
                address.getSubAdminArea(),
                address.getSubLocality(),
                address.getThoroughfare());
        return cidade_destino;
    }

    @Override
    public void onBackPressed() {

        int tipo = 100;
        setResult(tipo);
        finish();

    }


}











