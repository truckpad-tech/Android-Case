package com.example.truckpad;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.truckpad.data.Sqlite;
import com.example.truckpad.model.Fretes;
import com.example.truckpad.model.Rota;
import com.example.truckpad.view.Adapter_frete;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DetailRota extends AppCompatActivity {

    private TextView textVorigem;
    private TextView textVdestino;
    private TextView textVdistancia;
    private TextView textVcombustivel;
    private TextView textVvalor;
    private Rota rota;
    private Consfrete task;
    private ListView lvfretes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_rota);

        textVcombustivel = findViewById(R.id.textV_detailcombust);
        textVdestino = findViewById(R.id.textV_detaildestino);
        textVdistancia = findViewById(R.id.textV_detaildistancia);
        textVorigem = findViewById(R.id.textV_detailorigem);
        textVvalor = findViewById(R.id.textV_detailvalor);
        lvfretes = findViewById(R.id.listv_fretes);

        Bundle bundle = getIntent().getExtras();
        rota = (Rota) bundle.getSerializable("rota");

        DecimalFormat doisDigitos = new DecimalFormat("###,##0.00");
        double xvlr = rota.getTotal_cost();
        textVvalor.setText(doisDigitos.format(xvlr));
        textVorigem.setText(rota.getCidorigem());
        double xdistancia = Double.parseDouble(rota.getDistancia());
        xdistancia = xdistancia / 1000;
        textVdistancia.setText(String.valueOf(xdistancia));
        textVdestino.setText(rota.getCiddestino());
        textVcombustivel.setText(String.valueOf(rota.getCombustivel_usado()));

        task = new Consfrete();
        task.execute();
    }

    private class Consfrete extends AsyncTask<Void, Integer, String> {

        private URL url = null;
        private Uri.Builder builder;
        private HttpURLConnection conn;
        private StringBuilder result;
        private Adapter_frete adapter_frete;

        @Override
        protected void onPreExecute() {

            this.builder = new Uri.Builder();
            result = new StringBuilder();
        }
        @Override
        protected String doInBackground(Void... voids) {

            try {
                url = new URL("https://tictac.api.truckpad.io/v1/antt_price/all");
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-type", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                PrintStream printStream = new PrintStream(conn.getOutputStream());
                //crio json manualmente, obs:mudar para um objeto posterior
                double xdistancia = Double.parseDouble(textVdistancia.getText().toString());
                String json=("{\"axis\":2,\"distance\":"+String.valueOf(xdistancia)+",\"has_return_shipment\":true}");
                printStream.println(json);
                conn.connect();

                int response_code = 0;
                if (conn.getResponseCode() != 0) {

                    response_code = conn.getResponseCode();
                }

                if (response_code == HttpURLConnection.HTTP_OK) {
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    String line;
                    line = reader.readLine();
                    result.append(line);

                }else {
                    result.append("fail");
                }

            } catch (MalformedURLException e) {
                result.append("fail");
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                result.append("fail");
                e.printStackTrace();
            } catch (ProtocolException e) {
                result.append("fail");
                e.printStackTrace();
            } catch (IOException e) {
                result.append("fail");
                e.printStackTrace();
            }
            return result.toString();
        }
        @Override
        protected void onPostExecute(String result) {

            if (!result.isEmpty()){
                if (result.contains("fail")){
                    Toast.makeText(DetailRota.this,"Falha ao consultar fretes",Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    JSONObject obj = new JSONObject(result);
                    List<Fretes> list = new ArrayList<Fretes>();
                    DecimalFormat doisDigitos = new DecimalFormat("###,##0.00");

                    double xfrigo, xgeral, xgranel, xneogranel, xperig;
                    xfrigo = obj.getDouble("frigorificada");
                    xgeral = obj.getDouble("geral");
                    xgranel = obj.getDouble("granel");
                    xneogranel = obj.getDouble("neogranel");
                    xperig = obj.getDouble("perigosa");
                    Fretes fretes = new Fretes(doisDigitos.format(xfrigo),
                            doisDigitos.format(xgeral),
                            doisDigitos.format(xgranel),
                            doisDigitos.format(xneogranel),
                            doisDigitos.format(xperig));
                    list.add(fretes);

                    adapter_frete = new Adapter_frete(DetailRota.this,list);
                    lvfretes.setAdapter(adapter_frete);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
