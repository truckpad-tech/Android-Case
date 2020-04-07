package br.com.wesley.test.android_case;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.util.Locale;

import br.com.wesley.test.android_case.model.DetalhesViagem;


public class DetalhesRotaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_rota);


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
        txtEixos.setText(String.format("%d", viagem.getEixos()));
        String distancia = "" + viagem.getDistancia();
        txtDistancia.setText(distancia);
        String duracao = "" + viagem.getDuracao();
        txtDuracaoViagem.setText(duracao);
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        txtPedagios.setText(nf.format(viagem.getPedagio()));
        txtCombustivel.setText(String.format("%s/l", viagem.getConsumoCombustivel()));
        txtTotalCombustivel.setText(nf.format(viagem.getTotalCombustivel()));
        txtTotal.setText(nf.format(viagem.getTotal()));
        txtValorFreteGeral.setText(nf.format(viagem.getValorFreteGeral()));
        txtValorFreteGranel.setText(nf.format(viagem.getValorFreteGranel()));
        txtValorFreteNeogranel.setText(nf.format(viagem.getValorFreteNeogranel()));
        txtValorFreteFrigorificada.setText(nf.format(viagem.getValorFreteFrigorificada()));
        txtValorFretePerigosa.setText(nf.format(viagem.getValorFretePerigosa()));

    }

}
