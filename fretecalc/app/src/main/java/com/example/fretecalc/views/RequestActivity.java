package com.example.fretecalc.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.fretecalc.R;
import com.example.fretecalc.models.routes.Locality;
import com.example.fretecalc.models.routes.RouteRequest;
import com.example.fretecalc.models.routes.Search;
import com.example.fretecalc.utils.SingleClickListener;
import com.example.fretecalc.viewmodels.RequestActivityViewModel;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.fretecalc.utils.ApisConstants.GOOGLE_API_KEY;
import static com.example.fretecalc.views.SearchActivity.DESCRIPTION_KEY;
import static com.example.fretecalc.views.SearchActivity.LATITUDE_KEY;
import static com.example.fretecalc.views.SearchActivity.LONGITUDE_KEY;

public class RequestActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener {
    public static final int ORIGIN_PLACE_CODE = 100;
    public static final int DESTINATION_PLACE_CODE = 200;
    public static final String SEARCH_KEY = "search";
    private TextInputLayout inputAxis;
    private TextInputLayout inputOrigin;
    private TextInputLayout inputDestination;
    private TextInputLayout inputConsumption;
    private TextInputLayout inputPrice;
    private MaterialButton buttonCalculate;
    private RequestActivityViewModel viewModel;
    private SharedPreferences sharedPreferences;
    private List<Locality> localities = new ArrayList<>();
    private List<Double> originCoords;
    private List<Double> destinationCoords;
    private RouteRequest routeRequest;
    private Search search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Procurar");
        }

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), GOOGLE_API_KEY);
        }

        initViews();
        replaceFragment(new HistoryFragment());

        if (sharedPreferences.contains("axis")) {
            getUserPreferences();
        } else {
            String defaultAxis = "2";
            inputAxis.getEditText().setText(defaultAxis);
        }

        localities.clear();

        inputAxis.getEditText().setOnClickListener(new SingleClickListener() {
            @Override
            public void onClicked(View v) {
                showAxisPicker(inputAxis.getEditText());
            }
        });

        inputOrigin.getEditText().setOnClickListener(new SingleClickListener() {
            @Override
            public void onClicked(View v) {
                startActivityForResult(new Intent(RequestActivity.this, SearchActivity.class), ORIGIN_PLACE_CODE);

            }
        });

        inputDestination.getEditText().setOnClickListener(new SingleClickListener() {
            @Override
            public void onClicked(View v) {
                startActivityForResult(new Intent(RequestActivity.this, SearchActivity.class), DESTINATION_PLACE_CODE);
            }
        });

        buttonCalculate.setOnClickListener(new SingleClickListener() {
            @Override
            public void onClicked(View v) {
                if (fieldsAreValid() && originDestinationNotEquals(originCoords, destinationCoords)) {
                    saveSearch();
                    Long id = viewModel.createSearch(search).blockingGet();
                    openResults(id);
                }
            }
        });

    }

    private void initViews() {
        sharedPreferences = getSharedPreferences("vehiclePrefs", MODE_PRIVATE);
        inputAxis = findViewById(R.id.input_request_axis);
        inputOrigin = findViewById(R.id.input_request_origin);
        inputDestination = findViewById(R.id.input_request_destination);
        inputConsumption = findViewById(R.id.input_request_consumption);
        inputPrice = findViewById(R.id.input_request_price);
        buttonCalculate = findViewById(R.id.button_request_calculate);
        viewModel = ViewModelProviders.of(this).get(RequestActivityViewModel.class);
    }

    private void showAxisPicker(View view) {
        AxisPickerDialog fragment = new AxisPickerDialog();
        fragment.setValueChangeListener(this);
        fragment.show(getSupportFragmentManager(), "axis picker");
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
        String value = String.valueOf(numberPicker.getValue());
        inputAxis.getEditText().setText(value);
    }

    private void saveSearch() {
        localities.clear();
        localities.add(new Locality(originCoords));
        localities.add(new Locality(destinationCoords));
        Long axis = Long.parseLong(inputAxis.getEditText().getText().toString());
        Double fuel = Double.parseDouble(inputConsumption.getEditText().getText().toString().replace(',', '.'));
        Double price = Double.parseDouble(inputPrice.getEditText().getText().toString().replace(',', '.'));
        String originName = inputOrigin.getEditText().getText().toString();
        String destinationName = inputDestination.getEditText().getText().toString();

        routeRequest = new RouteRequest(fuel, price, localities);
        saveAsPreferences(axis, fuel.floatValue(), price.floatValue());
        search = new Search(routeRequest, axis, originName, destinationName);
    }

    private void openResults(Long id) {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra(SEARCH_KEY, id);
        startActivity(intent);

    }

    public boolean fieldsAreValid() {
        if (isNullOrEmpty(inputOrigin.getEditText().getText().toString())) {
            inputOrigin.setError("Escolha uma origem");
            return false;
        } else if (isNullOrEmpty(inputDestination.getEditText().getText().toString())) {
            inputOrigin.setErrorEnabled(false);
            inputDestination.setError("Escolha um destino");
            return false;
        } else if (isNullOrEmpty(inputAxis.getEditText().getText().toString())) {
            inputOrigin.setErrorEnabled(false);
            inputDestination.setErrorEnabled(false);
            Toast.makeText(this, "Selecione a quantidade de eixos", Toast.LENGTH_LONG).show();
            return false;
        } else if (isNullOrEmpty(inputConsumption.getEditText().getText().toString())) {
            inputOrigin.setErrorEnabled(false);
            inputDestination.setErrorEnabled(false);
            Toast.makeText(this, "Selecione o consumo, em km/l", Toast.LENGTH_LONG).show();
            inputConsumption.requestFocus();
            return false;
        } else if (isNullOrEmpty(inputPrice.getEditText().getText().toString())) {
            inputOrigin.setErrorEnabled(false);
            inputDestination.setErrorEnabled(false);
            Toast.makeText(this, "Selecione o preço do diesel, em reais", Toast.LENGTH_LONG).show();
            inputPrice.requestFocus();
            return false;
        } else {
            inputOrigin.setErrorEnabled(false);
            inputDestination.setErrorEnabled(false);
            return true;
        }
    }

    public boolean isNullOrEmpty(String string) {
        return TextUtils.isEmpty(string);
    }

    public boolean originDestinationNotEquals(List<Double> originList, List<Double> destinationList) {
        Location lOrigin = new Location("");
        lOrigin.setLongitude(originList.get(0));
        lOrigin.setLatitude(originList.get(1));
        Location lDestination = new Location("");
        lDestination.setLongitude(destinationList.get(0));
        lDestination.setLatitude(destinationList.get(1));

        if (lOrigin.distanceTo(lDestination) < 500F) {
            Toast.makeText(this, "Origem e destino precisam ser maior que 500 metros", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case ORIGIN_PLACE_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    String description = data.getStringExtra(DESCRIPTION_KEY);
                    Double originLatitude = data.getDoubleExtra(LATITUDE_KEY, 0.0);
                    Double originLongitude = data.getDoubleExtra(LONGITUDE_KEY, 0.0);
                    inputOrigin.getEditText().setText(description);
                    originCoords = new ArrayList<>();
                    originCoords.add(originLongitude);
                    originCoords.add(originLatitude);
                }
                break;
            case DESTINATION_PLACE_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    String description = data.getStringExtra(DESCRIPTION_KEY);
                    Double destinationLatitude = data.getDoubleExtra(LATITUDE_KEY, 0.0);
                    Double destinationLongitude = data.getDoubleExtra(LONGITUDE_KEY, 0.0);
                    inputDestination.getEditText().setText(description);
                    destinationCoords = new ArrayList<>();
                    destinationCoords.add(destinationLongitude);
                    destinationCoords.add(destinationLatitude);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    protected void saveAsPreferences(Long axis, Float fuel, Float price) {
        SharedPreferences.Editor preferencesEditor = getSharedPreferences("vehiclePrefs", MODE_PRIVATE).edit();
        preferencesEditor.putLong("axis", axis);
        preferencesEditor.putFloat("fuel", fuel);
        preferencesEditor.putFloat("price", price);
        preferencesEditor.commit();
    }

    public void getUserPreferences() {

        if (sharedPreferences.contains("axis")) {
            Long axis = sharedPreferences.getLong("axis", 2L);
            inputAxis.getEditText().setText(String.format(Locale.getDefault(), "%o", axis));
        }

        if (sharedPreferences.contains("fuel")) {
            Float fuel = sharedPreferences.getFloat("fuel", 0.0F);
            inputConsumption.getEditText().setText(String.format(Locale.getDefault(), "%.1f", fuel));
        }

        if (sharedPreferences.contains("price")) {
            Float price = sharedPreferences.getFloat("price", 0.0F);
            inputPrice.getEditText().setText(String.format(Locale.getDefault(), "%.3f", price));
        }
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_history, fragment)
                .commit();
    }

}
