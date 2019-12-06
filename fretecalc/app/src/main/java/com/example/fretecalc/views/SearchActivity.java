package com.example.fretecalc.views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fretecalc.R;
import com.example.fretecalc.viewmodels.SearchActivityViewModel;
import com.example.fretecalc.views.adapters.RecyclerSearchAdapter;
import com.example.fretecalc.views.interfaces.PredictionListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;

import java.util.Arrays;
import java.util.List;

import static com.example.fretecalc.utils.ApisConstants.GOOGLE_API_KEY;
import static com.example.fretecalc.views.RequestActivity.DESTINATION_PLACE_CODE;
import static com.example.fretecalc.views.RequestActivity.ORIGIN_PLACE_CODE;

public class SearchActivity extends AppCompatActivity implements PredictionListener {
    private static final int LOCATION_REQUEST_CODE = 1;
    public static final String DESCRIPTION_KEY = "description";
    public static final String LATITUDE_KEY = "latitude";
    public static final String LONGITUDE_KEY = "longitude";
    private EditText searchPlace;
    private TextView textLocation;
    private RecyclerView recyclerPrediction;
    private RecyclerSearchAdapter adapter;
    private FusedLocationProviderClient fusedLocationClient;
    private SearchActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initViews();

        searchPlace.requestFocus();
        searchPlace.addTextChangedListener(filterTextWatcher);

        textLocation.setOnClickListener(view -> {
            getLocationData();
        });

        viewModel.getPredictionList().observe(this, autocompletePredictions -> {
            adapter.updatePredictions(autocompletePredictions);
        });

        displayError();
    }

    private void initViews() {
        searchPlace = findViewById(R.id.edit_search_place);
        textLocation = findViewById(R.id.text_search_location);
        recyclerPrediction = findViewById(R.id.recycler_search_autocomplete);
        viewModel = ViewModelProviders.of(this).get(SearchActivityViewModel.class);
        adapter = new RecyclerSearchAdapter(this, this);
        recyclerPrediction.setLayoutManager(new LinearLayoutManager(this));
        recyclerPrediction.addItemDecoration(new DividerItemDecoration(recyclerPrediction.getContext(),
                DividerItemDecoration.VERTICAL));
        recyclerPrediction.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onSupportNavigateUp() {
        setResult(RESULT_CANCELED);
        finish();
        return true;
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            if (!s.toString().equals("")) {
                viewModel.getPredictions(s);
                if (recyclerPrediction.getVisibility() == View.GONE) {
                    recyclerPrediction.setVisibility(View.VISIBLE);
                }
            } else {
                if (recyclerPrediction.getVisibility() == View.VISIBLE) {
                    recyclerPrediction.setVisibility(View.GONE);
                }
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };

    public void onLocationClick(String description, Double latitude, Double longitude) {
        Intent intent = new Intent();
        if (ORIGIN_PLACE_CODE == 100) {
            intent.putExtra(DESCRIPTION_KEY, description);
            intent.putExtra(LATITUDE_KEY, latitude);
            intent.putExtra(LONGITUDE_KEY, longitude);
        } else if (DESTINATION_PLACE_CODE == 200) {
            intent.putExtra(DESCRIPTION_KEY, description);
            intent.putExtra(LATITUDE_KEY, latitude);
            intent.putExtra(LONGITUDE_KEY, longitude);
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    private void getLocationData() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        int permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            onLocationClick("Sua localização", location.getLatitude(), location.getLongitude());
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                        if (location != null) {
                            onLocationClick("Sua localização", location.getLatitude(), location.getLongitude());
                        }
                    });
                } else {
                    Toast.makeText(this, "Permissão negada, escolha manualmente uma cidade", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    @Override
    public void onPredictionClick(AutocompletePrediction prediction) {
        String placeId = prediction.getPlaceId();

        viewModel.getPlaceById(placeId);

        viewModel.getPlace().observe(this, place -> {
            Intent intent = new Intent();
            if (ORIGIN_PLACE_CODE == 100) {
                intent.putExtra(DESCRIPTION_KEY, place.getName());
                intent.putExtra(LATITUDE_KEY, place.getLatLng().latitude);
                intent.putExtra(LONGITUDE_KEY, place.getLatLng().longitude);
            } else if (DESTINATION_PLACE_CODE == 200) {
                intent.putExtra(DESCRIPTION_KEY, place.getName());
                intent.putExtra(LATITUDE_KEY, place.getLatLng().latitude);
                intent.putExtra(LONGITUDE_KEY, place.getLatLng().longitude);
            }
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    private void displayError() {
        viewModel.getError().observe(this, s -> {
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        });
    }
}
