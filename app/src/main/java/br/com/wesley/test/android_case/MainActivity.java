package br.com.wesley.test.android_case;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.List;
import java.util.Optional;

import br.com.wesley.test.android_case.model.DetalhesViagem;
import br.com.wesley.test.android_case.model.Place;
import br.com.wesley.test.android_case.model.PlaceRoute;
import br.com.wesley.test.android_case.model.PlaceRouteResponse;
import br.com.wesley.test.android_case.model.PrecoCargaResponse;
import br.com.wesley.test.android_case.utils.AutoCompleteTextWatcher;

import static br.com.wesley.test.android_case.utils.ConstantsUtils.TRIGGER_AUTO_COMPLETE;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private static final int PERMISSION_ID = 1;
    private AutoCompleteTextView edtOrigem;
    private AutoCompleteTextView edtDestino;
    private EditText edtQuantidadeEixos;
    private EditText edtConsumoMedio;
    private EditText edtPrecoDiesel;

    private MainPresenter presenter;
    private AutoSuggestAdapter origemAdapter;
    private AutoSuggestAdapter destinoAdapter;
    private FusedLocationProviderClient mFusedLocationClient;
    private PlaceRouteResponse placeRouteResponse;
    private Place origem;
    private Place destino;
    private int eixos;
    private AlertDialog alertDialogProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainPresenter(this);
        edtOrigem = findViewById(R.id.edtOrigem);
        edtDestino = findViewById(R.id.edtDestino);
        edtQuantidadeEixos = findViewById(R.id.edtQuantidadeEixos);
        edtConsumoMedio = findViewById(R.id.edtConsumoMedio);
        edtPrecoDiesel = findViewById(R.id.edtPrecoDiesel);
        Button btnCalcular = findViewById(R.id.btnCalcular);
        btnCalcular.setOnClickListener(v -> calcular());

        findViewById(R.id.btnMenosEixos)
                .setOnClickListener(v -> presenter.menosEixos(edtQuantidadeEixos.getText().toString()));
        findViewById(R.id.btnMaisEixos)
                .setOnClickListener(v -> presenter.maisEixos(edtQuantidadeEixos.getText().toString()));
        inicializaEdtOrigem();
        inicializaEdtDestino();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private void inicializaEdtDestino() {
        destinoAdapter = new AutoSuggestAdapter(this, android.R.layout.simple_dropdown_item_1line);
        edtDestino.setThreshold(3);
        edtDestino.setAdapter(destinoAdapter);
        Handler handlerDestino = new Handler(msg -> {
            if (msg.what == TRIGGER_AUTO_COMPLETE &&
                    !TextUtils.isEmpty(edtDestino.getText()) && edtDestino.getText().toString().length() > 2) {
                presenter.buscarDestino(edtDestino.getText().toString());
            }
            return false;
        });
        edtDestino.addTextChangedListener(new AutoCompleteTextWatcher(handlerDestino));
        edtDestino.setOnItemClickListener((parent, view, position, id) -> {
            Place place = destinoAdapter.getItem(position);
            edtDestino.setTag(place);
        });
    }

    private void inicializaEdtOrigem() {
        origemAdapter = new AutoSuggestAdapter(this,
                android.R.layout.simple_dropdown_item_1line);
        edtOrigem.setThreshold(3);
        edtOrigem.setAdapter(origemAdapter);
        Handler handlerOrigem = new Handler(msg -> {
            if (msg.what == TRIGGER_AUTO_COMPLETE &&
                    !TextUtils.isEmpty(edtOrigem.getText()) && edtOrigem.getText().toString().length() > 2) {
                presenter.buscarOrigem(edtOrigem.getText().toString());
            }
            return false;
        });
        edtOrigem.addTextChangedListener(new AutoCompleteTextWatcher(handlerOrigem));
        edtOrigem.setOnItemClickListener((parent, view, position, id) -> {
            Place place = origemAdapter.getItem(position);
            edtOrigem.setTag(place);
        });
    }

    @Override
    public void atualizaListaOrigem(List<Place> places) {
        Log.d("Main", "View::atualizaListaOrigem");
        origemAdapter.setData(places);
        origemAdapter.notifyDataSetChanged();
    }

    @Override
    public void atualizaListaDestino(List<Place> places) {
        Log.d("Main", "View::atualizaListaDestino");
        destinoAdapter.setData(places);
        destinoAdapter.notifyDataSetChanged();
    }

    @Override
    public void atualizaQuantidadeEixos(int eixos) {
        edtQuantidadeEixos.setText(String.valueOf(eixos));
    }

    @Override
    public void atualizarOrigemLocalizacaoAtual(Place place) {
        this.edtOrigem.setTag(place);
        this.edtOrigem.setText(place.getDisplayName());
    }

    public void calcular() {
        Optional<Object> origemOptional = Optional.ofNullable(edtOrigem.getTag());
        if (!origemOptional.isPresent()) {
            Toast.makeText(this, "Informe a origem.", Toast.LENGTH_LONG).show();
            return;
        }

        Optional<Object> destinoOptional = Optional.ofNullable(edtDestino.getTag());
        if (!destinoOptional.isPresent()) {
            Toast.makeText(this, "Informe o destino.", Toast.LENGTH_LONG).show();
            return;
        }

        String qtdEixosStr = edtQuantidadeEixos.getText().toString();
        if (TextUtils.isEmpty(qtdEixosStr)) {
            Toast.makeText(this, "Informe a quantidade de eixos.", Toast.LENGTH_LONG).show();
            return;
        }
        String consumoMedioStr = edtConsumoMedio.getText().toString();
        if (TextUtils.isEmpty(consumoMedioStr)) {
            Toast.makeText(this, "Informe o consumo médio.", Toast.LENGTH_LONG).show();
            return;
        }
        String precoDieselStr = edtPrecoDiesel.getText().toString();
        if (TextUtils.isEmpty(precoDieselStr)) {
            Toast.makeText(this, "Informe o preço do diesel.", Toast.LENGTH_LONG).show();
            return;
        }

        origem = (Place) origemOptional.get();
        destino = (Place) destinoOptional.get();
        eixos = Integer.parseInt(qtdEixosStr);

        PlaceRoute placeRoute = new PlaceRoute();
        placeRoute.setFuelConsumption(Double.parseDouble(consumoMedioStr));
        placeRoute.setFuelPrice(Double.parseDouble(precoDieselStr));
        placeRoute.setAxis(eixos);

        PlaceRoute.PlaceRoutePoint pointOrigem = new PlaceRoute.PlaceRoutePoint(origem.getPoint());
        PlaceRoute.PlaceRoutePoint pointDestino = new PlaceRoute.PlaceRoutePoint(destino.getPoint());

        placeRoute.setPlaces(new PlaceRoute.PlaceRoutePoint[]{pointOrigem, pointDestino});

        presenter.buscarInformacoesRotas(placeRoute);
    }

    @Override
    public void abrirConfiguracoesHabilitarLocalizacao() {
        Toast.makeText(this, "Habilite a localização", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }

    @Override
    public void mostraInformacoesRotas(PlaceRouteResponse placeRouteResponse) {
        this.placeRouteResponse = placeRouteResponse;
        this.presenter.buscarInformacoesPrecoCarga((double) this.placeRouteResponse.getDistance(), eixos);
    }

    @Override
    public void mostraInformacoesPrecoCarga(PrecoCargaResponse precoCargaResponse) {
        DetalhesViagem viagem = new DetalhesViagem();
        viagem.setOrigem(origem.getDisplayName());
        viagem.setDestino(destino.getDisplayName());
        viagem.setEixos(eixos);
        viagem.setDistancia(placeRouteResponse.getDistance());
        viagem.setDuracao(placeRouteResponse.getDuration());
        viagem.setPedagio(placeRouteResponse.getTollCost());
        viagem.setConsumoCombustivel(placeRouteResponse.getFuelUsage());
        viagem.setTotalCombustivel(placeRouteResponse.getFuelCost());
        viagem.setTotal(placeRouteResponse.getTotalCost());
        viagem.setValorFreteGeral(precoCargaResponse.getGeral());
        viagem.setValorFreteGranel(precoCargaResponse.getGranel());
        viagem.setValorFreteNeogranel(precoCargaResponse.getNeogranel());
        viagem.setValorFreteFrigorificada(precoCargaResponse.getFrigorificada());
        viagem.setValorFretePerigosa(precoCargaResponse.getPerigosa());

        Intent intent = new Intent(this, DetalhesRotaActivity.class);
        intent.putExtra("viagem", viagem);
        startActivity(intent);
    }

    @Override
    public void mensagemError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showPregressBar(String mensagem) {
        if (alertDialogProgress == null || !alertDialogProgress.isShowing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = LayoutInflater.from(this).inflate(R.layout.progress_dialog, null, false);
            if (!TextUtils.isEmpty(mensagem)) {
                TextView txtMensagem = view.findViewById(R.id.txtMensagem);
                txtMensagem.setText(mensagem);
            }
            builder.setView(view);
            alertDialogProgress = builder.create();
            alertDialogProgress.show();
        }
    }

    @Override
    public void showPregressBar() {
       showPregressBar(null);
    }

    @Override
    public void hidePregressBar() {
        if (alertDialogProgress != null && alertDialogProgress.isShowing()) {
            alertDialogProgress.dismiss();
        }
    }

    public void getLastLocation(View view) {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                showPregressBar(getString(R.string.carregando_localizacao_atual));
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        task -> {
                            Location location = task.getResult();
                            if (location == null) {
                                requestNewLocationData();
                            } else {
                                Place place = new Place();
                                place.setPoint(new double[]{location.getLatitude(), location.getLongitude()});
                                buscarDescricaoLocalizacao(place);
                            }
                        }
                );
            } else {
                abrirConfiguracoesHabilitarLocalizacao();
            }
        } else {
            requestPermissions();
        }
    }

    private void buscarDescricaoLocalizacao(Place place) {
        presenter.buscarLocalizacaoAtual(place.getPoint()[0], place.getPoint()[1]);
    }

    private void requestNewLocationData() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager != null && (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation(null);
            }
        }
    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            Place place = new Place();
            place.setPoint(new double[]{mLastLocation.getLatitude(), mLastLocation.getLongitude()});
            buscarDescricaoLocalizacao(place);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hidePregressBar();
    }
}
