package com.example.fretecalc.views;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.fretecalc.R;
import com.example.fretecalc.models.routes.RouteResponse;
import com.example.fretecalc.viewmodels.ResultActivityViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.List;
import java.util.Locale;

import static com.example.fretecalc.utils.ConvertersUtils.bitmapDescriptorFromVector;
import static com.example.fretecalc.utils.ConvertersUtils.distanceUnits;
import static com.example.fretecalc.utils.ConvertersUtils.timeUnits;
import static com.example.fretecalc.views.HistoryFragment.SEARCH_DB_KEY;
import static com.example.fretecalc.views.RequestActivity.SEARCH_KEY;

public class ResultActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private RelativeLayout layoutBottomSheet;
    private BottomSheetBehavior sheetBehavior;
    private ResultActivityViewModel viewModel;
    private TextView textOrigin;
    private TextView textDestination;
    private TextView textCost;
    private TextView textDistance;
    private TextView textTime;
    private TextView textTollCost;
    private TextView textTollNumber;
    private TextView textFuelCost;
    private TextView textFuelNumber;
    private TextView textGeral;
    private TextView textFrigorificada;
    private TextView textGranel;
    private TextView textNeogranel;
    private TextView textPerigosa;
    private Long searchId;
    private RelativeLayout containerToll;
    private ConstraintLayout progressBar;
    private LinearLayout containerContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initViews();

        displayError();
        displayLoading();

        if (getIntent() != null) {
            if (getIntent().hasExtra(SEARCH_KEY)) {
                searchId = getIntent().getLongExtra(SEARCH_KEY, 0L);
                viewModel.searchById(searchId);
                viewModel.getRoute(searchId);
            } else if (getIntent().hasExtra(SEARCH_DB_KEY)) {
                searchId = getIntent().getLongExtra(SEARCH_DB_KEY, 0L);
                viewModel.searchById(searchId);
                viewModel.getLocalRoute(searchId);
            }
        }

        getSearchData();
        getResponseData();
        getPriceData();
    }

    private void initViews() {
        layoutBottomSheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        viewModel = ViewModelProviders.of(this).get(ResultActivityViewModel.class);
        textOrigin = findViewById(R.id.text_result_origin);
        textDestination = findViewById(R.id.text_result_destination);
        textCost = findViewById(R.id.text_result_cost);
        textDistance = findViewById(R.id.text_result_distance);
        textTime = findViewById(R.id.text_result_time);
        textTollCost = findViewById(R.id.text_result_toll);
        textTollNumber = findViewById(R.id.text_result_tollNumber);
        textFuelCost = findViewById(R.id.text_result_fuel);
        textFuelNumber = findViewById(R.id.text_result_fuelLiters);
        textGeral = findViewById(R.id.text_request_geral);
        textFrigorificada = findViewById(R.id.text_request_frigorificada);
        textGranel = findViewById(R.id.text_request_granel);
        textNeogranel = findViewById(R.id.text_request_neogranel);
        textPerigosa = findViewById(R.id.text_request_perigosa);
        containerToll = findViewById(R.id.container_result_toll);
        progressBar = findViewById(R.id.progress_result);
        containerContent = findViewById(R.id.container_bottom_sheet);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void getSearchData() {
        viewModel.getSearchData().observe(this, search1 -> {
            textOrigin.setText(search1.getOriginName());
            textDestination.setText(search1.getDestinationName());

            LatLng origin = new LatLng(search1.getRequest().getPlaces().get(0).getPoint().get(1), search1.getRequest().getPlaces().get(0).getPoint().get(0));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin, 3L));
        });
    }

    private void getResponseData() {
        viewModel.getRouteResponse().observe(ResultActivity.this, routeResponse -> {
            if (routeResponse != null) {
                textCost.setText(String.format(Locale.getDefault(), "R$ %.2f", routeResponse.getTotalCost()));
                textDistance.setText(String.format(Locale.getDefault(), "%.0f km", distanceUnits(routeResponse.getDistanceUnit(), routeResponse.getDistance())));
                if (routeResponse.getDuration() != null) {
                    textTime.setText(String.format(Locale.getDefault(), "%s", timeUnits(routeResponse.getDurationUnit(), routeResponse.getDuration())));
                }

                if (routeResponse.getHasTolls() != null && routeResponse.getHasTolls()) {
                    textTollCost.setText(String.format(Locale.getDefault(), "%s %.2f", routeResponse.getTollCostUnit(), routeResponse.getTollCost()));
                    textTollNumber.setText(String.format(Locale.getDefault(), "%d pedágios", routeResponse.getTollCount()));
                } else {
                    containerToll.setVisibility(View.GONE);
                }

                textFuelCost.setText(String.format(Locale.getDefault(), "%s %.2f", routeResponse.getFuelCostUnit(), routeResponse.getFuelCost()));
                textFuelNumber.setText(String.format(Locale.getDefault(), "%.1f litros", routeResponse.getFuelUsage()));

                showMapRoute(routeResponse);
            }
        });
    }

    private void getPriceData() {
        viewModel.getPriceResponse().observe(ResultActivity.this, priceResponse -> {
            if (priceResponse != null) {
                textGeral.setText(String.format(Locale.getDefault(), "R$ %.2f", priceResponse.getGeral()));
                textFrigorificada.setText(String.format(Locale.getDefault(), "R$ %.2f", priceResponse.getFrigorificada()));
                textGranel.setText(String.format(Locale.getDefault(), "R$ %.2f", priceResponse.getGranel()));
                textNeogranel.setText(String.format(Locale.getDefault(), "R$ %.2f", priceResponse.getNeogranel()));
                textPerigosa.setText(String.format(Locale.getDefault(), "R$ %.2f", priceResponse.getPerigosa()));
            }
        });
    }

    private void displayError() {
        viewModel.getError().observe(this, s -> {
            Toast.makeText(this, s, Toast.LENGTH_LONG).show();
            finish();
        });
    }

    private void displayLoading() {
        viewModel.getLoading().observe(this, aBoolean -> {
            if (aBoolean) {
                progressBar.setVisibility(View.VISIBLE);
                containerContent.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.GONE);
                containerContent.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showMapRoute(RouteResponse routeResponse) {
        List<List<Double>> route = routeResponse.getRoute().get(0);
        PolylineOptions options = new PolylineOptions().width(8).color(Color.GRAY).geodesic(true);
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (int z = 0; z < route.size(); z++) {
            LatLng point = new LatLng(route.get(z).get(1), route.get(z).get(0));
            options.add(point);
            boundsBuilder.include(point);
        }

        int routePadding = 0;
        LatLngBounds latLngBounds = boundsBuilder.build();
        mMap.addPolyline(options);
        mMap.setPadding(80, 180, 80, 350);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding));
        mMap.addMarker(new MarkerOptions().position(new LatLng(routeResponse.getPoints().get(0).getPoint().get(1), routeResponse.getPoints().get(0).getPoint().get(0))).title("Origem").icon(bitmapDescriptorFromVector(this, R.drawable.ic_marker)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(routeResponse.getPoints().get(1).getPoint().get(1), routeResponse.getPoints().get(1).getPoint().get(0))).title("Destino").icon(bitmapDescriptorFromVector(this, R.drawable.ic_marker)));
    }

}
