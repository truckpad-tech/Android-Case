package br.com.wesley.test.android_case;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.util.Locale;

import br.com.wesley.test.android_case.model.DetalhesViagem;


public class DetalhesRotaActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_rota);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        Bundle extras = getIntent().getExtras();

        if (extras == null || !extras.containsKey("viagem")) {
            Toast.makeText(this, "Erro ao mostrar viagem", Toast.LENGTH_LONG).show();
            return;
        }

        DetalhesViagem viagem = (DetalhesViagem) extras.get("viagem");
        if (viagem == null) {
            Toast.makeText(this, "Erro ao carregar viagem", Toast.LENGTH_LONG).show();
            return;
        }

        TextView txtOrigem = findViewById(R.id.txtOrigem);
        TextView txtDestino = findViewById(R.id.txtDestino);
        TextView txtEixos = findViewById(R.id.txtEixos);
        TextView txtDistancia = findViewById(R.id.txtDistancia);
        TextView txtDuracaoViagem = findViewById(R.id.txtDuracaoViagem);
        TextView txtPedagios = findViewById(R.id.txtPedagios);
        TextView txtCombustivel = findViewById(R.id.txtCombustivel);
        TextView txtTotalCombustivel = findViewById(R.id.txtTotalCombustivel);
        TextView txtTotal = findViewById(R.id.txtTotal);
        TextView txtValorFreteGeral = findViewById(R.id.txtValorFreteGeral);
        TextView txtValorFreteGranel = findViewById(R.id.txtValorFreteGranel);
        TextView txtValorFreteNeogranel = findViewById(R.id.txtValorFreteNeogranel);
        TextView txtValorFreteFrigorificada = findViewById(R.id.txtValorFreteFrigorificada);
        TextView txtValorFretePerigosa = findViewById(R.id.txtValorFretePerigosa);


        txtOrigem.setText(viagem.getOrigem());
        txtDestino.setText(viagem.getDestino());
        txtEixos.setText("" + viagem.getEixos());
        txtDistancia.setText(String.format(Locale.US, "%.2f Km", (viagem.getDistancia() / 1000)));

        long tempo = viagem.getDuracao();
        if (tempo < 60) {
            txtDuracaoViagem.setText(String.format(Locale.US,"%d segundos", tempo));
        } else if (tempo < 3600) {
            txtDuracaoViagem.setText(String.format(Locale.US,"%d minutos", (tempo / 60)));
        } else {
            txtDuracaoViagem.setText(String.format(Locale.US,"%d horas", ((tempo / 60) / 60)));
        }

        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        nf.setMinimumFractionDigits(2);
        txtPedagios.setText(nf.format(viagem.getPedagio()));
        txtCombustivel.setText(String.format("%s L", viagem.getConsumoCombustivel()));
        txtTotalCombustivel.setText(nf.format(viagem.getTotalCombustivel()));
        txtTotal.setText(nf.format(viagem.getTotal()));
        txtValorFreteGeral.setText(String.format("%s + pedágio", nf.format(viagem.getValorFreteGeral())));
        txtValorFreteGranel.setText(String.format("%s + pedágio", nf.format(viagem.getValorFreteGranel())));
        txtValorFreteNeogranel.setText(String.format("%s + pedágio", nf.format(viagem.getValorFreteNeogranel())));
        txtValorFreteFrigorificada.setText(String.format("%s + pedágio", nf.format(viagem.getValorFreteFrigorificada())));
        txtValorFretePerigosa.setText(String.format("%s + pedágio", nf.format(viagem.getValorFretePerigosa())));

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
