package com.example.truckpad;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.truckpad.data.Sqlite;
import com.example.truckpad.model.Cidade;
import com.example.truckpad.model.Cidade_destino;
import com.example.truckpad.model.Rota;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;

import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText edtconscid;
    private EditText edtciddestino;
    private EditText edteixos;
    private EditText edtkmlitro;
    private EditText edtprecolitro;
    private Button button_consultar;
    private Button button_historico;
    private Cidade cidade;
    private Cidade_destino cidade_destino;
    private Consrota task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtconscid = findViewById(R.id.edit_cidorigem);
        edtciddestino = findViewById(R.id.edit_ciddestino);
        edteixos = findViewById(R.id.edit_nroeixos);
        edtkmlitro = findViewById(R.id.edit_kmlitro);
        edtprecolitro = findViewById(R.id.edit_precolitro);
        button_consultar = findViewById(R.id.button_calcular);
        button_historico = findViewById(R.id.button_historico);

        conscid();
    }

    public void conscid(){

        edtconscid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if (edtconscid.getText().toString().isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, MapActivity.class);
                    intent.putExtra("cid", "orig");
                    startActivityForResult(intent, 0);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

            }


        });
        edtciddestino.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if (edtciddestino.getText().toString().isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, MapActivity.class);
                    intent.putExtra("cid", "dest");
                    startActivityForResult(intent, 0);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        button_consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtconscid.getText().toString().isEmpty()){

                    Toast.makeText(MainActivity.this,"Selecione a cidade de origem",Toast.LENGTH_LONG).show();
                    return;
                }
                if (edtciddestino.getText().toString().isEmpty()){

                    Toast.makeText(MainActivity.this,"Selecione a cidade de destino",Toast.LENGTH_LONG).show();
                    return;
                }
                if (edteixos.getText().toString().isEmpty()){

                    Toast.makeText(MainActivity.this,"Selecione o numero de eixos",Toast.LENGTH_LONG).show();
                    return;
                }else {

                    int nro = Integer.parseInt(edteixos.getText().toString());
                    if (nro <2 || nro > 9){

                        Toast.makeText(MainActivity.this,"Numero de eixos tem de estar entre 2 e 9",Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                if (edtkmlitro.getText().toString().isEmpty()){

                    Toast.makeText(MainActivity.this,"Informe o consumo medio",Toast.LENGTH_LONG).show();
                    return;
                }
                if (edtprecolitro.getText().toString().isEmpty()){

                    Toast.makeText(MainActivity.this,"Informe o valor do LT de combustivel",Toast.LENGTH_LONG).show();
                    return;
                }
                task = new Consrota();
                task.execute();

            }
        });

        button_historico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,Historico.class);
                startActivity(intent);
            }
        });

    }


    protected void onActivityResult(int codigo, int resultado, Intent i) {
        super.onActivityResult(codigo, resultado, i);

        //quando o retorno for 100 o usuario encerrou a atividade map sem usar o botao confirmar
        if (resultado == 100){
            return;
        }
        Bundle bundle = i.getExtras();
        if (resultado == 1) {
            cidade = (Cidade) bundle.getSerializable("cid");
            edtconscid.setText(cidade.getNome());
        }
        if (resultado == 2){
            cidade_destino = (Cidade_destino) bundle.getSerializable("cid");
            edtciddestino.setText(cidade_destino.getNome());
        }
    }

    private class Consrota extends AsyncTask<Void, Integer, String> {

        private URL url = null;
        private Uri.Builder builder;
        private HttpURLConnection conn;
        private StringBuilder result;
        private ProgressDialog pDialog;
        private Sqlite sqlite;

        @Override
        protected void onPreExecute() {

            this.builder = new Uri.Builder();
            sqlite = new Sqlite(MainActivity.this);

            result = new StringBuilder();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Consultando...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                url = new URL("https://geo.api.truckpad.io/v1/route");
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-type", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                PrintStream printStream = new PrintStream(conn.getOutputStream());
                //crio json manualmente, obs:mudar para um objeto posterior
                String json=("{\n" +
                        "    \"places\": [{\n" +
                        "        \"point\": [\n" +
                        "       \t    "+cidade.getLongitude()+",\n" +
                        "       \t    "+cidade.getLatitude()+"\n" +
                        "        ]\n" +
                        "    },{\n" +
                        "        \"point\": [\n" +
                        "            "+cidade_destino.getLongitude()+",\n" +
                        "            "+cidade_destino.getLatitude()+"\n" +
                        "        ]\n" +
                        "    }],\n" +
                        "    \"fuel_consumption\": "+edtkmlitro.getText().toString()+",\n" +
                        "    \"fuel_price\": "+edtprecolitro.getText().toString()+"\n" +
                        "}");
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
                    int leng = line.length();
                    String copy = line.substring(0,line.indexOf("route"));
                    String copy2 = line.substring(line.indexOf("fuel_usage"),leng);
                    copy = copy + copy2;
                    result.append(copy);

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
                    pDialog.dismiss();
                    Toast.makeText(MainActivity.this,"Tente consultar novamente",Toast.LENGTH_LONG).show();
                    return;
                }
                try {

                    JSONObject obj = new JSONObject(result);
                    Rota rota = new Rota(cidade.getLatitude(),cidade.getLongitude(),
                    cidade_destino.getLatitude(),cidade_destino.getLongitude(),
                            obj.getString("distance"),
                            obj.getDouble("fuel_usage"),
                            obj.getString("fuel_usage_unit"),
                            obj.getDouble("fuel_cost"),
                            obj.getDouble("total_cost"),
                    cidade.getNome(),cidade_destino.getNome());

                    //add rota ao banco de dados
                    sqlite.addRota(rota);
                    pDialog.dismiss();

                    edtprecolitro.setText("");
                    edtkmlitro.setText("");
                    edtciddestino.setText("");
                    edtconscid.setText("");
                    edteixos.setText("");

                    Intent intent = new Intent(MainActivity.this,DetailRota.class);
                    intent.putExtra("rota",rota);
                    startActivity(intent);


                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
